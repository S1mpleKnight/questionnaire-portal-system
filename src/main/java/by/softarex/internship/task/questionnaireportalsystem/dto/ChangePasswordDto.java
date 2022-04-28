package by.softarex.internship.task.questionnaireportalsystem.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@ToString
public class ChangePasswordDto {
    @Size(max = 255, min = 10, message = "Enter a valid password")
    @NotBlank(message = "Old password is empty")
    private String oldPassword;
    @Size(max = 255, min = 10, message = "Enter a valid password")
    @NotBlank(message = "New password is empty")
    private String newPassword;
}
