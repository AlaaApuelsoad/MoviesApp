package com.alaa.MoviesApp.configuration;

import com.alaa.MoviesApp.model.User;
import lombok.NonNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;
import java.util.Optional;

public class AuditAware implements AuditorAware<Long> {

    @Override
    @NonNull
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (Objects.isNull(authentication) || !authentication.isAuthenticated()) {
            return Optional.of(0L);
        }
        Object principal = authentication.getPrincipal();

        if (principal instanceof User user) {
            return Optional.of(user.getId());
        }
        return Optional.of(0L);
    }
}
