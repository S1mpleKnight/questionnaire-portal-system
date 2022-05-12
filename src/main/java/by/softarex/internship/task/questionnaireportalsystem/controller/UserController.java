package by.softarex.internship.task.questionnaireportalsystem.controller;

import by.softarex.internship.task.questionnaireportalsystem.dto.ChangePasswordDto;
import by.softarex.internship.task.questionnaireportalsystem.dto.UserUpdateDto;
import by.softarex.internship.task.questionnaireportalsystem.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.security.Principal;

@Tag(name = "User controller", description = "Process update password & profile requests")
@Controller
@CrossOrigin(origins = "http://localhost:3000")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "User data", description = "Get user profile info")
    @GetMapping("/api/profile")
    public ResponseEntity<UserUpdateDto> getData(Principal principal) {
        UserUpdateDto userUpdateDto = userService.findByPrincipal(principal);
        return ResponseEntity.ok(userUpdateDto);
    }

    @Operation(summary = "Update user", description = "Update user profile info")
    @PutMapping("/api/profile")
    public ResponseEntity<UserUpdateDto> updateUser(
            @Valid @RequestBody @Parameter(description = "New user data") UserUpdateDto updateDto,
            Principal principal) {
        return ResponseEntity.ok(userService.update(principal, updateDto));
    }

    @Operation(summary = "Update password")
    @PutMapping("/api/password")
    public ResponseEntity<Boolean> updatePassword(
            @Valid @RequestBody @Parameter(description = "New & old passwords") ChangePasswordDto changeDto,
            Principal principal) {
        return ResponseEntity.ok(userService.updatePassword(principal, changeDto));
    }
}
