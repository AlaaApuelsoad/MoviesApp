package com.alaa.MoviesApp.repository;

import com.alaa.MoviesApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String adminUserName);

    @Transactional
    @Query("SELECT u FROM User u WHERE u.username = :userIdentifier OR u.email = :userIdentifier")
    Optional<User> findByUsernameOrEmail(@Param("userIdentifier") String userIdentifier);

    @Transactional
    @Query("SELECT u from User u WHERE u.verificationCode = :verificationCode")
    Optional<User> findByVerificationCode(@Param("verificationCode") String verificationCode);
}
