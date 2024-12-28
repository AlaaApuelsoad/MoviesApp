package com.fawry.MoviesApp.repository;

import com.fawry.MoviesApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    boolean existsByUsername(String adminUserName);
}
