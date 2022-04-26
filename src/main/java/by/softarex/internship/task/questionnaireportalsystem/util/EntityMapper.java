package by.softarex.internship.task.questionnaireportalsystem.util;

import by.softarex.internship.task.questionnaireportalsystem.dto.FieldDto;
import by.softarex.internship.task.questionnaireportalsystem.dto.ResponseDto;
import by.softarex.internship.task.questionnaireportalsystem.dto.UserDto;
import by.softarex.internship.task.questionnaireportalsystem.dto.UserUpdateDto;
import by.softarex.internship.task.questionnaireportalsystem.entity.Field;
import by.softarex.internship.task.questionnaireportalsystem.entity.FieldOption;
import by.softarex.internship.task.questionnaireportalsystem.entity.FieldType;
import by.softarex.internship.task.questionnaireportalsystem.entity.Response;
import by.softarex.internship.task.questionnaireportalsystem.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Scope("singleton")
@AllArgsConstructor
public class EntityMapper {
    private final static String FIELD_OPTIONS_DELIMITER = " ,";
    private PasswordEncoder passwordEncoder;

    public FieldDto mapToFieldDto(Field field) {
        FieldDto fieldDto = new FieldDto();
        fieldDto.setFieldType(field.getFieldType().toString());
        fieldDto.setActive(field.isActive());
        fieldDto.setLabel(field.getLabel());
        fieldDto.setRequired(field.isRequired());
        if (field.getFieldType() == FieldType.COMBOBOX || field.getFieldType() == FieldType.RADIO_BUTTON) {
            fieldDto.setFieldOptions(field.getOptions()
                    .stream()
                    .map(FieldOption::toString)
                    .collect(Collectors.joining(FIELD_OPTIONS_DELIMITER)));
        }
        return fieldDto;
    }

    public Field mapToFieldEntity(FieldDto fieldDto) {
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

    public UserUpdateDto mapToUserDto(User user) {
        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setEmail(user.getEmail());
        userUpdateDto.setFirstname(user.getEmail());
        userUpdateDto.setLastname(user.getLastname());
        userUpdateDto.setPhone(user.getPhone());
        return userUpdateDto;
    }

    public User mapToUserEntity(UserDto userDto) {
        User user = createUser(userDto);
        user.setPasswordHash(passwordEncoder.encode(userDto.getPassword()));
        return user;
    }

    private User createUser(UserDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        return user;
    }

    public ResponseDto mapToResponseDto(Response response, List<Field> fields) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setValue(response.getValue());
        responseDto.setFieldPosition(((Integer) fields.lastIndexOf(response.getField())).toString());
        return responseDto;
    }
}
