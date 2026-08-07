package com.alaa.moviesapp.service;

import com.alaa.moviesapp.model.Role;
import com.alaa.moviesapp.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    @Transactional
    public Role getAdminRoleReference(){
        return roleRepository.getReferenceById(Role.RoleEnum.ADMIN.getId());
    }

    @Transactional
    public Role getMemberRoleReference(){
        return roleRepository.getReferenceById(Role.RoleEnum.MEMBER.getId());
    }

}
