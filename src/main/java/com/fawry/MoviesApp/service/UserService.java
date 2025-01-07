package com.fawry.MoviesApp.service;

import com.fawry.MoviesApp.dto.AuthResponse;
import com.fawry.MoviesApp.dto.LoginRequest;
import com.fawry.MoviesApp.dto.UserRegisterDto;
import com.fawry.MoviesApp.enums.VerificationStatus;
import com.fawry.MoviesApp.jwt.JwtService;
import com.fawry.MoviesApp.listener.UserRegisterEvent;
import com.fawry.MoviesApp.mapper.UserMapper;
import com.fawry.MoviesApp.model.Role;
import com.fawry.MoviesApp.model.User;
import com.fawry.MoviesApp.repository.RoleRepository;
import com.fawry.MoviesApp.repository.UserRepository;
import com.fawry.MoviesApp.utils.UserUtils;
import com.fawry.MoviesApp.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final Utils utils;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserUtils userUtils;
    private final ApplicationEventPublisher eventPublisher;
    private final EmailService emailService;

    @Transactional
    public User userRegister(UserRegisterDto userRegisterDto) {
        User user = userMapper.mapToUser(userRegisterDto);
        user.setType("user");
        userUtils.userBuilder(user);
//        emailService.sendAccountVerificationEmail(user.getEmail(),user.getVerificationCode());
        User savedUser = userRepository.save(user);
        eventPublisher.publishEvent(new UserRegisterEvent(savedUser));
        return savedUser;
    }

    @Transactional
    public User createAdmin(UserRegisterDto userRegisterDto) {
        User user = userMapper.mapToUser(userRegisterDto);
        user.setType("admin");
        userUtils.userBuilder(user);
        return userRepository.save(user);

    }


    public AuthResponse login(LoginRequest userLoginRequest) {
        User user = userRepository.findByUsernameOrEmail(userLoginRequest.getUsernameOrEmail()).orElseThrow();

        String userPassword = userLoginRequest.getPassword().concat(user.getSaltPassword());
        LoginRequest newLoginRequest = LoginRequest.builder()
                .usernameOrEmail(userLoginRequest.getUsernameOrEmail())
                .password(userPassword)
                .build();
        return authenticate(newLoginRequest);
    }


    public AuthResponse authenticate(LoginRequest loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                (loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));

        User user = userRepository.findByUsernameOrEmail(loginRequest.getUsernameOrEmail()).orElseThrow();
        String userIdentifier = isEmail(loginRequest.getUsernameOrEmail()) ? user.getEmail() : user.getUsername();

        String token = jwtService.generateToken(userIdentifier);
        return AuthResponse.builder()
                .token(token)
//                .role(user.getRole().getRoleName())
                .build();
    }

    public UserDetails getCredentials() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return (UserDetails) authentication.getPrincipal();
        } else {
            throw new AccessDeniedException("UnAuthorized");
        }
    }

    private boolean isEmail(String userInput) {
        String emailRegex = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
        return userInput.matches(emailRegex);
    }


//    public VerificationStatus verifyUserAccount(String verificationCode) {
//
//
//    }
}
