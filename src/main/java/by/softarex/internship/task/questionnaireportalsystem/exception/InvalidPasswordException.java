package by.softarex.internship.task.questionnaireportalsystem.exception;

public class InvalidPasswordException extends RuntimeException{
    public InvalidPasswordException() {
        super("Invalid password");
    }
}
