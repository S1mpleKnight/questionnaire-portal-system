package by.softarex.internship.task.questionnaireportalsystem.util;

import by.softarex.internship.task.questionnaireportalsystem.dto.QuestionnaireResponseDto;
import by.softarex.internship.task.questionnaireportalsystem.entity.QuestionnaireResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class QuestionnaireResponseMapper {
    private final FieldResponseMapper fieldResponseMapper;

    public QuestionnaireResponseDto toResponse(QuestionnaireResponse response) {
        QuestionnaireResponseDto responseDto = new QuestionnaireResponseDto();
        responseDto.setResponses(
                response.getConcreteResponses()
                        .stream()
                        .map(fieldResponseMapper::toResponseDto)
                        .collect(Collectors.toList())
        );
        return responseDto;
    }
}
