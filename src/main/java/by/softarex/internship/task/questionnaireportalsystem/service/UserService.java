package by.softarex.internship.task.questionnaireportalsystem.service;

import by.softarex.internship.task.questionnaireportalsystem.dto.ChangePasswordDto;
import by.softarex.internship.task.questionnaireportalsystem.dto.UserDto;
import by.softarex.internship.task.questionnaireportalsystem.entity.User;
import by.softarex.internship.task.questionnaireportalsystem.exception.EmailExistException;
import by.softarex.internship.task.questionnaireportalsystem.exception.InvalidPasswordException;
import by.softarex.internship.task.questionnaireportalsystem.exception.LoginExistException;
import by.softarex.internship.task.questionnaireportalsystem.repository.UserRepository;
import by.softarex.internship.task.questionnaireportalsystem.util.EntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Scope("singleton")
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private EntityMapper mapper;
    private PasswordEncoder passwordEncoder;

    public void save(UserDto userDto) {
        User user = mapper.mapToUserEntity(userDto);
        if (isUserExist(user)) {
            userRepository.save(user);
        }
    }

    public void update(UUID currentUserID, UserDto userDto) {
        User newUser = mapper.mapToUserEntity(userDto);
        if (isUserExist(newUser)) {
            User oldUser = userRepository.findById(currentUserID).get();
            newUser.setId(oldUser.getId());
            userRepository.save(newUser);
        }
    }

    public void changePassword(UUID currentUserId, ChangePasswordDto changePasswordDto) {
        User user = userRepository.findById(currentUserId).get();
        String receivedPasswordValue = passwordEncoder.encode(changePasswordDto.getOldPassword());
        if (user.getPassword().equals(receivedPasswordValue)) {
            user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
            userRepository.save(user);
            //todo: email notification
        }
        throw new InvalidPasswordException();
    }

    private boolean isUserExist(User user) {
        if (isLoginExist(user.getLogin())) {
            throw new LoginExistException(user.getLogin());
        }
        if (isEmailExist(user.getEmail())) {
            throw new EmailExistException(user.getEmail());
        }
        return true;
    }

    private boolean isLoginExist(String login) {
        return userRepository.existsByLogin(login);
    }

    private boolean isEmailExist(String email) {
        return userRepository.existsByEmail(email);
    }
}
