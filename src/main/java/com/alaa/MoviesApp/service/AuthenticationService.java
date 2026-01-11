package com.alaa.MoviesApp.service;

import com.alaa.MoviesApp.dto.AuthResponse;
import com.alaa.MoviesApp.dto.LoginRequest;
import com.alaa.MoviesApp.enums.ErrorCode;
import com.alaa.MoviesApp.exception.CustomException;
import com.alaa.MoviesApp.model.User;
import com.alaa.MoviesApp.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserUtils userUtils;

    public AuthResponse login(LoginRequest userLoginRequest) {

        User user = userUtils.getUser(userLoginRequest.getUsername());
        validateAccountVerification(user);
        String userPassword = userLoginRequest.getPassword().concat(user.getSaltPassword());

        if (!bCryptPasswordEncoder.matches(userPassword, user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_CREDENTIALS);
        }
        LoginRequest newLoginRequest = LoginRequest.builder()
                .username(userLoginRequest.getUsername())
                .password(userPassword)
                .build();
        return authenticate(newLoginRequest);
    }

    public AuthResponse authenticate(LoginRequest loginRequest) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword()));

        User user = userUtils.getUser(loginRequest.getUsername());
        String userIdentifier = user.getUsername();

        String token = jwtService.generateToken(userIdentifier);
        return AuthResponse.builder()
                .token(token)
                .role(user.getRole().getRoleName())
                .build();
    }

    public UserDetails getUserCredentials() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return (UserDetails) authentication.getPrincipal();
        } else {
            throw new CustomException(ErrorCode.INVALID_CREDENTIALS);
        }
    }

    private void validateAccountVerification(User user){
        if (user != null && "member".equals(user .getType()) && !user.isVerified()){
            throw new CustomException(ErrorCode.ACCOUNT_NOT_VERIFIED);
        }
    }


}
