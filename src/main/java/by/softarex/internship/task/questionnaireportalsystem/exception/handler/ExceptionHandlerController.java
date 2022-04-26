package by.softarex.internship.task.questionnaireportalsystem.exception.handler;

import by.softarex.internship.task.questionnaireportalsystem.exception.EmailExistException;
import by.softarex.internship.task.questionnaireportalsystem.exception.FieldNotExistException;
import by.softarex.internship.task.questionnaireportalsystem.exception.InvalidPasswordException;
import by.softarex.internship.task.questionnaireportalsystem.exception.JwtAuthenticationException;
import by.softarex.internship.task.questionnaireportalsystem.exception.QuestionnaireNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;


@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler({EmailExistException.class, InvalidPasswordException.class})
    public ResponseEntity<String> badRequest(RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<String> badRequest(BindException e) {
        return ResponseEntity.badRequest().body(takeErrorFields(e));
    }

    @ExceptionHandler({FieldNotExistException.class, QuestionnaireNotExistException.class})
    public ResponseEntity<String> notFound(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler({JwtAuthenticationException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String unauthorized(AuthenticationException exception) {
        return exception.getMessage();
    }

    private String takeErrorFields(BindException e) {
        StringBuilder stringBuilder = new StringBuilder("Invalid data in:\n");
        for (String fieldError : getDistinctErrorFields(e)) {
            stringBuilder.append("Field - ").append(fieldError).append("\n");
        }
        return stringBuilder.toString();
    }

    private List<String> getDistinctErrorFields(BindException e) {
        return e.getFieldErrors()
                .stream()
                .map(FieldError::getField)
                .distinct()
                .collect(Collectors.toList());
    }
}
