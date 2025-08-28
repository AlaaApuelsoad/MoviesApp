package com.fawry.MoviesApp.utils;

import com.fawry.MoviesApp.enums.ErrorCode;
import com.fawry.MoviesApp.exception.CustomException;
import com.fawry.MoviesApp.model.Role;
import com.fawry.MoviesApp.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SystemUtils {

    private final RoleRepository roleRepository;

    @Value("${app.uuid.code.length}")
    private int CODE_LENGTH;

    public String generateUUIDCode(){
        return UUID.randomUUID().toString().substring(0,CODE_LENGTH);
    }


    public Role findRoleByRoleName(String roleName){
        return roleRepository.findByRoleName(roleName).orElseThrow(
                () -> new CustomException(ErrorCode.ROLE_NOT_FOUND)
        );
    }
}
