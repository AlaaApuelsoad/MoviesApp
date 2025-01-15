package com.fawry.MoviesApp.utils;

import com.fawry.MoviesApp.enums.ErrorCode;
import com.fawry.MoviesApp.exception.CustomException;
import com.fawry.MoviesApp.model.User;
import com.fawry.MoviesApp.repository.RoleRepository;
import com.fawry.MoviesApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserUtils {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final Utils utils;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public void userBuilder(User user) {
        String saltPassword = utils.generateUUIDCode();
        String verificationCode = utils.generateUUIDCode();

        if (user.getType().equals("admin")) {
            user.setRole(utils.findRoleByRoleName("ADMIN"));
            user.setVerified(true);
            user.setVerificationCode(null);
            user.setVerificationCodeExpiryDate(null);
        }
        if (user.getType().equals("member")) {
            user.setRole(utils.findRoleByRoleName("MEMBER"));
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
