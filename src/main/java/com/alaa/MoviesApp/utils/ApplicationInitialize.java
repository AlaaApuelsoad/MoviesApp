package com.alaa.MoviesApp.utils;

import com.alaa.MoviesApp.model.Role;
import com.alaa.MoviesApp.model.User;
import com.alaa.MoviesApp.repository.UserRepository;
import com.alaa.MoviesApp.service.RoleService;
import com.alaa.MoviesApp.service.SystemPropertyService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Application runner is an interface that lets you execute code after the spring boot application has started
 * and the application context has been initialized.
 */

@Component
@RequiredArgsConstructor
public class ApplicationInitialize implements ApplicationRunner {

    private final RoleService roleService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final SystemPropertyService systemPropertyService;
    private static final Logger logger = LoggerFactory.getLogger(ApplicationInitialize.class);

    @Override
    public void run(@NonNull ApplicationArguments args) {
        createMainAdmin();
    }

    public void createMainAdmin() {
        String adminUserName = "admin";
        String saltPass = SystemUtils.generateUUIDCode();
        if (!userRepository.existsByUsername(adminUserName)) {
            Role role = roleService.getAdminRoleReference();
            User adminUser = User.builder()
                    .firstName("Admin")
                    .lastName("Admin")
                    .username(adminUserName)
                    .password(bCryptPasswordEncoder.encode(systemPropertyService.getProperty("app.admin.password").concat(saltPass)))
                    .email(systemPropertyService.getProperty("app.admin.email"))
                    .isVerified(true)
                    .isDeleted(false)
                    .type("admin")
                    .saltPassword(saltPass)
                    .role(role)
                    .build();

            userRepository.save(adminUser);
            logger.info("Main admin has been created");
        } else {
            logger.info("User {} already exists.", adminUserName);
        }
    }
}
