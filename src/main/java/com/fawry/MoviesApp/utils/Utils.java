package com.fawry.MoviesApp.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class Utils {

    @Value("${app.uuid.code.length}")
    private int CODE_LENGTH;

    public String generateUUIDCode(){
        return UUID.randomUUID().toString().substring(0,CODE_LENGTH);
    }


}
