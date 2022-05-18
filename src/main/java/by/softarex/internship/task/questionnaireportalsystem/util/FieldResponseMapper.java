package by.softarex.internship.task.questionnaireportalsystem.util;

import by.softarex.internship.task.questionnaireportalsystem.dto.FieldResponseDto;
import by.softarex.internship.task.questionnaireportalsystem.entity.FieldResponse;
import org.springframework.stereotype.Component;

@Component
public class FieldResponseMapper {
    public FieldResponseDto toResponseDto(FieldResponse fieldResponse) {
        FieldResponseDto fieldResponseDto = new FieldResponseDto();
        fieldResponseDto.setValue(fieldResponse.getValue());
        fieldResponseDto.setPosition(fieldResponse.getPosition() + 1);
        return fieldResponseDto;
    }

    public FieldResponse toResponseEntity(FieldResponseDto fieldResponseDto) {
        FieldResponse fieldResponse = new FieldResponse();
        fieldResponse.setPosition(fieldResponseDto.getPosition() - 1);
        fieldResponse.setValue(fieldResponseDto.getValue());
        return fieldResponse;
    }
}
