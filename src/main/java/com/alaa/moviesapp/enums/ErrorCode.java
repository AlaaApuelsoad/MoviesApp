package com.alaa.moviesapp.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

    BAD_REQUEST("E001", "error.validation", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND("E002", "user.not.found", HttpStatus.NOT_FOUND),
    MOVIE_NOT_FOUND("E003", "movie.not.found", HttpStatus.NOT_FOUND),
    ROLE_NOT_FOUND("E004", "role.not.found", HttpStatus.NOT_FOUND),
    PERMISSION_DENIED("E005", "auth.access.denied", HttpStatus.UNAUTHORIZED),
    INTERNAL_SERVER_ERROR("E006", "error.internal.server", HttpStatus.INTERNAL_SERVER_ERROR),
    TOKEN_EXPIRED("E007", "auth.token.expired", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN_SIGNATURE("E008", "auth.invalid.token", HttpStatus.UNAUTHORIZED),
    INVALID_CREDENTIALS("E009", "auth.login.failed", HttpStatus.UNAUTHORIZED),
    NO_DATA_FOUND("E010", "error.no.data.found", HttpStatus.NOT_FOUND),
    ACCOUNT_DELETED("E011", "account.deleted", HttpStatus.NOT_FOUND),
    MOVIE_EXISTS("E012", "movie.exists", HttpStatus.CONFLICT),
    ALREADY_DELETED("E013", "movie.already.deleted", HttpStatus.CONFLICT),
    ACCOUNT_NOT_VERIFIED("E014", "account.not.verified", HttpStatus.UNAUTHORIZED),
    DATA_INTEGRITY_VIOLATION("E015", "database.unique.constraint", HttpStatus.CONFLICT),
    CODE_EXPIRED("E016", "code.expired", HttpStatus.BAD_REQUEST),
    VERIFICATION_USER_NOT_FOUND("E017", "verification.user.not.found", HttpStatus.NOT_FOUND),
    INVALID_VERIFICATION_CODE("E018", "invalid.verification.code", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String messageKey;
    private final HttpStatus httpStatus;
}