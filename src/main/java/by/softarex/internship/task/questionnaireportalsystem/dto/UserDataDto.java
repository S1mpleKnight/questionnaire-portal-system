package by.softarex.internship.task.questionnaireportalsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@Schema(description = "Entity to update user data")
public class UserDataDto {
    @NotBlank(message = "Enter a valid firstname")
    @Pattern(regexp = "[a-zA-Z]{2,35}", message = "Enter a valid firstname")
    private String firstname;
    @NotBlank(message = "Enter a valid lastname")
    @Pattern(regexp = "[a-zA-Z]{2,35}", message = "Enter a valid lastname")
    private String lastname;
    @Email(message = "Enter a valid email address")
    @NotBlank(message = "Enter a valid email address")
    private String email;
    @NotBlank
    @Pattern(regexp = "[+]?[\\d]{11,14}", message = "Enter a valid phone number")
    private String phone;
}
