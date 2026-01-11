package com.alaa.MoviesApp.utils;

import com.alaa.MoviesApp.enums.ErrorCode;
import com.alaa.MoviesApp.exception.CustomException;
import com.alaa.MoviesApp.model.User;
import com.alaa.MoviesApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserUtils {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final SystemUtils systemUtils;
    private final UserRepository userRepository;

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
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword().concat(saltPassword)));
    }

    public User getUser(String userIdentifier){
        return userRepository.findByUsernameOrEmail(userIdentifier).orElseThrow(
                ()-> new CustomException(ErrorCode.INVALID_CREDENTIALS)
        );
    }

}
