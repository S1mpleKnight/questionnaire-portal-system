package by.softarex.internship.task.questionnaireportalsystem.service;

import by.softarex.internship.task.questionnaireportalsystem.dto.FieldDto;
import by.softarex.internship.task.questionnaireportalsystem.entity.Field;
import by.softarex.internship.task.questionnaireportalsystem.entity.FieldOption;
import by.softarex.internship.task.questionnaireportalsystem.entity.FieldType;
import by.softarex.internship.task.questionnaireportalsystem.entity.Questionnaire;
import by.softarex.internship.task.questionnaireportalsystem.entity.QuestionnaireResponse;
import by.softarex.internship.task.questionnaireportalsystem.entity.User;
import by.softarex.internship.task.questionnaireportalsystem.exception.FieldNotExistException;
import by.softarex.internship.task.questionnaireportalsystem.exception.QuestionnaireNotExistException;
import by.softarex.internship.task.questionnaireportalsystem.repository.FieldOptionRepository;
import by.softarex.internship.task.questionnaireportalsystem.repository.FieldRepository;
import by.softarex.internship.task.questionnaireportalsystem.repository.QuestionnaireRepository;
import by.softarex.internship.task.questionnaireportalsystem.repository.QuestionnaireResponseRepository;
import by.softarex.internship.task.questionnaireportalsystem.repository.UserRepository;
import by.softarex.internship.task.questionnaireportalsystem.util.FieldMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Scope("singleton")
@RequiredArgsConstructor
public class FieldService {
    private final FieldRepository fieldRepository;
    private final QuestionnaireRepository questionnaireRepository;
    private final UserRepository userRepository;
    private final FieldOptionRepository fieldOptionRepository;
    private final QuestionnaireResponseRepository questionnaireResponseRepository;
    private final FieldMapper mapper;

    public Page<FieldDto> findAllByUserEmail(Principal principal, Pageable pageable) {
        List<Field> fields = getAllField(principal);
        return new PageImpl<>(fields
                .stream()
                .map(mapper::toFieldDto)
                .collect(Collectors.toList()), pageable, fields.size());
    }

    public List<FieldDto> findAllByUserEmail(Principal principal) {
        List<Field> fields = getAllField(principal);
        return fields
                .stream()
                .map(mapper::toFieldDto)
                .collect(Collectors.toList());
    }

    public FieldDto getFieldDto(Principal principal, Integer fieldPosition) {
        return mapper.toFieldDto(getField(principal.getName(), fieldPosition - 1));
    }

    @Transactional
    public FieldDto save(Principal principal, FieldDto fieldDto) {
        Questionnaire questionnaire = getQuestionnaire(principal);
        Field field = getPreparedField(fieldDto, questionnaire);
        fieldRepository.save(field);
        saveFieldOptions(field);
        return mapper.toFieldDto(field);
    }

    @Transactional
    public FieldDto delete(Principal principal, Integer fieldPosition) {
        Field field = getField(principal.getName(), fieldPosition - 1);
        FieldDto result = getFieldDto(principal, fieldPosition);
        deleteDependEntities(field);
        List<Field> fields = calculateNewPositions(principal, fieldPosition);
        fieldRepository.delete(field);
        fieldRepository.saveAll(fields);
        return result;
    }

    @Transactional
    public FieldDto update(Principal principal, Integer fieldPosition, FieldDto fieldDto) {
        Field field = getField(principal.getName(), fieldPosition - 1);
        Field newField = updateOldFieldData(fieldDto, field);
        fieldRepository.save(newField);
        saveFieldOptions(newField);
        return mapper.toFieldDto(newField);
    }

    private Field updateOldFieldData(FieldDto fieldDto, Field field) {
        deleteDependEntities(field);
        Field newField = mapper.toFieldEntity(fieldDto);
        newField.setPosition(field.getPosition());
        newField.setId(field.getId());
        newField.setQuestionnaire(field.getQuestionnaire());
        return newField;
    }

    private Field getPreparedField(FieldDto fieldDto, Questionnaire questionnaire) {
        Field field = mapper.toFieldEntity(fieldDto);
        field.setPosition(fieldRepository.countAllByQuestionnaire(questionnaire));
        field.setQuestionnaire(questionnaire);
        return field;
    }

    private List<Field> calculateNewPositions(Principal principal, Integer fieldPosition) {
        List<Field> fields = getAllField(principal);
        fields = fields.stream()
                .filter(f -> f.getPosition() > fieldPosition - 1)
                .peek(f -> f.setPosition(f.getPosition() - 1))
                .collect(Collectors.toList());
        return fields;
    }

    private List<Field> getAllField(Principal principal) {
        Optional<Questionnaire> questionnaire = questionnaireRepository.findByUser_Email(principal.getName());
        return questionnaire.isPresent()
                ? fieldRepository.findAllByQuestionnaire_IdOrderByPositionAsc(questionnaire.get().getId())
                : Collections.emptyList();
    }

    private void deleteDependEntities(Field field) {
        deleteFieldOptions(field);
        deleteFieldResponses(field);
    }

    private void deleteFieldResponses(Field field) {
        List<QuestionnaireResponse> responses = questionnaireResponseRepository.findAllByField(field);
        if (!responses.isEmpty()) {
            questionnaireResponseRepository.deleteAll(responses);
        }
    }

    private Field getField(String currentUserEmail, Integer fieldPosition) {
        List<Field> fields = getQuestionnaireFields(currentUserEmail, fieldPosition);
        return fields.stream().filter(f -> Objects.equals(f.getPosition(), fieldPosition)).findFirst().get();
    }

    private void deleteFieldOptions(Field field) {
        if (isFieldMultivariate(field)) {
            List<FieldOption> fieldOptions = fieldOptionRepository.findAllByField(field);
            if (!fieldOptions.isEmpty()) {
                fieldOptionRepository.deleteAll(fieldOptions);
            }
        }
    }

    private void saveFieldOptions(Field field) {
        if (isFieldMultivariate(field)) {
            for (FieldOption o : field.getOptions()) {
                o.setField(field);
            }
            fieldOptionRepository.saveAll(field.getOptions());
        }
    }

    private boolean isFieldMultivariate(Field field) {
        return field.getFieldType() == FieldType.COMBOBOX || field.getFieldType() == FieldType.RADIO_BUTTON;
    }

    private List<Field> getQuestionnaireFields(String currentUserEmail, Integer fieldPosition) {
        Optional<Questionnaire> questionnaire = questionnaireRepository.findByUser_Email(currentUserEmail);
        if (questionnaire.isEmpty()) {
            throw new QuestionnaireNotExistException("There are not questionnaire & fields for the current user");
        }
        List<Field> fields = fieldRepository.findAllByQuestionnaire_IdOrderByPositionAsc(questionnaire.get().getId());
        if (fields.size() <= fieldPosition) {
            throw new FieldNotExistException(fieldPosition + 1);
        }
        return fields;
    }

    private Questionnaire getQuestionnaire(Principal principal) {
        Optional<Questionnaire> questionnaire = questionnaireRepository.findByUser_Email(principal.getName());
        return questionnaire.orElseGet(() -> createQuestionnaire(principal));
    }

    private Questionnaire createQuestionnaire(Principal principal) {
        Questionnaire newQuestionnaire = new Questionnaire();
        User user = userRepository.findByEmail(principal.getName()).get();
        user.setQuestionnaire(newQuestionnaire);
        newQuestionnaire.setUser(user);
        questionnaireRepository.save(newQuestionnaire);
        userRepository.save(user);
        return newQuestionnaire;
    }
}
