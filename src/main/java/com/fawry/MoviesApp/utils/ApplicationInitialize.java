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
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ApplicationInitialize {


    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    @Value("${app.admin.password}")
    private String adminPassword;
    private static final Logger logger = LogManager.getLogger(ApplicationInitialize.class);

    public ApplicationInitialize(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    private void init() {

        List<String> roleNames = Arrays.asList("ADMIN", "USER");

        roleNames.forEach(roleName -> {
            if (!roleRepository.existsByRoleName(roleName)) {
                Role role = new Role();
                role.setRoleName(roleName);
                roleRepository.save(role);
            } else {
                logger.info("Role {} already exists.", roleName);
            }
        });
    }

    @PostConstruct
    public void createMainAdmin() {
        String adminUserName = "admin";

        if (!userRepository.existsByUsername(adminUserName)) {
            User adminUser = new User();
            adminUser.setFirstName("Admin");
            adminUser.setLastName("Admin");
            adminUser.setUsername(adminUserName);
            adminUser.setPassword(adminPassword);
            adminUser.setEmail("admin@fawry.task.com");
            adminUser.setVerified(true);
            adminUser.setDeleted(false);
            adminUser.setRole(roleRepository.findByRoleName("ADMIN"));

            userRepository.save(adminUser);
        } else {
            logger.info("User {} already exists.", adminUserName);
        }
    }
}
