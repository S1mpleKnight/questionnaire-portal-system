package by.softarex.internship.task.questionnaireportalsystem.controller;

import by.softarex.internship.task.questionnaireportalsystem.dto.FieldResponseDto;
import by.softarex.internship.task.questionnaireportalsystem.service.FieldResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Tag(name = "Questionnaire response controller", description = "Process questionnaire response creation")
@RequiredArgsConstructor
@Controller
@RequestMapping("/api/responses")
public class QuestionnaireResponseController {
    private final FieldResponseService fieldResponseService;

    @Operation(summary = "Create responses", description = "Save all questionnaire responses to the database")
    @PostMapping("/{userId}")
    public ResponseEntity<List<FieldResponseDto>> create(
            @Valid @RequestBody @Parameter(description = "New responses data") List<FieldResponseDto> responses,
            @PathVariable(name = "userId") @Parameter(description = "Questionnaire creator's id") UUID userId
    ) {
        return ResponseEntity.ok(fieldResponseService.saveAll(responses, userId));
    }

    @Operation(summary = "Get responses", description = "Get all responses to the questionnaire")
    @GetMapping
    public ResponseEntity<?> findAll(
            @Parameter(description = "The number & the size of the page") Pageable pageable,
            Principal principal
    ) {
        return ResponseEntity.ok(fieldResponseService.findAllByUserId(principal, pageable));
    }
}


