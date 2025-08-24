package com.fawry.MoviesApp.exception;

import com.fawry.MoviesApp.enums.ErrorCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String errorCodeValue;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.errorCodeValue = errorCode.getCode();
    }

}
