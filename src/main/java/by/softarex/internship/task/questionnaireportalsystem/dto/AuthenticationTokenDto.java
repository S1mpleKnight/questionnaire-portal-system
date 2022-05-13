package by.softarex.internship.task.questionnaireportalsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "Auth token representation")
@AllArgsConstructor
public class AuthenticationTokenDto {
    private String token;
}
