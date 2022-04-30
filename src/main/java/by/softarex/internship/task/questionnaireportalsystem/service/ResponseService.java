package by.softarex.internship.task.questionnaireportalsystem.service;

import by.softarex.internship.task.questionnaireportalsystem.dto.QuestionnaireResponseDto;
import by.softarex.internship.task.questionnaireportalsystem.entity.Field;
import by.softarex.internship.task.questionnaireportalsystem.entity.Questionnaire;
import by.softarex.internship.task.questionnaireportalsystem.entity.QuestionnaireResponse;
import by.softarex.internship.task.questionnaireportalsystem.exception.FieldNotExistException;
import by.softarex.internship.task.questionnaireportalsystem.exception.QuestionnaireNotExistException;
import by.softarex.internship.task.questionnaireportalsystem.repository.FieldRepository;
import by.softarex.internship.task.questionnaireportalsystem.repository.QuestionnaireRepository;
import by.softarex.internship.task.questionnaireportalsystem.repository.ResponseRepository;
import by.softarex.internship.task.questionnaireportalsystem.util.FieldEntityMapper;
import by.softarex.internship.task.questionnaireportalsystem.util.QuestionnaireResponseEntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Scope("singleton")
@AllArgsConstructor
public class ResponseService {
    private final ResponseRepository responseRepository;
    private final QuestionnaireRepository questionnaireRepository;
    private final FieldRepository fieldRepository;
    private final QuestionnaireResponseEntityMapper questionnaireResponseEntityMapper;

    public Page<QuestionnaireResponseDto> findAllByUserId(Principal principal, Pageable pageable) {
        Optional<Questionnaire> questionnaire = questionnaireRepository.findByUser_Email(principal.getName());
        List<Field> fieldList = fieldRepository.findAllByQuestionnaire_IdOrderByPositionAsc(questionnaire.get().getId());
        List<QuestionnaireResponseDto> questionnaireResponses = responseRepository.findAllByQuestionnaireOrderByAnswerId(questionnaire.get())
                .stream()
                .map(r -> questionnaireResponseEntityMapper.toResponseDto(r, fieldList))
                .collect(Collectors.toList());
        return new PageImpl<>(questionnaireResponses, pageable, questionnaireResponses.size());
    }

    public void save(List<QuestionnaireResponseDto> questionnaireResponseDtos, UUID questionnaireId) {
        if (!questionnaireRepository.existsById(questionnaireId)) {
            throw new QuestionnaireNotExistException();
        }
        Questionnaire questionnaire = questionnaireRepository.findById(questionnaireId).get();
        List<Field> fields = fieldRepository.findAllByQuestionnaire_IdOrderByPositionAsc(questionnaireId);
        List<QuestionnaireResponse> questionnaireResponses = getCreatedResponses(questionnaireResponseDtos, questionnaire, fields);
        responseRepository.saveAll(questionnaireResponses);
    }

    private List<QuestionnaireResponse> getCreatedResponses(List<QuestionnaireResponseDto> questionnaireResponseDtos, Questionnaire questionnaire, List<Field> fields) {
        List<QuestionnaireResponse> questionnaireResponses = new ArrayList<>();
        for (QuestionnaireResponseDto questionnaireResponseDto : questionnaireResponseDtos) {
            createResponse(questionnaire, fields, questionnaireResponses, questionnaireResponseDto);
        }
        return questionnaireResponses;
    }

    private void createResponse(Questionnaire questionnaire, List<Field> fields, List<QuestionnaireResponse> respons, QuestionnaireResponseDto questionnaireResponseDto) {
        QuestionnaireResponse questionnaireResponse = new QuestionnaireResponse();
        questionnaireResponse.setValue(questionnaireResponseDto.getValue());
        setCorrespondField(fields, questionnaireResponseDto, questionnaireResponse);
        questionnaireResponse.setAnswerId(getAnswerId());
        questionnaireResponse.setQuestionnaire(questionnaire);
        respons.add(questionnaireResponse);
    }

    private void setCorrespondField(List<Field> fields, QuestionnaireResponseDto questionnaireResponseDto, QuestionnaireResponse questionnaireResponse) {
        int fieldPos = Integer.parseInt(questionnaireResponseDto.getFieldPosition());
        if (fields.size() < fieldPos) {
            throw new FieldNotExistException(fieldPos);
        }
        questionnaireResponse.setField(fields.get(fieldPos - 1));
    }

    private UUID getAnswerId() {
        UUID answerId = UUID.randomUUID();
        while (responseRepository.existsByAnswerId(answerId)) {
            answerId = UUID.randomUUID();
        }
        return answerId;
    }
}
