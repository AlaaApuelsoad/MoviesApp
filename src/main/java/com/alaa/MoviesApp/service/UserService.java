package com.alaa.MoviesApp.service;

import com.alaa.MoviesApp.dto.UserRegisterDto;
import com.alaa.MoviesApp.dto.UserRegisterResponse;
import com.alaa.MoviesApp.enums.ErrorCode;
import com.alaa.MoviesApp.exception.BusinessException;
import com.alaa.MoviesApp.listener.UserRegisterEvent;
import com.alaa.MoviesApp.mapper.UserMapper;
import com.alaa.MoviesApp.model.User;
import com.alaa.MoviesApp.repository.UserRepository;
import com.alaa.MoviesApp.utils.SystemUtils;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final SystemUtils systemUtils;
    private final BCryptPasswordEncoder BCryptPasswordEncoder;


    @Transactional(rollbackFor = Exception.class)
    public UserRegisterResponse userRegister(UserRegisterDto userRegisterDto) throws MessagingException, TemplateException, IOException {
        User user = userMapper.mapToUser(userRegisterDto);
        user.setType("member");
        this.userBuilder(user);
        User savedUser = userRepository.save(user);
        eventPublisher.publishEvent(new UserRegisterEvent(savedUser));
        return userMapper.mapToUserRegisterResponse(savedUser);
    }

    @Transactional(rollbackFor = Exception.class)
    public UserRegisterResponse createAdmin(UserRegisterDto userRegisterDto) {
        User user = userMapper.mapToUser(userRegisterDto);
        user.setType("admin");
        this.userBuilder(user);
        return userMapper.mapToUserRegisterResponse(userRepository.save(user));
    }


    public void userBuilder(User user) {
        String saltPassword = SystemUtils.generateUUIDCode();
        String verificationCode = SystemUtils.generateUUIDCode();

        if (user.getType().equals("admin")) {
            user.setRole(systemUtils.findRoleByRoleName("ADMIN"));
            user.setVerified(true);
            user.setVerificationCode(null);
            user.setVerificationCodeExpiryDate(null);
        }
        if (user.getType().equals("member")) {
            user.setRole(systemUtils.findRoleByRoleName("MEMBER"));
            user.setVerificationCode(verificationCode);
            user.setVerified(false);
        }

        user.setSaltPassword(saltPassword);
        user.setPassword(BCryptPasswordEncoder.encode(user.getPassword().concat(saltPassword)));
    }

    public User getUser(String userIdentifier){
        return userRepository.findByUsernameOrEmail(userIdentifier).orElseThrow(
                ()-> new BusinessException(ErrorCode.INVALID_CREDENTIALS)
        );
    }

}
