package com.alaa.MoviesApp.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.alaa.MoviesApp.context.LogContext;
import com.alaa.MoviesApp.context.UserContextHolder;
import com.alaa.MoviesApp.service.SystemPropertyService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MDC;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandling {

    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandling.class);
    private final ObjectMapper mapper;
    private final SystemPropertyService systemPropertyService;

    @ExceptionHandler(CustomException.class) //system exception
    public ResponseEntity<?> handleCustomException(CustomException ex, HttpServletRequest request) throws JsonProcessingException {

        LogContext logContext = LogContext.builder()
                .timestamp(LocalDateTime.now().toString())
                .correlationId(MDC.get("X-Correlation-ID"))
                .level("ERROR")
                .environment(systemPropertyService.getActiveProfile())
                .logger(logger.getName())
                .thread(Thread.currentThread().getName())
                .httpMethod(request.getMethod())
                .uri(request.getRequestURI())
                .responseStatus(ex.getErrorCode().getHttpStatus().value())
                .responseTimMs(System.currentTimeMillis() - Long.parseLong(MDC.get("Start-Time")))
                .userId(UserContextHolder.getLoggedInUserContext().getUserId())
                .userName(UserContextHolder.getLoggedInUserContext().getUserName())
                .role(UserContextHolder.getLoggedInUserContext().getRole())
                .errorType(ex.getClass().getSimpleName())
                .errorMessage(ex.getMessage())
                .rootCause(ex.getCause() != null ? ex.getCause().getMessage() : null)
                .stackTrace(writeStackTrace(ex))
                .build();

        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(ex.getMessage())
                .correlationId(MDC.get("X-Correlation-ID"))
                .timestamp(LocalDateTime.now())
                .code(ex.getErrorCode().getCode())
                .build();

        logger.error(mapper.writeValueAsString(logContext));//console for dev logging

        return new ResponseEntity<>(errorResponse,ex.getErrorCode().getHttpStatus());

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex, HttpServletRequest request) throws JsonProcessingException {

        LogContext logContext = LogContext.builder()
                .timestamp(LocalDateTime.now().toString())
                .correlationId(MDC.get("X-Correlation-ID"))
                .level("ERROR")
                .environment(systemPropertyService.getActiveProfile())
                .logger(logger.getName())
                .thread(Thread.currentThread().getName())
                .httpMethod(request.getMethod())
                .uri(request.getRequestURI())
                .responseStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .responseTimMs(System.currentTimeMillis() - Long.parseLong(MDC.get("Start-Time")))
                .userId(UserContextHolder.getLoggedInUserContext().getUserId())
                .userName(UserContextHolder.getLoggedInUserContext().getUserName())
                .role(UserContextHolder.getLoggedInUserContext().getRole())
                .errorType(ex.getClass().getSimpleName())
                .errorMessage(ex.getMessage())
                .rootCause(ex.getCause() != null ? ex.getCause().getMessage() : null)
                .stackTrace(writeStackTrace(ex))
                .build();


        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(ex.getMessage())
                .correlationId(MDC.get("X-Correlation-ID"))
                .timestamp(LocalDateTime.now())
                .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .build();

        logger.error(mapper.writeValueAsString(logContext));//console
        return new ResponseEntity<>(errorResponse,HttpStatus.INTERNAL_SERVER_ERROR);

    }

    private String getStackTrace(Exception exception) {
        StackTraceElement[] stackTraceElements = exception.getStackTrace();
        if (stackTraceElements.length > 0) {
            StackTraceElement origin = stackTraceElements[0];
            String className = origin.getClassName();
            String methodName = origin.getMethodName();
            int lineNumber = origin.getLineNumber();
            return className + "." + methodName + "(" + lineNumber + ")";
        }
        return "Unknown Origin";
    }

    private String writeStackTrace(Exception exception) {
        StringWriter sw = new StringWriter();
        exception.printStackTrace(new java.io.PrintWriter(sw));
        return sw.toString();
    }


    @org.springframework.web.bind.annotation.ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(ex.getMessage())
                .correlationId(MDC.get("X-Correlation-ID"))
                .timestamp(LocalDateTime.now())
                .code(HttpStatus.BAD_REQUEST.toString())
                .details(errors)
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleBadRequestException(DataIntegrityViolationException ex) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(ex.getMostSpecificCause().getMessage())
                .correlationId(MDC.get("X-Correlation-ID"))
                .timestamp(LocalDateTime.now())
                .code(HttpStatus.BAD_REQUEST.toString())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
