package by.softarex.internship.task.questionnaireportalsystem.service;

import by.softarex.internship.task.questionnaireportalsystem.dto.ChangePasswordDto;
import by.softarex.internship.task.questionnaireportalsystem.dto.UserDto;
import by.softarex.internship.task.questionnaireportalsystem.dto.UserUpdateDto;
import by.softarex.internship.task.questionnaireportalsystem.entity.User;
import by.softarex.internship.task.questionnaireportalsystem.exception.EmailExistException;
import by.softarex.internship.task.questionnaireportalsystem.exception.InvalidPasswordException;
import by.softarex.internship.task.questionnaireportalsystem.repository.UserRepository;
import by.softarex.internship.task.questionnaireportalsystem.util.EntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Collections;
import java.util.UUID;

@Service
@Scope("singleton")
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final static String MAIL_MESSAGE = "Password has been changed";
    private final static String MAIL_SUBJECT = "Security notification";
    private UserRepository userRepository;
    private EntityMapper mapper;
    private PasswordEncoder passwordEncoder;
    private MailService mailService;

    public UserUpdateDto findByPrincipal(Principal principal) {
        return mapper.mapToUserDto(userRepository.findByEmail(principal.getName()));
    }

    public void save(UserDto userDto) {
        User user = mapper.mapToUserEntity(userDto);
        if (isUserExist(user.getEmail())) {
            throw new EmailExistException(user.getEmail());
        } else {
            userRepository.save(user);
        }
    }

    public void update(Principal principal, UserUpdateDto userDto) {
        if (isUserExist(userDto.getEmail())) {
            User oldData = userRepository.findByEmail(principal.getName());
            updateUserData(userDto, oldData);
            userRepository.save(oldData);
        }
    }

    public void changePassword(UUID currentUserId, ChangePasswordDto changePasswordDto) {
        User user = userRepository.findById(currentUserId).get();
        String receivedPasswordValue = passwordEncoder.encode(changePasswordDto.getOldPassword());
        if (user.getPasswordHash().equals(receivedPasswordValue)) {
            user.setPasswordHash(passwordEncoder.encode(changePasswordDto.getNewPassword()));
            userRepository.save(user);
            mailService.send(user.getEmail(), MAIL_SUBJECT, MAIL_MESSAGE);
        }
        throw new InvalidPasswordException();
    }

    public UUID findIdByEmail(String email) {
        if (!userRepository.existsByEmail(email)) {
            throw new UsernameNotFoundException("User with email: " + email + " does not exist");
        }
        return userRepository.findByEmail(email).getId();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!userRepository.existsByEmail(username)) {
            throw new UsernameNotFoundException("User with email: " + username + " does not exist");
        }
        User user = userRepository.findByEmail(username);
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPasswordHash(),
                Collections.emptyList());
    }

    private void updateUserData(UserUpdateDto userDto, User oldData) {
        oldData.setEmail(userDto.getEmail());
        oldData.setFirstname(userDto.getFirstname());
        oldData.setLastname(userDto.getLastname());
        oldData.setPhone(userDto.getPhone());
    }

    private boolean isUserExist(String email) {
        return isEmailExist(email);
    }

    private boolean isEmailExist(String email) {
        return userRepository.existsByEmail(email);
    }
}
