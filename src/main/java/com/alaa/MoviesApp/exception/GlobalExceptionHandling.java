package com.alaa.MoviesApp.exception;

import com.alaa.MoviesApp.constants.AppConstant;
import com.alaa.MoviesApp.dto.AppResponse;
import com.alaa.MoviesApp.utils.AppResponseBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.alaa.MoviesApp.context.LogContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandling.class);
    private final ObjectMapper mapper;

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<AppResponse<Object>> handleBusinessException(BusinessException ex, HttpServletRequest request) throws JsonProcessingException {

        LogContext logContext = LogContext.builder()
                .timestamp(LocalDateTime.now().toString())
                .correlationId(MDC.get("X-Correlation-ID"))
                .logger(logger.getName())
                .thread(Thread.currentThread().getName())
                .httpMethod(request.getMethod())
                .uri(request.getRequestURI())
                .responseStatus(ex.getErrorCode().getHttpStatus().value())
                .responseTimMs(System.currentTimeMillis() - Long.parseLong(MDC.get(AppConstant.REQUEST_START_TIME)))
                .errorType(ex.getClass().getSimpleName())
                .errorMessage(ex.getMessage())
                .rootCause(ex.getCause() != null ? ex.getCause().getMessage() : null)
                .stackTrace(getStackTrace(ex))
                .build();

        logger.error(mapper.writeValueAsString(logContext));

        return new ResponseEntity<>(AppResponseBuilder.buildResponse(false, null, ex.getMessage(), ex.getErrorCode().getHttpStatus(),
                null, null),ex.getErrorCode().getHttpStatus());

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<AppResponse<Object>> handleGenericException(Exception ex, HttpServletRequest request) throws JsonProcessingException {

        LogContext logContext = LogContext.builder()
                .timestamp(LocalDateTime.now().toString())
                .correlationId(MDC.get("X-Correlation-ID"))
                .logger(logger.getName())
                .thread(Thread.currentThread().getName())
                .httpMethod(request.getMethod())
                .uri(request.getRequestURI())
                .responseStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .responseTimMs(System.currentTimeMillis() - Long.parseLong(MDC.get("Start-Time")))
                .errorType(ex.getClass().getSimpleName())
                .errorMessage(ex.getMessage())
                .rootCause(ex.getCause() != null ? ex.getCause().getMessage() : null)
                .stackTrace(writeStackTrace(ex))
                .build();

        logger.error(mapper.writeValueAsString(logContext));
        return new ResponseEntity<>(AppResponseBuilder.buildResponse(
                false,null,ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR,null,null
        ),HttpStatus.INTERNAL_SERVER_ERROR);

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

        return new ResponseEntity<>(
                AppResponseBuilder.buildResponse(false,null,ex.getMessage(),HttpStatus.BAD_REQUEST
                        ,errors,null),HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<AppResponse<Object>> handleBadRequestException(DataIntegrityViolationException ex) {

        return new ResponseEntity<>(AppResponseBuilder.buildResponse(
                false,null,ex.getMessage(),HttpStatus.BAD_REQUEST,null,null
        ),HttpStatus.BAD_REQUEST);
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
}
