package by.softarex.internship.task.questionnaireportalsystem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class QuestionnaireResponseDto {
    private List<FieldResponseDto> responses;
}
