package by.softarex.internship.task.questionnaireportalsystem.controller;

import by.softarex.internship.task.questionnaireportalsystem.dto.AuthenticationRequestDto;
import by.softarex.internship.task.questionnaireportalsystem.dto.UserDto;
import by.softarex.internship.task.questionnaireportalsystem.service.JwtAuthenticationService;
import by.softarex.internship.task.questionnaireportalsystem.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.Map;


@Slf4j
@Controller
@AllArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JwtAuthenticationService jwtService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserDto userDto) {
        log.info("Registration: " + userDto);
        userService.save(userDto);
        return ResponseEntity.ok("You are successfully registered");
    }

    @PostMapping("/login")
    public ResponseEntity<Map<Object, Object>> login(@Valid @RequestBody AuthenticationRequestDto authDto) {
        log.info("Auth: " + authDto);
        Map<Object, Object> result = jwtService.authenticate(authDto);
        return ResponseEntity.ok(result);
    }
}
