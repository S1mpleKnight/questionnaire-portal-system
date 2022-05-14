package by.softarex.internship.task.questionnaireportalsystem.service;

import by.softarex.internship.task.questionnaireportalsystem.dto.FieldResponseDto;
import by.softarex.internship.task.questionnaireportalsystem.entity.Field;
import by.softarex.internship.task.questionnaireportalsystem.entity.FieldOption;
import by.softarex.internship.task.questionnaireportalsystem.entity.FieldType;
import by.softarex.internship.task.questionnaireportalsystem.entity.Questionnaire;
import by.softarex.internship.task.questionnaireportalsystem.entity.FieldResponse;
import by.softarex.internship.task.questionnaireportalsystem.exception.FieldNotExistException;
import by.softarex.internship.task.questionnaireportalsystem.exception.QuestionnaireNotExistException;
import by.softarex.internship.task.questionnaireportalsystem.exception.QuestionnaireResponseException;
import by.softarex.internship.task.questionnaireportalsystem.repository.FieldOptionRepository;
import by.softarex.internship.task.questionnaireportalsystem.repository.FieldRepository;
import by.softarex.internship.task.questionnaireportalsystem.repository.QuestionnaireRepository;
import by.softarex.internship.task.questionnaireportalsystem.repository.FieldResponseRepository;
import by.softarex.internship.task.questionnaireportalsystem.util.FieldResponseEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Scope("singleton")
@RequiredArgsConstructor
public class FieldResponseService {
    private static final String NO_DATA_STRING = "N/A";
    private final static String RESPONSE_OPTIONS_DELIMITER = ", ";
    private final FieldResponseRepository fieldResponseRepository;
    private final QuestionnaireRepository questionnaireRepository;
    private final FieldRepository fieldRepository;
    private final FieldOptionRepository fieldOptionsRepository;
    private final FieldResponseEntityMapper mapper;

    public Page<FieldResponseDto> findAllByUserId(Principal principal, Pageable pageable) {
        Optional<Questionnaire> questionnaire = questionnaireRepository.findByUser_Email(principal.getName());
        Page<FieldResponse> questionnaireResponses
                = fieldResponseRepository.findAllByQuestionnaireOrderByAnswerId(questionnaire.get(), pageable);
        return questionnaireResponses.map(mapper::toResponseDto);
    }

    @Transactional
    public List<FieldResponseDto> saveAll(List<FieldResponseDto> responses, UUID userId) {
        Optional<Questionnaire> questionnaire = isQuestionnaireExist(userId);
        List<Field> questionnaireFields = fieldRepository.findAllByQuestionnaire_IdOrderByPositionAsc(questionnaire.get().getId());
        UUID answerId = prepareSaving(responses, getRequiredFields(questionnaireFields));
        List<FieldResponse> preparedFieldRespons = getQuestionnaireResponses(responses, questionnaire, questionnaireFields, answerId);
        fieldResponseRepository.saveAll(preparedFieldRespons);
        return preparedFieldRespons.stream().map(mapper::toResponseDto).collect(Collectors.toList());
    }

    private List<FieldResponse> getQuestionnaireResponses(
            List<FieldResponseDto> responses,
            Optional<Questionnaire> questionnaire,
            List<Field> questionnaireFields,
            UUID answerId
    ) {
        List<FieldResponse> preparedFieldRespons = new ArrayList<>();
        Date date = new Date();
        for (FieldResponseDto responseDto : responses) {
            FieldResponse response = prepareQuestionnaireResponse(date, questionnaire, questionnaireFields, answerId, responseDto);
            preparedFieldRespons.add(response);
        }
        return preparedFieldRespons;
    }

    private UUID prepareSaving(List<FieldResponseDto> responses, List<Field> requiredFields) {
        checkCopiedAnswers(responses);
        checkRequiredAnswers(responses, getRequiredFieldsPositions(requiredFields));
        return createAnswerId();
    }

    private FieldResponse prepareQuestionnaireResponse(
            Date date,
            Optional<Questionnaire> questionnaire,
            List<Field> questionnaireFields,
            UUID answerId,
            FieldResponseDto responseDto
    ) {
        Field field = getField(questionnaireFields, responseDto);
        return createQuestionnaireResponse(questionnaire, answerId, responseDto, field, date);
    }

