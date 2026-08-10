package com.alaa.moviesapp.exception;

import com.alaa.moviesapp.enums.ErrorCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String errorCodeValue;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessageKey());
        this.errorCode = errorCode;
        this.errorCodeValue = errorCode.getCode();
    }

}
