package com.fawry.MoviesApp.listener;

import com.fawry.MoviesApp.model.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;



@Getter
public class UserRegisterEvent extends ApplicationEvent {

    private String email;
    private String verificationCode;

    public UserRegisterEvent(Object source,User user) {
        super(source);
        this.email = user.getEmail();
        this.verificationCode = user.getVerificationCode();
    }




}
