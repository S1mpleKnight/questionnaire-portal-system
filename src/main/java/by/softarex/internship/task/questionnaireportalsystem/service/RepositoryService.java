package by.softarex.internship.task.questionnaireportalsystem.service;

import by.softarex.internship.task.questionnaireportalsystem.dto.ResponseDto;
import by.softarex.internship.task.questionnaireportalsystem.entity.Field;
import by.softarex.internship.task.questionnaireportalsystem.entity.Questionnaire;
import by.softarex.internship.task.questionnaireportalsystem.entity.Response;
import by.softarex.internship.task.questionnaireportalsystem.exception.FieldNotExistException;
import by.softarex.internship.task.questionnaireportalsystem.exception.QuestionnaireNotExistException;
import by.softarex.internship.task.questionnaireportalsystem.repository.FieldRepository;
import by.softarex.internship.task.questionnaireportalsystem.repository.QuestionnaireRepository;
import by.softarex.internship.task.questionnaireportalsystem.repository.ResponseRepository;
import by.softarex.internship.task.questionnaireportalsystem.util.EntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Scope("singleton")
@AllArgsConstructor
public class RepositoryService {
    private final ResponseRepository responseRepository;
    private final QuestionnaireRepository questionnaireRepository;
    private final FieldRepository fieldRepository;
    private final EntityMapper entityMapper;

    public Page<ResponseDto> findAllByUserId(UUID currentUserId, Pageable pageable) {
        Optional<Questionnaire> questionnaire = questionnaireRepository.findByUser_Id(currentUserId);
        List<Field> fieldList = fieldRepository.findAllByQuestionnaire_Id(questionnaire.get().getId());
        List<ResponseDto> responses = responseRepository.findAllByQuestionnaireOrderByAnswerId(questionnaire.get())
                .stream()
                .map(r -> entityMapper.mapToResponseDto(r, fieldList))
                .collect(Collectors.toList());
        return new PageImpl<>(responses, pageable, responses.size());
    }

    public void save(List<ResponseDto> responseDtos, UUID questionnaireId) {
        if (!questionnaireRepository.existsById(questionnaireId)) {
            throw new QuestionnaireNotExistException();
        }
        Questionnaire questionnaire = questionnaireRepository.findById(questionnaireId).get();
        List<Field> fields = fieldRepository.findAllByQuestionnaire_Id(questionnaireId);
        List<Response> responses = getCreatedResponses(responseDtos, questionnaire, fields);
        responseRepository.saveAll(responses);
    }

    private List<Response> getCreatedResponses(List<ResponseDto> responseDtos, Questionnaire questionnaire, List<Field> fields) {
        List<Response> responses = new ArrayList<>();
        for (ResponseDto responseDto : responseDtos) {
            createResponse(questionnaire, fields, responses, responseDto);
        }
        return responses;
    }

    private void createResponse(Questionnaire questionnaire, List<Field> fields, List<Response> responses, ResponseDto responseDto) {
        Response response = new Response();
        response.setValue(responseDto.getValue());
        setCorrespondField(fields, responseDto, response);
        response.setAnswerId(getAnswerId());
        response.setQuestionnaire(questionnaire);
        responses.add(response);
    }

    private void setCorrespondField(List<Field> fields, ResponseDto responseDto, Response response) {
        int fieldPos = Integer.parseInt(responseDto.getFieldPosition());
        if (fields.size() < fieldPos) {
            throw new FieldNotExistException(fieldPos);
        }
        response.setField(fields.get(fieldPos - 1));
    }

    private UUID getAnswerId() {
        UUID answerId = UUID.randomUUID();
        while (responseRepository.existsByAnswerId(answerId)) {
            answerId = UUID.randomUUID();
        }
        return answerId;
    }
}
