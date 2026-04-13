package com.alaa.MoviesApp.service;

import com.alaa.MoviesApp.dto.AppResponse;
import com.alaa.MoviesApp.dto.UserRegisterDto;
import com.alaa.MoviesApp.dto.UserRegisterResponse;
import com.alaa.MoviesApp.enums.ErrorCode;
import com.alaa.MoviesApp.exception.BusinessException;
import com.alaa.MoviesApp.listener.UserRegisterEvent;
import com.alaa.MoviesApp.mapper.ModelMapper;
import com.alaa.MoviesApp.model.User;
import com.alaa.MoviesApp.repository.UserRepository;
import com.alaa.MoviesApp.utils.AppResponseBuilder;
import com.alaa.MoviesApp.utils.SystemUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final SystemUtils systemUtils;
    private final BCryptPasswordEncoder BCryptPasswordEncoder;


    @Transactional(rollbackFor = Exception.class)
    public AppResponse<UserRegisterResponse> userRegister(UserRegisterDto userRegisterDto) throws JsonProcessingException {
        User user = modelMapper.map(userRegisterDto, User.class);
        user.setType("member");
        this.userBuilder(user);
        User savedUser = userRepository.save(user);
        eventPublisher.publishEvent(new UserRegisterEvent(savedUser));
        return AppResponseBuilder.buildResponse(true,modelMapper.mapToUserRegisterResponse(savedUser),
                "User Registered", HttpStatus.OK,null,null);
    }

    @Transactional(rollbackFor = Exception.class)
    public AppResponse<UserRegisterResponse> createAdmin(UserRegisterDto userRegisterDto) throws JsonProcessingException {
        User user = modelMapper.map(userRegisterDto, User.class);
        user.setType("admin");
        this.userBuilder(user);
        return AppResponseBuilder.buildResponse(true,modelMapper.mapToUserRegisterResponse(userRepository.save(user)),
                "Admin user created successfully",HttpStatus.OK,null,null);
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
