package com.fawry.MoviesApp.utils;

import com.fawry.MoviesApp.model.Role;
import com.fawry.MoviesApp.model.User;
import com.fawry.MoviesApp.repository.RoleRepository;
import com.fawry.MoviesApp.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ApplicationInitialize {


    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    @Value("${app.admin.password}")
    private String adminPassword;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final SystemUtils systemUtils;
    private static final Logger logger = LogManager.getLogger(ApplicationInitialize.class);



    @PostConstruct
    public void inti(){
        createRoles();
        createMainAdmin();
    }

    private void createRoles() {

        List<String> roleNames = Arrays.asList("ADMIN", "MEMBER");

        roleNames.forEach(roleName -> {
            if (!roleRepository.existsByRoleName(roleName)) {
                Role role = Role.builder()
                        .roleName(roleName)
                        .build();
                roleRepository.save(role);
            } else {
                logger.info("Role {} already exists.", roleName);
            }
        });
    }

    public void createMainAdmin() {
        String adminUserName = "admin";
        String saltPass = systemUtils.generateUUIDCode();

        if (!userRepository.existsByUsername(adminUserName)) {
            User adminUser = User.builder()
                    .firstName("Admin")
                    .lastName("Admin")
                    .username(adminUserName)
                    .password(bCryptPasswordEncoder.encode(adminPassword.concat(saltPass)))
                    .email("admin@fawry.com")
                    .isVerified(true)
                    .isDeleted(false)
                    .type("admin")
                    .saltPassword(saltPass)
                    .role(systemUtils.findRoleByRoleName("ADMIN"))
                    .build();

            userRepository.save(adminUser);
        } else {
            logger.info("User {} already exists.", adminUserName);
        }
    }


}
