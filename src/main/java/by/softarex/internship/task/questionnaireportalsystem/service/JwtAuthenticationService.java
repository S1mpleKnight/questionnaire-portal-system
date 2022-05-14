package by.softarex.internship.task.questionnaireportalsystem.service;

import by.softarex.internship.task.questionnaireportalsystem.dto.AuthenticationRequestDto;
import by.softarex.internship.task.questionnaireportalsystem.dto.AuthenticationTokenDto;
import by.softarex.internship.task.questionnaireportalsystem.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtAuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;

    public AuthenticationTokenDto authenticate(AuthenticationRequestDto request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        if (authentication == null) {
            throw new BadCredentialsException("Invalid credentials");
        }
        return new AuthenticationTokenDto(createToken(request));
    }

    private String createToken(AuthenticationRequestDto request) {
        UUID id = userService.findIdByEmail(request.getEmail());
        return tokenProvider.createToken(request.getEmail(), id.toString());
    }
}
