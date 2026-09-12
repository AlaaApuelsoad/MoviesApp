package com.alaa.MoviesApp.context;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LogContext {

    private Instant timestamp;
    private String correlationId;
    private String thread;
    private String logger;

    private String httpMethod;
    private String uri;
    private Integer responseStatus;
    private Long responseTimMs;

    private Long userId;
    private String userName;
    private String role;

    private String errorType;
    private String errorMessage;
    private String stackTrace;
    private String rootCause;
}
