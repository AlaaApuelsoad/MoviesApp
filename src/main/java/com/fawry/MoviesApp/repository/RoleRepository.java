package com.fawry.MoviesApp.repository;

import com.fawry.MoviesApp.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    boolean existsByRoleName(String roleName);

    Optional<Role> findByRoleName(String roleName);

//    @Query("SELECT r.id from Role  r where r.roleName = :roleName")
//    long roleIdByRoleName(@Param("roleName") String roleName);

}
