package by.softarex.internship.task.questionnaireportalsystem.util;

import by.softarex.internship.task.questionnaireportalsystem.dto.QuestionnaireResponseDto;
import by.softarex.internship.task.questionnaireportalsystem.entity.QuestionnaireResponse;
import org.springframework.stereotype.Component;

@Component
public class QuestionnaireResponseEntityMapper {
    public QuestionnaireResponseDto toResponseDto(QuestionnaireResponse questionnaireResponse) {
        QuestionnaireResponseDto questionnaireResponseDto = new QuestionnaireResponseDto();
        questionnaireResponseDto.setValue(questionnaireResponse.getValue());
        questionnaireResponseDto.setPosition(questionnaireResponse.getPosition() + 1);
        return questionnaireResponseDto;
    }

    public QuestionnaireResponse toResponseEntity(QuestionnaireResponseDto questionnaireResponseDto) {
        QuestionnaireResponse questionnaireResponse = new QuestionnaireResponse();
        questionnaireResponse.setPosition(questionnaireResponseDto.getPosition() - 1);
        questionnaireResponse.setValue(questionnaireResponseDto.getValue());
        return questionnaireResponse;
    }
}
