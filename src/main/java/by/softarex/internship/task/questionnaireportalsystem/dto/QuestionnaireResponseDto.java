package by.softarex.internship.task.questionnaireportalsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Questionnaire response entity")
public class QuestionnaireResponseDto {
    private String value;
    private String fieldPosition;
}
