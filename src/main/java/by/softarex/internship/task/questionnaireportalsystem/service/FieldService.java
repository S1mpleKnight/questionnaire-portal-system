package by.softarex.internship.task.questionnaireportalsystem.service;

import by.softarex.internship.task.questionnaireportalsystem.dto.FieldDto;
import by.softarex.internship.task.questionnaireportalsystem.entity.Field;
import by.softarex.internship.task.questionnaireportalsystem.entity.FieldOption;
import by.softarex.internship.task.questionnaireportalsystem.entity.FieldType;
import by.softarex.internship.task.questionnaireportalsystem.entity.Questionnaire;
import by.softarex.internship.task.questionnaireportalsystem.entity.Response;
import by.softarex.internship.task.questionnaireportalsystem.entity.User;
import by.softarex.internship.task.questionnaireportalsystem.exception.FieldNotExistException;
import by.softarex.internship.task.questionnaireportalsystem.exception.QuestionnaireNotExistException;
import by.softarex.internship.task.questionnaireportalsystem.repository.FieldOptionRepository;
import by.softarex.internship.task.questionnaireportalsystem.repository.FieldRepository;
import by.softarex.internship.task.questionnaireportalsystem.repository.QuestionnaireRepository;
import by.softarex.internship.task.questionnaireportalsystem.repository.ResponseRepository;
import by.softarex.internship.task.questionnaireportalsystem.repository.UserRepository;
import by.softarex.internship.task.questionnaireportalsystem.util.EntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Scope("singleton")
@AllArgsConstructor
public class FieldService {
    private final FieldRepository fieldRepository;
    private final QuestionnaireRepository questionnaireRepository;
    private final UserRepository userRepository;
    private final FieldOptionRepository fieldOptionRepository;
    private final ResponseRepository responseRepository;
    private final EntityMapper mapper;

    public Page<FieldDto> findAllByUserEmail(Principal principal, Pageable pageable) {
        List<Field> fields = getAllField(principal);
        return new PageImpl<>(fields.stream().map(mapper::mapToFieldDto).collect(Collectors.toList()), pageable, fields.size());
    }

    public List<FieldDto> findAllByUserEmail(Principal principal) {
        List<Field> fields = getAllField(principal);
        return fields.stream().map(mapper::mapToFieldDto).collect(Collectors.toList());
    }

    public FieldDto getFieldDto(Principal principal, Integer fieldPosition) {
        return mapper.mapToFieldDto(getField(principal.getName(), fieldPosition));
    }

    @Transactional
    public void save(Principal principal, FieldDto fieldDto) {
        Questionnaire questionnaire = getQuestionnaire(principal);
        Field field = mapper.mapToFieldEntity(fieldDto);
        field.setQuestionnaire(questionnaire);
        fieldRepository.save(field);
        saveFieldOptions(field);
    }

    @Transactional
    public void delete(Principal principal, Integer fieldPosition) {
        Field field = getField(principal.getName(), fieldPosition);
        deleteDependEntities(field);
        fieldRepository.delete(field);
    }

    @Transactional
    public void update(Principal principal, Integer fieldPosition, FieldDto fieldDto) {
        Field field = getField(principal.getName(), fieldPosition);
        deleteDependEntities(field);
        Field newField = mapper.mapToFieldEntity(fieldDto);
        newField.setId(field.getId());
        fieldRepository.save(newField);
        saveFieldOptions(newField);
    }

    private List<Field> getAllField(Principal principal) {
        Optional<Questionnaire> questionnaire = questionnaireRepository.findByUser_Email(principal.getName());
        return questionnaire.isPresent()
                ? fieldRepository.findAllByQuestionnaire_Id(questionnaire.get().getId())
                : Collections.emptyList();
    }

    private void deleteDependEntities(Field field) {
        deleteFieldOptions(field);
        deleteFieldResponses(field);
    }

    private void deleteFieldResponses(Field field) {
        List<Response> responses = responseRepository.findAllByField(field);
        if (!responses.isEmpty()) {
            responseRepository.deleteAll(responses);
        }
    }

    private Field getField(String currentUserEmail, Integer fieldPosition) {
        List<Field> fields = getQuestionnaireFields(currentUserEmail, fieldPosition);
        return fields.get(fieldPosition - 1);
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
            throw new QuestionnaireNotExistException();
        }
        List<Field> fields = fieldRepository.findAllByQuestionnaire_Id(questionnaire.get().getId());
        if (fields.size() < fieldPosition) {
            throw new FieldNotExistException(fieldPosition);
        }
        return fields;
    }

    private Questionnaire getQuestionnaire(Principal principal) {
        Optional<Questionnaire> questionnaire = questionnaireRepository.findByUser_Email(principal.getName());
        return questionnaire.orElseGet(() -> createQuestionnaire(principal));
    }

    private Questionnaire createQuestionnaire(Principal principal) {
        Questionnaire newQuestionnaire = new Questionnaire();
        User user = userRepository.findByEmail(principal.getName());
        user.setQuestionnaire(newQuestionnaire);
        newQuestionnaire.setUser(user);
        questionnaireRepository.save(newQuestionnaire);
        userRepository.save(user);
        return newQuestionnaire;
    }
}
