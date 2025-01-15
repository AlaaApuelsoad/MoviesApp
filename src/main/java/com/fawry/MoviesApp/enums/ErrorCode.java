package com.fawry.MoviesApp.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

    INVALID_INPUT("E001", "Invalid input provided", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND("E002", "User not found", HttpStatus.NOT_FOUND),
    MOVIE_NOT_FOUND("E003", "Movie not found", HttpStatus.NOT_FOUND),
    ROLE_NOT_FOUND("E004", "Role not found", HttpStatus.NOT_FOUND),
    PERMISSION_DENIED("E005", "Permission denied", HttpStatus.UNAUTHORIZED),
    INTERNAL_SERVER_ERROR("E006", "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),
    TOKEN_EXPIRED("E007", "Token expired", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN_SIGNATURE("E008", "Invalid token signature", HttpStatus.UNAUTHORIZED),
    INVALID_CREDENTIALS("E009", "Invalid username and password", HttpStatus.UNAUTHORIZED),
    NO_DATA_FOUND("E010", "No data found", HttpStatus.NOT_FOUND);


    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

}
