package com.alaa.moviesapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class SystemPropertyService {

    private final Environment environment;

    public <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
        return environment.getProperty(key, targetType, defaultValue);
    }

    public <T> T getProperty(String key, Class<T> targetType) {
        return environment.getProperty(key, targetType);
    }

    public String getProperty(String key, String defaultValue) {
        return environment.getProperty(key, defaultValue);
    }

    public String getProperty(String key) {
        return environment.getProperty(key);
    }

    public Integer getIntegerProperty(String key) {
        return Integer.parseInt(Objects.requireNonNull(environment.getProperty(key)));
    }
}
