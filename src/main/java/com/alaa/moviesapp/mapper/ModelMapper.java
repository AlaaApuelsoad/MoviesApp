package com.alaa.MoviesApp.mapper;

import com.alaa.MoviesApp.dto.UserRegisterResponse;
import com.alaa.MoviesApp.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

/**
 * ModelMapper is responsible for mapping / converting data between different representations
 */
@Component
@RequiredArgsConstructor
public class ModelMapper {

    private final ObjectMapper objectMapper;

    public <S,T> T map(S source, Class<T> targetClass) throws JacksonException {
        return objectMapper.convertValue(source, targetClass);
    }

    public UserRegisterResponse mapToUserRegisterResponse(User user) {
        UserRegisterResponse userRegisterResponse = objectMapper.convertValue(user, UserRegisterResponse.class);
        if ("admin".equals(user.getType())){
            userRegisterResponse.setAccountVerify("verified admin");
        }
        return userRegisterResponse;
    }

}
