package com.fawry.MoviesApp.utils;

import com.fawry.MoviesApp.enums.ErrorCode;
import com.fawry.MoviesApp.exception.CustomException;
import com.fawry.MoviesApp.model.Role;
import com.fawry.MoviesApp.repository.RoleRepository;
import com.fawry.MoviesApp.service.SystemPropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SystemUtils {

    private final RoleRepository roleRepository;

    public String accountVerificationEmailBuild(String verificationLink) throws IOException {
        Path path = Paths.get(SystemPropertyService.getProperty("app.template.Account.Verification"));
        String templateContent = Files.readString(path);
        return templateContent.replace("{{verification_link}}",verificationLink);
    }

    public static String generateUUIDCode(){
        return UUID.randomUUID().toString();
    }

    public Role findRoleByRoleName(String roleName){
        return roleRepository.findByRoleName(roleName).orElseThrow(
                () -> new CustomException(ErrorCode.ROLE_NOT_FOUND)
        );
    }
}
