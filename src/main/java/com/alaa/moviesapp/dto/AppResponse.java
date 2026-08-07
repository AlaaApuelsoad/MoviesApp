package com.alaa.moviesapp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppResponse<T> {

    private boolean success;
    private String message;
    private HttpStatus status;
    private T data;
    private Object errors;
    private MetaData metaData;
    private String correlationId;
    private long timestamp;

}

