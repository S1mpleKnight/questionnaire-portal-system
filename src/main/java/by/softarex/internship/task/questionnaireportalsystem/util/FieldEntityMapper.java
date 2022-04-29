package by.softarex.internship.task.questionnaireportalsystem.util;

import by.softarex.internship.task.questionnaireportalsystem.dto.FieldDto;
import by.softarex.internship.task.questionnaireportalsystem.dto.QuestionnaireResponseDto;
import by.softarex.internship.task.questionnaireportalsystem.dto.UserDto;
import by.softarex.internship.task.questionnaireportalsystem.dto.UserUpdateDto;
import by.softarex.internship.task.questionnaireportalsystem.entity.Field;
import by.softarex.internship.task.questionnaireportalsystem.entity.FieldOption;
import by.softarex.internship.task.questionnaireportalsystem.entity.FieldType;
import by.softarex.internship.task.questionnaireportalsystem.entity.QuestionnaireResponse;
import by.softarex.internship.task.questionnaireportalsystem.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FieldEntityMapper {
    private final static String FIELD_OPTIONS_DELIMITER = "~!@#%&_&%#@!~";

    public FieldDto toFieldDto(Field field) {
        FieldDto fieldDto = new FieldDto();
        fieldDto.setFieldType(field.getFieldType().toString());
        fieldDto.setActive(field.isActive());
        fieldDto.setLabel(field.getLabel());
        fieldDto.setRequired(field.isRequired());
        if (field.getFieldType() == FieldType.COMBOBOX || field.getFieldType() == FieldType.RADIO_BUTTON) {
            fieldDto.setFieldOptions(field.getOptions()
                    .stream()
                    .map(FieldOption::getValue)
                    .collect(Collectors.joining(FIELD_OPTIONS_DELIMITER)));
        }
        return fieldDto;
    }

    public Field toFieldEntity(FieldDto fieldDto) {
        Field field = new Field();
        field.setActive(fieldDto.isActive());
        field.setRequired(fieldDto.isRequired());
        field.setLabel(fieldDto.getLabel());
        field.setFieldType(FieldType.valueOf(fieldDto.getFieldType()));
        if (field.getFieldType() == FieldType.COMBOBOX || field.getFieldType() == FieldType.RADIO_BUTTON) {
            field.setOptions(Arrays.stream(fieldDto.getFieldOptions()
                    .split(FIELD_OPTIONS_DELIMITER))
                    .map(FieldOption::new)
                    .collect(Collectors.toSet()));
        }
        return field;
    }
}
