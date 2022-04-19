package by.softarex.internship.task.questionnaireportalsystem.exception;

public class LoginExistException extends RuntimeException{
    public LoginExistException(String login) {
        super("A user with such login already exists: " + login);
    }
}
