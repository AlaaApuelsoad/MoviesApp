package com.alaa.MoviesApp.service;

import com.alaa.MoviesApp.dto.AppResponse;
import com.alaa.MoviesApp.dto.AuthResponse;
import com.alaa.MoviesApp.dto.LoginRequest;
import com.alaa.MoviesApp.enums.ErrorCode;
import com.alaa.MoviesApp.enums.UserTypes;
import com.alaa.MoviesApp.exception.BusinessException;
import com.alaa.MoviesApp.model.User;
import com.alaa.MoviesApp.utils.AppResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;

    public AppResponse<AuthResponse> login(LoginRequest userLoginRequest) {

        User user = userService.getUser(userLoginRequest.getUsername());
        validateAccountVerification(user);
        String userPassword = userLoginRequest.getPassword().concat(user.getSaltPassword());

        if (!bCryptPasswordEncoder.matches(userPassword, user.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
        }
        LoginRequest newLoginRequest = LoginRequest.builder()
                .username(userLoginRequest.getUsername())
                .password(userPassword)
                .build();
        return authenticate(newLoginRequest);
    }

    public AppResponse<AuthResponse> authenticate(LoginRequest loginRequest) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword()));

        User user = userService.getUser(loginRequest.getUsername());
        String userIdentifier = user.getUsername();

        String token = jwtService.generateToken(userIdentifier);
         AuthResponse response = AuthResponse.builder()
                .token(token)
                .role(user.getRole().getRoleName())
                .build();
         return AppResponseBuilder.success(response,"auth.login.success");
    }

    public UserDetails getUserCredentials() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            return userDetails;
        } else {
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
        }
    }

    private void validateAccountVerification(User user){
        if (Objects.isNull(user) || !Objects.equals(UserTypes.MEMBER.getType(),user.getType())
                && Boolean.FALSE.equals(user.getIsVerified())){
            throw new BusinessException(ErrorCode.ACCOUNT_NOT_VERIFIED);
        }
    }


}
