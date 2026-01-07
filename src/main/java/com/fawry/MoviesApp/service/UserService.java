package com.fawry.MoviesApp.service;

import com.fawry.MoviesApp.dto.UserRegisterDto;
import com.fawry.MoviesApp.dto.UserRegisterResponse;
import com.fawry.MoviesApp.mapper.UserMapper;
import com.fawry.MoviesApp.model.User;
import com.fawry.MoviesApp.repository.UserRepository;
import com.fawry.MoviesApp.utils.UserUtils;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserUtils userUtils;
    private final ApplicationEventPublisher eventPublisher;
    private final EmailService2 emailService2;

    @Transactional(rollbackFor = Exception.class)
    public UserRegisterResponse userRegister(UserRegisterDto userRegisterDto) throws MessagingException, TemplateException, IOException {
        User user = userMapper.mapToUser(userRegisterDto);
        user.setType("member");
        userUtils.userBuilder(user);
        User savedUser = userRepository.save(user);
        emailService2.sendEmail();
//        eventPublisher.publishEvent(new UserRegisterEvent(savedUser));
        return userMapper.mapToUserRegisterResponse(savedUser);
    }

    @Transactional(rollbackFor = Exception.class)
    public UserRegisterResponse createAdmin(UserRegisterDto userRegisterDto) {
        User user = userMapper.mapToUser(userRegisterDto);
        user.setType("admin");
        userUtils.userBuilder(user);
        return userMapper.mapToUserRegisterResponse(userRepository.save(user));
    }

}
