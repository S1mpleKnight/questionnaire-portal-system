package by.softarex.internship.task.questionnaireportalsystem.exception;

public class QuestionnaireNotExistException extends RuntimeException{
    public QuestionnaireNotExistException() {
        super("Questionnaire does not exist");
    }
}
