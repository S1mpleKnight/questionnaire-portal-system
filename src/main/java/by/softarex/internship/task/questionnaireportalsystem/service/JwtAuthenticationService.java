package by.softarex.internship.task.questionnaireportalsystem.service;

import by.softarex.internship.task.questionnaireportalsystem.dto.AuthenticationRequestDto;
import by.softarex.internship.task.questionnaireportalsystem.security.JwtTokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
public class JwtAuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;

    public Object authenticate(AuthenticationRequestDto request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        return fillMap(createToken(request));
    }

    private String createToken(AuthenticationRequestDto request) {
        UUID id = userService.findIdByEmail(request.getEmail());
        return tokenProvider.createToken(request.getEmail(), id.toString());
    }

    private Object fillMap(String token) {
        Map<Object, Object> response = new HashMap<>();
        response.put("token", token);
        return response;
    }
}
