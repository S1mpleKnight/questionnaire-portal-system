package by.softarex.internship.task.questionnaireportalsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Authentication entity")
public class AuthenticationRequestDto {
    @Email
    @NotBlank
    private String email;
    @Size(max = 255, min = 10, message = "Enter a valid password")
    @NotBlank(message = "Enter a valid password")
    private String password;
}