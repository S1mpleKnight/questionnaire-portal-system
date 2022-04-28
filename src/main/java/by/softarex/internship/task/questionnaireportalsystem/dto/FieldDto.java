package by.softarex.internship.task.questionnaireportalsystem.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@ToString
@Getter
@Setter
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
