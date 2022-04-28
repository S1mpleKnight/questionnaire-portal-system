package by.softarex.internship.task.questionnaireportalsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class AuthenticationRequestDto {
    @Email
    private String email;
    @Size(max = 255, min = 10, message = "Enter a valid password")
    @NotBlank(message = "Enter a valid password")
    private String password;
}