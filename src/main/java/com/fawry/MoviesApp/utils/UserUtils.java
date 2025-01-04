package com.fawry.MoviesApp.utils;

import com.fawry.MoviesApp.model.User;
import com.fawry.MoviesApp.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserUtils {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final Utils utils;
    private final RoleRepository roleRepository;

    public void userBuilder(User user) {
        String saltPassword = utils.generateUUIDCode();
        String verificationCode = utils.generateUUIDCode();

        if (user.getType().equals("admin")) {
            user.setRole(utils.findRoleByRoleName("ADMIN"));
        }
        if (user.getType().equals("user")) {
            user.setRole(utils.findRoleByRoleName("USER"));
        }
        user.setSaltPassword(saltPassword);
        user.setVerificationCode(verificationCode);
        user.setVerified(true);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword().concat(saltPassword)));

    }
}
