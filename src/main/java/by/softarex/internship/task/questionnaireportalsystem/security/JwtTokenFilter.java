package by.softarex.internship.task.questionnaireportalsystem.security;

import by.softarex.internship.task.questionnaireportalsystem.exception.JwtAuthenticationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    private final JwtTokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String token = tokenProvider.resolveToken(httpServletRequest);
        try {
            acceptAuthentication(token);
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } catch (JwtAuthenticationException e) {
            rejectAuthentication(e);
        }
    }

    private void rejectAuthentication(JwtAuthenticationException e) {
        log.error(e.getMessage());
        SecurityContextHolder.clearContext();
        throw new JwtAuthenticationException("JWT token filter have not been passed");
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
