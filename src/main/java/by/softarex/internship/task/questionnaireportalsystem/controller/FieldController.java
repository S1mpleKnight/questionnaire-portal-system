package by.softarex.internship.task.questionnaireportalsystem.controller;

import by.softarex.internship.task.questionnaireportalsystem.dto.FieldDto;
import by.softarex.internship.task.questionnaireportalsystem.service.FieldService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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
        log.info("Get all fields");
        if (page != null && size != null) {
            return ResponseEntity.ok(fieldService.findAllByUserEmail(principal, PageRequest.of(page, size)));
        } else {
            return ResponseEntity.ok(fieldService.findAllByUserEmail(principal));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<FieldDto> getField(Principal principal, @PathVariable Integer id) {
        log.info("Get field with id: " + id);
        return ResponseEntity.ok(fieldService.getFieldDto(principal, id));
    }

    @PostMapping
    public ResponseEntity<String> create(@Valid @RequestBody FieldDto fieldDto, Principal principal) {
        log.info("Create field: " + fieldDto);
        fieldService.save(principal, fieldDto);
        return ResponseEntity.ok("Field was successfully created");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id, Principal principal) {
        log.info("Delete field with pos: " + id);
        fieldService.delete(principal, id);
        return ResponseEntity.ok("Field was successfully deleted");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(
            @PathVariable Integer id,
            @Valid @RequestBody FieldDto fieldDto,
            Principal principal) {
        log.info("Update field with pos: " + id);
        fieldService.update(principal, id, fieldDto);
        return ResponseEntity.ok("Field was successfully updated");
    }
}
