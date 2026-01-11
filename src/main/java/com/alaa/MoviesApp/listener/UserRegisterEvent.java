package com.alaa.MoviesApp.listener;

import com.alaa.MoviesApp.model.User;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserRegisterEvent  {

    private String email;
    private String verificationCode;

    public UserRegisterEvent(User user) {
        this.email = user.getEmail();
        this.verificationCode = user.getVerificationCode();
    }




}
