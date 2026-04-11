package com.alaa.MoviesApp.utils;

import com.alaa.MoviesApp.enums.ErrorCode;
import com.alaa.MoviesApp.exception.BusinessException;
import com.alaa.MoviesApp.model.Role;
import com.alaa.MoviesApp.repository.RoleRepository;
import com.alaa.MoviesApp.service.SystemPropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SystemUtils {

    private final RoleRepository roleRepository;
    private final SystemPropertyService systemPropertyService;

    public static String generateUUIDCode(){
        return UUID.randomUUID().toString();
    }

    public Role findRoleByRoleName(String roleName){
        return roleRepository.findByRoleName(roleName).orElseThrow(
                () -> new BusinessException(ErrorCode.ROLE_NOT_FOUND)
        );
    }

    public Pageable buildPageableObj(Integer pageNumber) {
        int pageSize = Integer.parseInt(systemPropertyService.getProperty("app.page.size"));
        int pageIndex = Math.max(pageNumber - 1, 0);
        return PageRequest.of(pageIndex,pageSize);
    }
}
