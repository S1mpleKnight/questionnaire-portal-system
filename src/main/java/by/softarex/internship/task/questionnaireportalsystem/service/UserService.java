package by.softarex.internship.task.questionnaireportalsystem.service;

import by.softarex.internship.task.questionnaireportalsystem.dto.ChangePasswordDto;
import by.softarex.internship.task.questionnaireportalsystem.dto.UserDto;
import by.softarex.internship.task.questionnaireportalsystem.dto.UserUpdateDto;
import by.softarex.internship.task.questionnaireportalsystem.entity.User;
import by.softarex.internship.task.questionnaireportalsystem.exception.EmailExistException;
import by.softarex.internship.task.questionnaireportalsystem.exception.InvalidPasswordException;
import by.softarex.internship.task.questionnaireportalsystem.repository.UserRepository;
import by.softarex.internship.task.questionnaireportalsystem.util.UserEntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Collections;
import java.util.UUID;

@Service
@Scope("singleton")
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final static String PASSWORD_MAIL_MESSAGE = "Password has been changed";
    private final static String PASSWORD_MAIL_SUBJECT = "Security notification";
    private final static String REGISTRATION_MAIL_MESSAGE = "You have been registered in the questionnaire portal system";
    private final static String REGISTRATION_MAIL_SUBJECT = "Portal registration";
    private UserRepository userRepository;
    private UserEntityMapper mapper;
    private PasswordEncoder passwordEncoder;
    private MailService mailService;

    public UserUpdateDto findByPrincipal(Principal principal) {
        return mapper.toUserDto(userRepository.findByEmail(principal.getName()));
    }

    @Transactional
    public UserUpdateDto save(UserDto userDto) {
        User user = mapper.toUserEntity(userDto);
        if (!isNotUserExist(user.getEmail())) {
            userRepository.save(user);
        }
        mailService.send(user.getEmail(), REGISTRATION_MAIL_SUBJECT, REGISTRATION_MAIL_MESSAGE);
        return mapper.toUserDto(user);
    }

    @Transactional
    public UserUpdateDto update(Principal principal, UserUpdateDto userDto) {
        User oldData = userRepository.findByEmail(principal.getName());
        if (userDto.getEmail().equals(principal.getName()) || !isNotUserExist(userDto.getEmail())) {
            updateUserData(userDto, oldData);
            userRepository.save(oldData);
        }
        return mapper.toUserDto(oldData);
    }

    @Transactional
    public Boolean updatePassword(Principal principal, ChangePasswordDto passwordDto) {
        User user = userRepository.findByEmail(principal.getName());
        if (passwordEncoder.matches(passwordDto.getOldPassword(), user.getPasswordHash())) {
            changePassword(passwordDto, user);
            return true;
        } else {
            throw new InvalidPasswordException();
        }
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

    private void changePassword(ChangePasswordDto passwordDto, User user) {
        user.setPasswordHash(passwordEncoder.encode(passwordDto.getNewPassword()));
        userRepository.save(user);
        mailService.send(user.getEmail(), PASSWORD_MAIL_SUBJECT, PASSWORD_MAIL_MESSAGE);
    }

    private boolean isNotUserExist(String email) {
        if (isEmailExist(email)) {
            throw new EmailExistException(email);
        }
        return false;
    }

    private boolean isEmailExist(String email) {
        return userRepository.existsByEmail(email);
    }
}
