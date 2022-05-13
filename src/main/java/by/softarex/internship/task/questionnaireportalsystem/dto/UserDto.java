package by.softarex.internship.task.questionnaireportalsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Schema(description = "User entity")
public class UserDto extends UserDataDto {
    @NotBlank(message = "Enter a valid password")
    @Size(max = 255, min = 10, message = "Enter a valid password")
    private String password;
}
