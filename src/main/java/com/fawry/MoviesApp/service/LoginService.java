package com.fawry.MoviesApp.service;

import com.fawry.MoviesApp.dto.AuthResponse;
import com.fawry.MoviesApp.dto.LoginRequest;
import com.fawry.MoviesApp.enums.ErrorCode;
import com.fawry.MoviesApp.exception.CustomException;
import com.fawry.MoviesApp.jwt.JwtService;
import com.fawry.MoviesApp.model.User;
import com.fawry.MoviesApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthResponse login(LoginRequest userLoginRequest) {

        User user = userRepository.findByUsernameOrEmail(userLoginRequest.getUsernameOrEmail()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );

        String userPassword = userLoginRequest.getPassword().concat(user.getSaltPassword());
        LoginRequest newLoginRequest = LoginRequest.builder()
                .usernameOrEmail(userLoginRequest.getUsernameOrEmail())
                .password(userPassword)
                .build();
        return authenticate(newLoginRequest);
    }

    public AuthResponse authenticate(LoginRequest loginRequest) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(),
                loginRequest.getPassword()));

        User user = userRepository.findByUsernameOrEmail(loginRequest.getUsernameOrEmail()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        String userIdentifier = user.getUsername();

        String token = jwtService.generateToken(userIdentifier);
        return AuthResponse.builder()
                .token(token)
                .build();
    }
}
