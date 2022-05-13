package by.softarex.internship.task.questionnaireportalsystem.controller;

import by.softarex.internship.task.questionnaireportalsystem.dto.AuthenticationRequestDto;
import by.softarex.internship.task.questionnaireportalsystem.dto.AuthenticationTokenDto;
import by.softarex.internship.task.questionnaireportalsystem.dto.UserDataDto;
import by.softarex.internship.task.questionnaireportalsystem.dto.UserDto;
import by.softarex.internship.task.questionnaireportalsystem.service.JwtAuthenticationService;
import by.softarex.internship.task.questionnaireportalsystem.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Tag(name = "Authentication Controller", description = "Processes registration and authorization requests")
@Controller
@RequestMapping("/api/")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JwtAuthenticationService jwtService;

    @Operation(summary = "Registration", description = "Register user via entered data")
    @PostMapping("/register")
    public ResponseEntity<UserDataDto> register(
            @Valid @RequestBody @Parameter(required = true, description = "User data without password") UserDto userDto) {
        return ResponseEntity.ok(userService.save(userDto));
    }

    @Operation(summary = "Log in", description = "Log in user via email and password")
    @PostMapping("/login")
    public ResponseEntity<AuthenticationTokenDto> login(
            @Valid @RequestBody @Parameter(required = true, description = "Email & password") AuthenticationRequestDto authDto) {
       AuthenticationTokenDto result = jwtService.authenticate(authDto);
        return ResponseEntity.ok(result);
    }
}
