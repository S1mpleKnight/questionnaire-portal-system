package by.softarex.internship.task.questionnaireportalsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@Schema(description = "Field entity")
public class FieldDto {
    @NotBlank(message = "Enter a valid label")
    private String label;
    private boolean active;
    private boolean required;
    @NotBlank(message = "Select a field type")
    @Pattern(regexp = "[A-Z_]{4,16}", message = "Select valid field type")
    private String fieldType;
    private String fieldOptions;
}
