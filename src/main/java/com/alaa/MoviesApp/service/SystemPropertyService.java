package com.alaa.MoviesApp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SystemPropertyService {

    private static Environment environment;

    public SystemPropertyService (ApplicationContext context){
        environment = context.getEnvironment();
    }


    public static <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
        return environment.getProperty(key, targetType, defaultValue);
    }

    public static <T> T getProperty(String key, Class<T> targetType) {
        return environment.getProperty(key, targetType);
    }

    public static String getProperty(String key, String defaultValue) {
        return environment.getProperty(key, defaultValue);
    }

    public static String getProperty(String key) {
        return environment.getProperty(key);
    }

    public static String [] getActiveProfile(){
        return environment.getActiveProfiles();
    }


}
