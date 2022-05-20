package by.softarex.internship.task.questionnaireportalsystem.service;

import by.softarex.internship.task.questionnaireportalsystem.dto.ChangePasswordDto;
import by.softarex.internship.task.questionnaireportalsystem.dto.UserDataDto;
import by.softarex.internship.task.questionnaireportalsystem.dto.UserDto;
import by.softarex.internship.task.questionnaireportalsystem.entity.User;
import by.softarex.internship.task.questionnaireportalsystem.exception.EmailExistException;
import by.softarex.internship.task.questionnaireportalsystem.exception.InvalidPasswordException;
import by.softarex.internship.task.questionnaireportalsystem.repository.UserRepository;
import by.softarex.internship.task.questionnaireportalsystem.util.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
@Scope("singleton")
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final static String PASSWORD_MAIL_MESSAGE = "Password has been changed";
    private final static String PASSWORD_MAIL_SUBJECT = "Security notification";
    private final static String REGISTRATION_MAIL_MESSAGE = "You have been registered in the questionnaire portal system";
    private final static String REGISTRATION_MAIL_SUBJECT = "Portal registration";
    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    public UserDataDto findByPrincipal(Principal principal) {
        return mapper.toUserDto(userRepository.findByEmail(principal.getName()).get());
    }

    public String findIdByEmail(Principal principal) {
        return userRepository.findByEmail(principal.getName()).get().getId().toString();
    }

    @Transactional
    public UserDataDto save(UserDto userDto) {
        User user = mapper.toUserEntity(userDto);
        if (!isNotUserExist(user.getEmail())) {
            userRepository.save(user);
        }
        mailService.send(user.getEmail(), REGISTRATION_MAIL_SUBJECT, REGISTRATION_MAIL_MESSAGE);
        return mapper.toUserDto(user);
    }

    @Transactional
    public UserDataDto update(Principal principal, UserDataDto userDto) {
        User oldData = userRepository.findByEmail(principal.getName()).get();
        if (userDto.getEmail().equals(principal.getName()) || !isNotUserExist(userDto.getEmail())) {
            updateUserData(userDto, oldData);
            userRepository.save(oldData);
        }
        return mapper.toUserDto(oldData);
    }

    @Transactional
    public Boolean updatePassword(Principal principal, ChangePasswordDto passwordDto) {
        User user = userRepository.findByEmail(principal.getName()).get();
        if (passwordEncoder.matches(passwordDto.getOldPassword(), user.getPasswordHash())) {
            changePassword(passwordDto, user);
            return true;
        } else {
            throw new InvalidPasswordException();
        }
    }

    public UUID findIdByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User with email: " + email + " does not exist");
        }
        return user.get().getId();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User with email: " + username + " does not exist");
        }
        return new org.springframework.security.core.userdetails.User(
                user.get().getEmail(),
                user.get().getPasswordHash(),
                Collections.emptyList());
    }

    private void updateUserData(UserDataDto userDto, User oldData) {
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
