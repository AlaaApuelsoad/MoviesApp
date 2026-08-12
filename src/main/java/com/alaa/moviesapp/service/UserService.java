package com.alaa.moviesapp.service;

import com.alaa.moviesapp.dto.AppResponse;
import com.alaa.moviesapp.dto.UserRegisterDto;
import com.alaa.moviesapp.dto.UserRegisterResponse;
import com.alaa.moviesapp.enums.ErrorCode;
import com.alaa.moviesapp.enums.UserTypes;
import com.alaa.moviesapp.exception.BusinessException;
import com.alaa.moviesapp.listener.UserRegisterEvent;
import com.alaa.moviesapp.mapper.ModelMapper;
import com.alaa.moviesapp.model.User;
import com.alaa.moviesapp.repository.UserRepository;
import com.alaa.moviesapp.utils.AppResponseBuilder;
import com.alaa.moviesapp.utils.SystemUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.core.JacksonException;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class UserService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final MessageService messageService;
    private final RoleService roleService;
    private final ApplicationEventPublisher eventPublisher;
    private final BCryptPasswordEncoder bcryptPasswordEncoder;
    private final SystemPropertyService systemPropertyService;


    @Transactional(rollbackFor = Exception.class)
    public AppResponse<UserRegisterResponse> userRegister(UserRegisterDto userRegisterDto) throws JacksonException {
        User user = modelMapper.map(userRegisterDto, User.class);
        user.setType(UserTypes.MEMBER.getType());
        this.userBuilder(user);
        User savedUser = userRepository.save(user);
        eventPublisher.publishEvent(new UserRegisterEvent(savedUser));
        return AppResponseBuilder.buildResponse(true,modelMapper.mapToUserRegisterResponse(savedUser),
                "user.created.success", HttpStatus.OK,null,null);
    }

    @Transactional(rollbackFor = Exception.class)
    public AppResponse<UserRegisterResponse> createAdmin(UserRegisterDto userRegisterDto) throws JacksonException {
        User user = modelMapper.map(userRegisterDto, User.class);
        user.setType(UserTypes.ADMIN.getType());
        this.userBuilder(user);
        return AppResponseBuilder.buildResponse(true,modelMapper.mapToUserRegisterResponse(userRepository.save(user)),
                messageService.getMessage("user.created.success"),HttpStatus.OK,null,null);
    }


    public void userBuilder(User user) {
        String saltPassword = SystemUtils.generateUUIDCode();
        String verificationCode = SystemUtils.generateUUIDCode();

        if (Objects.equals(user.getType(), UserTypes.ADMIN.getType())) {
            user.setRole(roleService.getAdminRoleReference());
            user.setIsVerified(Boolean.TRUE);
            user.setVerificationCode(null);
            user.setVerificationCodeExpiryDate(null);
        }
        if (Objects.equals(user.getType(), UserTypes.MEMBER.getType())) {
            user.setRole(roleService.getMemberRoleReference());
            user.setVerificationCode(verificationCode);
            user.setIsVerified(Boolean.FALSE);
            user.setVerificationCodeExpiryDate(Instant.now()
                    .plus(Duration.ofSeconds
                            (systemPropertyService.getIntegerProperty("app.verification.code.expiration"))));
        }

        user.setSaltPassword(saltPassword);
        user.setPassword(bcryptPasswordEncoder.encode(Objects.requireNonNull(user.getPassword()).concat(saltPassword)));
    }

    public User getUser(String userIdentifier){
        return userRepository.findByUsernameOrEmail(userIdentifier).orElseThrow(
                ()-> new BusinessException(ErrorCode.INVALID_CREDENTIALS)
        );
    }

}