    private List<FieldOption> getFieldOptions(Field field) {
        return (field.getFieldType() == FieldType.COMBOBOX || field.getFieldType() == FieldType.RADIO_BUTTON)
                ? fieldOptionsRepository.findAllByField(field)
                : Collections.emptyList();
    }

    private FieldResponse createQuestionnaireResponse(
            Optional<Questionnaire> questionnaire,
            UUID answerId,
            FieldResponseDto responseDto,
            Field field,
            Date date
    ) {
        FieldResponse response = mapper.toResponseEntity(responseDto);
        response.setAnswerId(answerId);
        response.setQuestionnaire(questionnaire.get());
        response.setField(field);
        response.setDate(date);
        setCorrespondValue(field, response);
        return response;
    }

    private void setCorrespondValue(Field field, FieldResponse response) {
        if (field.isActive()) {
            checkResponseValue(field, response);
        } else {
            if (field.isRequired()) {
                response.setValue(NO_DATA_STRING);
            } else {
                response.setValue(null);
            }
        }
    }

    private void checkResponseValue(Field field, FieldResponse response) {
        List<FieldOption> options = getFieldOptions(field);
        checkRadiobuttonResponseValues(field, response, options);
        checkComboboxResponseValues(field, response, options);
    }

    private void checkRadiobuttonResponseValues(Field field, FieldResponse response, List<FieldOption> options) {
        if (field.getFieldType() == FieldType.RADIO_BUTTON) {
            if (options.stream().filter(o -> o.getValue().equals(response.getValue())).findFirst().isEmpty()) {
                throw new QuestionnaireResponseException("No such response value: " + response.getValue());
            }
        }
    }

    private void checkComboboxResponseValues(Field field, FieldResponse response, List<FieldOption> options) {
        if (field.getFieldType() == FieldType.COMBOBOX) {
            List<String> fieldResponses = Arrays.stream(response.getValue().split(RESPONSE_OPTIONS_DELIMITER)).toList();
            if (!options.stream().map(FieldOption::getValue).collect(Collectors.toList()).containsAll(fieldResponses)) {
                throw new QuestionnaireResponseException("Invalid combobox response values: " + fieldResponses);
            }
        }
    }

    private Field getField(List<Field> questionnaireFields, FieldResponseDto responseDto) {
        if (responseDto.getPosition() - 1 >= questionnaireFields.size()) {
            throw new FieldNotExistException(responseDto.getPosition());
        }
        return questionnaireFields.get(responseDto.getPosition() - 1);
    }

    private void checkRequiredAnswers(List<FieldResponseDto> responses, List<Integer> requiredFieldsPositions) {
        if (!isAllRequiredResponses(responses, requiredFieldsPositions)) {
            throw new QuestionnaireResponseException("There are not all required responses in the answer");
        }
    }

    private List<Integer> getRequiredFieldsPositions(List<Field> requiredFields) {
        return requiredFields
                .stream()
                .map(Field::getPosition)
                .collect(Collectors.toList());
    }

    private void checkCopiedAnswers(List<FieldResponseDto> responses) {
        if (responses.size() != getResponsesDistinctCount(responses)) {
            throw new QuestionnaireResponseException("There are 2 or more responses for the 1 field");
        }
    }

    private boolean isAllRequiredResponses(List<FieldResponseDto> responses, List<Integer> positions) {
        return responses
                .stream()
                .map(FieldResponseDto::getPosition)
                .map(p -> p - 1)
                .collect(Collectors.toList())
                .containsAll(positions);
    }

    private long getResponsesDistinctCount(List<FieldResponseDto> responses) {
        return responses
                .stream()
                .map(FieldResponseDto::getPosition)
                .distinct()
                .count();
    }

    private List<Field> getRequiredFields(List<Field> questionnaireFields) {
        return questionnaireFields
                .stream()
                .filter(f -> f.isActive() && f.isRequired())
                .collect(Collectors.toList());
    }

    private Optional<Questionnaire> isQuestionnaireExist(UUID userId) {
        Optional<Questionnaire> questionnaire = questionnaireRepository.findByUser_Id(userId);
        if (questionnaire.isEmpty()) {
            throw new QuestionnaireNotExistException("Questionnaire does not exist, id: " + userId.toString());
        }
        return questionnaire;
    }

    private UUID createAnswerId() {
        UUID answerId = UUID.randomUUID();
        while (fieldResponseRepository.existsByAnswerId(answerId)) {
            answerId = UUID.randomUUID();
        }
        return answerId;
    }
}
