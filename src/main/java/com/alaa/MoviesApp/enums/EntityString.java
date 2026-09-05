package com.alaa.MoviesApp.enums;

import lombok.Getter;

@Getter
public enum EntityString {

    MOVIE("Movie"),
    USER("User"),
    ROLE("Role"),
    RATING("Rating");

    private final String name;

    EntityString(String name) {
        this.name = name;
    }
}
