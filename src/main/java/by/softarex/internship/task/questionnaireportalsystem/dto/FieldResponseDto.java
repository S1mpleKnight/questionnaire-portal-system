package by.softarex.internship.task.questionnaireportalsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.Objects;

@Getter
@Setter
@Schema(description = "Questionnaire response entity")
public class FieldResponseDto {
    @NotBlank
    private String value;
    @Positive
    private Integer position;
}
