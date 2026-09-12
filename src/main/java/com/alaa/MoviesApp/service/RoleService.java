package com.alaa.MoviesApp.service;
import com.alaa.MoviesApp.model.Role;
import com.alaa.MoviesApp.repository.RoleRepository;
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
