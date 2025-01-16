package com.fawry.MoviesApp.service;

import com.fawry.MoviesApp.dto.UserRegisterDto;
import com.fawry.MoviesApp.dto.UserRegisterResponse;
import com.fawry.MoviesApp.jwt.JwtService;
import com.fawry.MoviesApp.listener.UserRegisterEvent;
import com.fawry.MoviesApp.mapper.UserMapper;
import com.fawry.MoviesApp.model.User;
import com.fawry.MoviesApp.repository.UserRepository;
import com.fawry.MoviesApp.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserUtils userUtils;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public UserRegisterResponse userRegister(UserRegisterDto userRegisterDto) {
        User user = userMapper.mapToUser(userRegisterDto);
        user.setType("member");
        userUtils.userBuilder(user);
        User savedUser = userRepository.save(user);
        eventPublisher.publishEvent(new UserRegisterEvent(savedUser));
        return userMapper.mapToUserRegisterResponse(savedUser);
    }

    @Transactional
    public UserRegisterResponse createAdmin(UserRegisterDto userRegisterDto) {
        User user = userMapper.mapToUser(userRegisterDto);
        user.setType("admin");
        userUtils.userBuilder(user);
        return userMapper.mapToUserRegisterResponse(userRepository.save(user));
    }

}
