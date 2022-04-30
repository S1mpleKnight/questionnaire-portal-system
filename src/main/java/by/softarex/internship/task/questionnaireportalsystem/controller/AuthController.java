package by.softarex.internship.task.questionnaireportalsystem.controller;

import by.softarex.internship.task.questionnaireportalsystem.dto.AuthenticationRequestDto;
import by.softarex.internship.task.questionnaireportalsystem.dto.UserDto;
import by.softarex.internship.task.questionnaireportalsystem.dto.UserUpdateDto;
import by.softarex.internship.task.questionnaireportalsystem.service.JwtAuthenticationService;
import by.softarex.internship.task.questionnaireportalsystem.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.Map;

@Tag(name = "Authentication Controller", description = "Processes registration and authorization requests")
@Controller
@AllArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JwtAuthenticationService jwtService;

    @Operation(summary = "Registration", description = "Register user via entered data")
    @PostMapping("/api/register")
    public ResponseEntity<UserUpdateDto> register(
            @Valid @RequestBody @Parameter(required = true, description = "User data without password") UserDto userDto) {
        return ResponseEntity.ok(userService.save(userDto));
    }

    @Operation(summary = "Log in", description = "Log in user via email and password")
    @PostMapping("/api/login")
    public ResponseEntity<Map<Object, Object>> login(
            @Valid @RequestBody @Parameter(required = true, description = "Email & password") AuthenticationRequestDto authDto) {
        Map<Object, Object> result = jwtService.authenticate(authDto);
        return ResponseEntity.ok(result);
    }
}
