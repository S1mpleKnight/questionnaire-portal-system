package by.softarex.internship.task.questionnaireportalsystem.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ChangePasswordDto {
    private String oldPassword;
    private String newPassword;
}
