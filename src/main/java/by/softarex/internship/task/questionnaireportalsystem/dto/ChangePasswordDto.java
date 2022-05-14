package by.softarex.internship.task.questionnaireportalsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@Schema(description = "Dto that hold old & new passwords")
public class ChangePasswordDto {
    @Size(max = 255, min = 10, message = "Enter a valid password")
    @NotBlank(message = "Old password is empty")
    private String oldPassword;
    @Size(max = 255, min = 10, message = "Enter a valid password")
    @NotBlank(message = "New password is empty")
    private String newPassword;
}
