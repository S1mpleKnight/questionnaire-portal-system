package by.softarex.internship.task.questionnaireportalsystem.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString
public class UserUpdateDto {
    @NotBlank(message = "Enter a valid firstname")
    @Pattern(regexp = "[A-Z][a-z]{3,34}", message = "Enter a valid firstname")
    private String firstname;
    @NotBlank(message = "Enter a valid lastname")
    @Pattern(regexp = "[A-Z][a-z]{2,34}", message = "Enter a valid lastname")
    private String lastname;
    @Email(message = "Enter a valid email address")
    @NotBlank(message = "Enter a valid email address")
    private String email;
    @Pattern(regexp = "[\\d]{12,15}", message = "Enter a valid phone number")
    private String phone;
}
