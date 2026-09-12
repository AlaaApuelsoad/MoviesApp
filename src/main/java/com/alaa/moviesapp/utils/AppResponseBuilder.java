package com.alaa.MoviesApp.utils;

import com.alaa.MoviesApp.constants.AppConstant;
import com.alaa.MoviesApp.dto.AppResponse;
import com.alaa.MoviesApp.dto.MetaData;
import com.alaa.MoviesApp.enums.ErrorCode;
import com.alaa.MoviesApp.service.MessageService;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;

import java.time.Instant;

public final class AppResponseBuilder {

    private AppResponseBuilder() {
        /* This utility class should not be instantiated */
    }

    private static MessageService messageService;

    public static void setMessageService(MessageService messageService) {
        AppResponseBuilder.messageService = messageService;
    }

    public static <T> AppResponse<T> success(T data, HttpStatus status, String message, Object ...args) {
        return buildResponse(true, data, null, status, null, null,message,args);
    }

    public static <T> AppResponse<T> success(T data, MetaData metaData, HttpStatus status, String message, Object ...args) {
        return buildResponse(true, data, metaData, status, null, null,message,args);
    }

    public static <T> AppResponse<T> error(ErrorCode errorCode, String message, Object errors) {
        return buildResponse(false, null,null, errorCode.getHttpStatus(),
                errorCode.getCode(), errors, null,message);
    }

    public static <T> AppResponse<T> success(T data, String message) {
        return success(data, HttpStatus.OK, message);
    }

    public static <T> AppResponse<T> success(String message, HttpStatus status) {
        return success(null, status, message);
    }

    public static <T> AppResponse<T> success(T data, MetaData metaData, String message){
        return success(data, metaData, HttpStatus.OK, message);
    }

    public static <T> AppResponse<T> success(String message) {
        return success(null, HttpStatus.OK, message);
    }

    public static <T> AppResponse<T> error(ErrorCode errorCode, String message) {
        return error(errorCode, message, null);
    }

    private static <T> AppResponse<T> buildResponse(boolean success, T data, MetaData metaData, HttpStatus status, String errorCode,
                                                    Object errors, String message, Object ...args) {
        return AppResponse.<T>builder()
                .success(success)
                .message(messageService.getMessage(message,args))
                .status(status.value())
                .errorCode(errorCode)
                .data(data)
                .metaData(metaData)
                .errors(errors)
                .correlationId(MDC.get(AppConstant.X_CORRELATION_ID))
                .timestamp(Instant.now())
                .build();
    }

}
