package by.softarex.internship.task.questionnaireportalsystem.exception.handler;

import by.softarex.internship.task.questionnaireportalsystem.exception.JwtAuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RestAuthFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException ex) throws IOException {
        String message = HttpStatus.UNAUTHORIZED.getReasonPhrase();

        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        if (ex instanceof JwtAuthenticationException) {
            message = ex.getMessage();
        }
        if (ex instanceof BadCredentialsException) {
            message = ex.getMessage();
        }

        response.getOutputStream().println(message);
    }
}
