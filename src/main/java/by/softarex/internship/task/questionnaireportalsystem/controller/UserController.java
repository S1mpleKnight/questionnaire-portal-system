package by.softarex.internship.task.questionnaireportalsystem.controller;

import by.softarex.internship.task.questionnaireportalsystem.dto.ChangePasswordDto;
import by.softarex.internship.task.questionnaireportalsystem.dto.UserUpdateDto;
import by.softarex.internship.task.questionnaireportalsystem.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserUpdateDto> getData(Principal principal) {
        UserUpdateDto userUpdateDto = userService.findByPrincipal(principal);
        return ResponseEntity.ok(userUpdateDto);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserUpdateDto> updateUser(@Valid @RequestBody UserUpdateDto updateDto, Principal principal) {
        return ResponseEntity.ok(userService.update(principal, updateDto));
    }

    @PutMapping("/password")
    public ResponseEntity<Boolean> updatePassword(@Valid @RequestBody ChangePasswordDto changeDto, Principal principal) {
        return ResponseEntity.ok(userService.updatePassword(principal, changeDto));
    }
}
