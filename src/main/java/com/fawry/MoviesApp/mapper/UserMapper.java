package com.fawry.MoviesApp.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fawry.MoviesApp.dto.UserRegisterDto;
import com.fawry.MoviesApp.dto.UserRegisterResponse;
import com.fawry.MoviesApp.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ObjectMapper objectMapper;


    public User mapToUser(UserRegisterDto userRegisterDto) {
        return objectMapper.convertValue(userRegisterDto, User.class);
    }

    public UserRegisterResponse mapToUserRegisterResponse(User user) {
        UserRegisterResponse userRegisterResponse = objectMapper.convertValue(user, UserRegisterResponse.class);
        if ("admin".equals(user.getType())){
            userRegisterResponse.setAccountVerify("verified admin");
        }
        return userRegisterResponse;
    }


}
