package com.fawry.MoviesApp.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfiguration {

    @Bean
    public ObjectMapper objectMapperConfig() {
        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
//        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);
        return objectMapper;

    }
}
