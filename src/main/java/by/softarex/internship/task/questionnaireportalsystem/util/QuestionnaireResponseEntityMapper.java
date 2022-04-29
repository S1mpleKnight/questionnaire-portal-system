package by.softarex.internship.task.questionnaireportalsystem.util;

import by.softarex.internship.task.questionnaireportalsystem.dto.QuestionnaireResponseDto;
import by.softarex.internship.task.questionnaireportalsystem.entity.Field;
import by.softarex.internship.task.questionnaireportalsystem.entity.QuestionnaireResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QuestionnaireResponseEntityMapper {
    public QuestionnaireResponseDto toResponseDto(QuestionnaireResponse questionnaireResponse, List<Field> fields) {
        QuestionnaireResponseDto questionnaireResponseDto = new QuestionnaireResponseDto();
        questionnaireResponseDto.setValue(questionnaireResponse.getValue());
        questionnaireResponseDto.setFieldPosition(((Integer) fields.lastIndexOf(questionnaireResponse.getField())).toString());
        return questionnaireResponseDto;
    }
}
