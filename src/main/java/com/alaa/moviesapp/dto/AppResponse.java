package com.alaa.moviesapp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class AppResponse<T> {

    private boolean success;
    private String message;
    private Integer status;
    private String errorCode;
    private T data;
    private Object errors;
    private MetaData metaData;
    private String correlationId;
    private Instant timestamp;

}

