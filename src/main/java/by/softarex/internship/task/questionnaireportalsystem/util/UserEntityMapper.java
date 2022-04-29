package by.softarex.internship.task.questionnaireportalsystem.util;

import by.softarex.internship.task.questionnaireportalsystem.dto.UserDto;
import by.softarex.internship.task.questionnaireportalsystem.dto.UserUpdateDto;
import by.softarex.internship.task.questionnaireportalsystem.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserEntityMapper {
    private PasswordEncoder passwordEncoder;

    public UserUpdateDto toUserDto(User user) {
        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setEmail(user.getEmail());
        userUpdateDto.setFirstname(user.getFirstname());
        userUpdateDto.setLastname(user.getLastname());
        userUpdateDto.setPhone(user.getPhone());
        return userUpdateDto;
    }

    public User toUserEntity(UserDto userDto) {
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
}
