package by.softarex.internship.task.questionnaireportalsystem.exception;

public class FieldNotExistException extends RuntimeException{
    public FieldNotExistException(Integer position) {
        super("Field does not exist: " + position);
    }
}
