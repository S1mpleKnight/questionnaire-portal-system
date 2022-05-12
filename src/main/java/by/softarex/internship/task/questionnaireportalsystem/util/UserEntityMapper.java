package by.softarex.internship.task.questionnaireportalsystem.util;

import by.softarex.internship.task.questionnaireportalsystem.dto.UserDto;
import by.softarex.internship.task.questionnaireportalsystem.dto.UserDataDto;
import by.softarex.internship.task.questionnaireportalsystem.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserEntityMapper {
    private PasswordEncoder passwordEncoder;

    public UserDataDto toUserDto(User user) {
        UserDataDto userDataDto = new UserDataDto();
        userDataDto.setEmail(user.getEmail());
        userDataDto.setFirstname(user.getFirstname());
        userDataDto.setLastname(user.getLastname());
        userDataDto.setPhone(user.getPhone());
        return userDataDto;
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
