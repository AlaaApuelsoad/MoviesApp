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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthResponse login(LoginRequest userLoginRequest) {

        User user = userRepository.findByUsernameOrEmail(userLoginRequest.getUsernameOrEmail()).orElseThrow(
                () -> new CustomException(ErrorCode.INVALID_CREDENTIALS)
        );

        String userPassword = userLoginRequest.getPassword().concat(user.getSaltPassword());

        if (!bCryptPasswordEncoder.matches(userPassword, user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_CREDENTIALS);
        }
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
                () -> new CustomException(ErrorCode.INVALID_CREDENTIALS)
        );
        String userIdentifier = user.getUsername();

        String token = jwtService.generateToken(userIdentifier);
        return AuthResponse.builder()
                .token(token)
                .build();
    }
}
