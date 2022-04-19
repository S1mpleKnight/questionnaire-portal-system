package by.softarex.internship.task.questionnaireportalsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthenticationRequestDto {
    private String login;
    private String password;
}
