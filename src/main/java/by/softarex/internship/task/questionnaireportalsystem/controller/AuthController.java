package by.softarex.internship.task.questionnaireportalsystem.controller;

import by.softarex.internship.task.questionnaireportalsystem.dto.AuthenticationRequestDto;
import by.softarex.internship.task.questionnaireportalsystem.dto.UserDto;
import by.softarex.internship.task.questionnaireportalsystem.dto.UserUpdateDto;
import by.softarex.internship.task.questionnaireportalsystem.service.JwtAuthenticationService;
import by.softarex.internship.task.questionnaireportalsystem.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.Map;

@Controller
@AllArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JwtAuthenticationService jwtService;

    @PostMapping("/register")
    public ResponseEntity<UserUpdateDto> register(@Valid @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.save(userDto));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<Object, Object>> login(@Valid @RequestBody AuthenticationRequestDto authDto) {
        Map<Object, Object> result = jwtService.authenticate(authDto);
        return ResponseEntity.ok(result);
    }
}
