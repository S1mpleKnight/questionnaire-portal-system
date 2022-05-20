package by.softarex.internship.task.questionnaireportalsystem.controller;

import by.softarex.internship.task.questionnaireportalsystem.dto.FieldDto;
import by.softarex.internship.task.questionnaireportalsystem.service.FieldService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

@Tag(name = "Questionnaire controller", description = "Process questionnaire requests")
@Controller
@RequestMapping("/api/questionnaires")
@RequiredArgsConstructor
public class QuestionnaireController {
    private final FieldService fieldService;

    @Operation(summary = "Get questionnaire", description = "Get all questionnaire fields")
    @GetMapping("/{id}")
    public ResponseEntity<List<FieldDto>> getFields(@Parameter(name = "User id") @PathVariable UUID id) {
        return ResponseEntity.ok(fieldService.findAllByUserId(id));
    }
}
