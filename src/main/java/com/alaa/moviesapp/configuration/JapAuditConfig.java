package com.alaa.MoviesApp.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorRef")
public class JapAuditConfig {

    @Bean
    public AuditAware auditorRef() {
        return new AuditAware();
    }
}
