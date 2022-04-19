package by.softarex.internship.task.questionnaireportalsystem.exception;

public class EmailExistException extends RuntimeException{
    public EmailExistException(String email) {
        super("A user with such email already exists: " + email);
    }
}
