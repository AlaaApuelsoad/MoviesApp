package com.alaa.moviesapp.utils;

import com.alaa.moviesapp.dto.AppResponse;
import com.alaa.moviesapp.dto.MetaData;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;

public class AppResponseBuilder {

    public static <T> AppResponse<T> buildResponse(boolean success, T data, String message, HttpStatus status , Object errors,
                                                    MetaData metaData) {
        AppResponse<T> response = new AppResponse<>();
        response.setSuccess(success);
        response.setMessage(message);
        response.setStatus(status);
        response.setData(data);
        response.setErrors(errors);
        response.setMetaData(metaData);
        response.setCorrelationId(MDC.get("X-CORRELATION-ID"));
        response.setTimestamp(System.currentTimeMillis());
        return response;
    }

    private AppResponseBuilder() {

    }

}
