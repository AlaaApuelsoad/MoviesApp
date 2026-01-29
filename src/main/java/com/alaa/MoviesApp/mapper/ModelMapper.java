package com.alaa.MoviesApp.mapper;

import com.alaa.MoviesApp.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * ModelMapper is responsible for mapping / converting data between different representations
 */
@Component
@RequiredArgsConstructor
public class ModelMapper {

    private final ObjectMapper objectMapper;

    public <S,T> T map(S source, Class<T> targetClass) throws JsonProcessingException {
        return objectMapper.convertValue(source, targetClass);
    }

}
