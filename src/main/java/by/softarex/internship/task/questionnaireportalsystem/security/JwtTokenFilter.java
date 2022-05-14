package by.softarex.internship.task.questionnaireportalsystem.security;

import by.softarex.internship.task.questionnaireportalsystem.exception.JwtAuthenticationException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationFailureHandler failureHandler;

    public JwtTokenFilter(
            JwtTokenProvider tokenProvider,
            @Qualifier(value = "restAuthFailureHandler") AuthenticationFailureHandler failureHandler) {
        this.tokenProvider = tokenProvider;
        this.failureHandler = failureHandler;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String token = tokenProvider.resolveToken(httpServletRequest);
        try {
            acceptAuthentication(token);
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } catch (JwtAuthenticationException e) {
            SecurityContextHolder.clearContext();
            failureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse,e);
        }
    }

    private void acceptAuthentication(String token) {
        if (isTokenValid(token)) {
            Authentication authentication = tokenProvider.getAuthentication(token);
            if (authentication != null) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
    }

    private boolean isTokenValid(String token) {
        return token != null && tokenProvider.validateToken(token);
    }
}
