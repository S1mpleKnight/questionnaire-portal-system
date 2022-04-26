package by.softarex.internship.task.questionnaireportalsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class AuthenticationRequestDto {
    private String email;
    private String password;
}