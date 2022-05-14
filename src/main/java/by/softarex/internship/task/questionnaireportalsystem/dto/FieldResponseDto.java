package by.softarex.internship.task.questionnaireportalsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@Schema(description = "Questionnaire response entity")
public class FieldResponseDto {
    @NotBlank
    @NotNull
    private String value;
    @Positive
    private Integer position;
}
