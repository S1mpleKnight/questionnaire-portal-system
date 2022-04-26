package by.softarex.internship.task.questionnaireportalsystem.controller;

import by.softarex.internship.task.questionnaireportalsystem.dto.UserUpdateDto;
import by.softarex.internship.task.questionnaireportalsystem.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@Slf4j
@AllArgsConstructor
@RequestMapping("/profile")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserUpdateDto> getData(Principal principal) {
        log.info("Get profile data");
        UserUpdateDto userUpdateDto = userService.findByPrincipal(principal);
        return ResponseEntity.ok(userUpdateDto);
    }

    @PutMapping
    public ResponseEntity<String> update(@Valid @RequestBody UserUpdateDto updateDto, Principal principal) {
        log.info("Update user: " + principal.getName() + " with data: " + updateDto);
        userService.update(principal, updateDto);
        return ResponseEntity.ok("Profile was successfully updated");
    }
}
