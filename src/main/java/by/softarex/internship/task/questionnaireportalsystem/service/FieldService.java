package by.softarex.internship.task.questionnaireportalsystem.service;

import by.softarex.internship.task.questionnaireportalsystem.dto.FieldDto;
import by.softarex.internship.task.questionnaireportalsystem.entity.Field;
import by.softarex.internship.task.questionnaireportalsystem.entity.FieldOption;
import by.softarex.internship.task.questionnaireportalsystem.entity.FieldType;
import by.softarex.internship.task.questionnaireportalsystem.entity.Questionnaire;
import by.softarex.internship.task.questionnaireportalsystem.entity.Response;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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

    public Page<FieldDto> findAllByUserId(UUID UserId, Pageable pageable) {
        Optional<Questionnaire> questionnaire = questionnaireRepository.findByUser_Id(UserId);
        List<Field> fields = questionnaire.isPresent()
                ? fieldRepository.findAllByQuestionnaire_Id(questionnaire.get().getId())
                : Collections.emptyList();
        return new PageImpl<>(fields.stream().map(mapper::mapToFieldDto).collect(Collectors.toList()), pageable, fields.size());
    }

    public void save(UUID currentUserId, FieldDto fieldDto) {
        Questionnaire questionnaire = getQuestionnaire(currentUserId);
        Field field = mapper.mapToFieldEntity(fieldDto);
        field.setQuestionnaire(questionnaire);
        fieldRepository.save(field);
        saveFieldOptions(field);
    }

    public void delete(UUID currentUserId, Integer fieldPosition) {
        Field field = getField(currentUserId, fieldPosition);
        deleteDependEntities(field);
        fieldRepository.delete(field);
    }

    public void update(UUID currentUserId, Integer fieldPosition, FieldDto fieldDto) {
        Field field = getField(currentUserId, fieldPosition);
        deleteDependEntities(field);
        Field newField = mapper.mapToFieldEntity(fieldDto);
        newField.setId(field.getId());
        fieldRepository.save(newField);
        saveFieldOptions(newField);
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

    private Field getField(UUID currentUserId, Integer fieldPosition) {
        List<Field> fields = getQuestionnaireFields(currentUserId, fieldPosition);
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

    private List<Field> getQuestionnaireFields(UUID currentUserId, Integer fieldPosition) {
        Optional<Questionnaire> questionnaire = questionnaireRepository.findByUser_Id(currentUserId);
        if (questionnaire.isEmpty()) {
            throw new QuestionnaireNotExistException();
        }
        List<Field> fields = fieldRepository.findAllByQuestionnaire_Id(questionnaire.get().getId());
        if (fields.size() < fieldPosition) {
            throw new FieldNotExistException(fieldPosition);
        }
        return fields;
    }

    private Questionnaire getQuestionnaire(UUID currentUserId) {
        Optional<Questionnaire> questionnaire = questionnaireRepository.findByUser_Id(currentUserId);
        return questionnaire.orElseGet(() -> createQuestionnaire(currentUserId));
    }

    private Questionnaire createQuestionnaire(UUID currentUserId) {
        Questionnaire newQuestionnaire = new Questionnaire();
        newQuestionnaire.setUser(userRepository.findById(currentUserId).get());
        questionnaireRepository.save(newQuestionnaire);
        return newQuestionnaire;
    }
}
