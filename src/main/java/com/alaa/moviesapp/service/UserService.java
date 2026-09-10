package com.alaa.MoviesApp.service;

import com.alaa.MoviesApp.dto.AppResponse;
import com.alaa.MoviesApp.dto.UserRegisterDto;
import com.alaa.MoviesApp.dto.UserRegisterResponse;
import com.alaa.MoviesApp.enums.EntityString;
import com.alaa.MoviesApp.enums.ErrorCode;
import com.alaa.MoviesApp.enums.UserTypes;
import com.alaa.MoviesApp.exception.BusinessException;
import com.alaa.MoviesApp.listener.UserRegisterEvent;
import com.alaa.MoviesApp.mapper.ModelMapper;
import com.alaa.MoviesApp.model.User;
import com.alaa.MoviesApp.repository.UserRepository;
import com.alaa.MoviesApp.utils.AppResponseBuilder;
import com.alaa.MoviesApp.utils.SystemUtils;
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
    private final RoleService roleService;
    private final UserRepository userRepository;
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
        UserRegisterResponse response = modelMapper.mapToUserRegisterResponse(savedUser);
        return AppResponseBuilder.success(response, HttpStatus.CREATED,
                "entity.created.success", EntityString.USER.getName());
    }

    @Transactional(rollbackFor = Exception.class)
    public AppResponse<UserRegisterResponse> createAdmin(UserRegisterDto userRegisterDto) throws JacksonException {
        User user = modelMapper.map(userRegisterDto, User.class);
        user.setType(UserTypes.ADMIN.getType());
        this.userBuilder(user);
        UserRegisterResponse response = modelMapper.mapToUserRegisterResponse(userRepository.save(user));
        return AppResponseBuilder.success(response, HttpStatus.CREATED,
                "entity.created.success", EntityString.USER.getName());    }


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
                ()-> new BusinessException(ErrorCode.USER_NOT_FOUND)
        );
    }

}
