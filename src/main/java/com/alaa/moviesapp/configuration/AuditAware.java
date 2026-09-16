package com.alaa.MoviesApp.configuration;

import com.alaa.MoviesApp.context.UserContextHolder;
import lombok.NonNull;
import org.springframework.data.domain.AuditorAware;

import java.util.Objects;
import java.util.Optional;

public class AuditAware implements AuditorAware<Long> {

    @Override
    @NonNull
    public Optional<Long> getCurrentAuditor() {
        if (Objects.isNull(UserContextHolder.getLoggedInUserContext()))
            return Optional.of(0L);
        return Optional.ofNullable(UserContextHolder.getLoggedInUserContext().getUserId());
    }
}
