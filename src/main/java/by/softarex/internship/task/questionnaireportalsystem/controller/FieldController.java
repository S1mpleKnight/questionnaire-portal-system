package by.softarex.internship.task.questionnaireportalsystem.controller;

import by.softarex.internship.task.questionnaireportalsystem.dto.FieldDto;
import by.softarex.internship.task.questionnaireportalsystem.service.FieldService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@AllArgsConstructor
@RequestMapping("/fields")
public class FieldController {
    private final FieldService fieldService;

    @GetMapping
    public ResponseEntity<?> getAllFields(
            Principal principal,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size) {
        if (page != null && size != null) {
            return ResponseEntity.ok(fieldService.findAllByUserEmail(principal, PageRequest.of(page, size)));
        } else {
            return ResponseEntity.ok(fieldService.findAllByUserEmail(principal));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<FieldDto> getField(Principal principal, @PathVariable Integer id) {
        return ResponseEntity.ok(fieldService.getFieldDto(principal, id));
    }

    @PostMapping
    public ResponseEntity<FieldDto> create(@Valid @RequestBody FieldDto fieldDto, Principal principal) {
        return ResponseEntity.ok(fieldService.save(principal, fieldDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id, Principal principal) {
        fieldService.delete(principal, id);
        return ResponseEntity.ok("Field was successfully deleted");
    }

    @PutMapping("/{id}")
    public ResponseEntity<FieldDto> update(
            @PathVariable Integer id,
            @Valid @RequestBody FieldDto fieldDto,
            Principal principal) {
        return ResponseEntity.ok(fieldService.update(principal, id, fieldDto));
    }
}
