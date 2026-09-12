package com.alaa.MoviesApp.enums;

import lombok.Getter;

@Getter
public enum UserTypes {
    ADMIN("admin"),
    MEMBER("member");

    private final String type;

    UserTypes(String type) {
        this.type = type;
    }
}
