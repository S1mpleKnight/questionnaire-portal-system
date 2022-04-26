package by.softarex.internship.task.questionnaireportalsystem.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
public class UserDto extends UserUpdateDto {
    @NotBlank(message = "Enter a valid password")
    @Size(max = 255, min = 10, message = "Enter a valid password")
    private String password;
}
