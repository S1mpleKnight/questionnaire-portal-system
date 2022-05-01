package by.softarex.internship.task.questionnaireportalsystem.controller;

import by.softarex.internship.task.questionnaireportalsystem.dto.FieldDto;
import by.softarex.internship.task.questionnaireportalsystem.service.FieldService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Field controller", description = "Process CRUD field requests")
@Controller
@AllArgsConstructor
@RequestMapping("/api/fields")
public class FieldController {
    private final FieldService fieldService;

    @Operation(summary = "All fields", description = "Get all fields to the current questionnaire")
    @GetMapping
    public ResponseEntity<?> getAllFields(
            Principal principal,
            @RequestParam(name = "page", required = false)
            @Parameter (description = "The number of the page") Integer page,
            @RequestParam(name = "size", required = false) @Parameter(description = "The size of the page") Integer size
    ) {
        if (page != null && size != null) {
            return ResponseEntity.ok(fieldService.findAllByUserEmail(principal, PageRequest.of(page, size)));
        } else {
            return ResponseEntity.ok(fieldService.findAllByUserEmail(principal));
        }
    }

    @Operation(summary = "Get field", description = "Get the field by the position")
    @GetMapping("/{id}")
    public ResponseEntity<FieldDto> getField(
            Principal principal,
            @PathVariable @Parameter(description = "Field position in questionnaire") Integer id) {
        return ResponseEntity.ok(fieldService.getFieldDto(principal, id));
    }

    @Operation(summary = "Create field", description = "Create new field in the questionnaire")
    @PostMapping
    public ResponseEntity<FieldDto> create(
            @Valid @RequestBody @Parameter(description = "Field creation data") FieldDto fieldDto,
            Principal principal) {
        return ResponseEntity.ok(fieldService.save(principal, fieldDto));
    }

    @Operation(summary = "Delete field", description = "Delete the field with given position")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable @Parameter(description = "Field position in questionnaire") Integer id,
            Principal principal) {
        fieldService.delete(principal, id);
        return ResponseEntity.ok("Field was successfully deleted");
    }

    @Operation(summary = "Update field", description = "Update the field with given position")
    @PutMapping("/{id}")
    public ResponseEntity<FieldDto> update(
            @PathVariable @Parameter(description = "Field position in questionnaire") Integer id,
            @Valid @RequestBody @Parameter(description = "New field data") FieldDto fieldDto,
            Principal principal) {
        return ResponseEntity.ok(fieldService.update(principal, id, fieldDto));
    }
}
