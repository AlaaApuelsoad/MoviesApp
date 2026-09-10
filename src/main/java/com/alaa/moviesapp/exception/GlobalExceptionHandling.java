package com.alaa.MoviesApp.exception;

import com.alaa.MoviesApp.constants.AppConstant;
import com.alaa.MoviesApp.context.LogContext;
import com.alaa.MoviesApp.dto.AppResponse;
import com.alaa.MoviesApp.enums.ErrorCode;
import com.alaa.MoviesApp.utils.AppResponseBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandling {

    private final ObjectMapper mapper;
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandling.class);


    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<AppResponse<Object>> handleBusinessException(BusinessException ex, HttpServletRequest request) throws JacksonException {
        logError(ex,request,ex.getErrorCode().getHttpStatus());
        return new ResponseEntity<>(AppResponseBuilder.error(ex.getErrorCode(),ex.getErrorCode().getMessageKey())
                ,ex.getErrorCode().getHttpStatus());

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<AppResponse<Object>> handleGenericException(Exception ex, HttpServletRequest request) throws JacksonException {
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        logError(ex, request, errorCode.getHttpStatus());
        return new ResponseEntity<>(AppResponseBuilder.error(errorCode,errorCode.getMessageKey()),
                errorCode.getHttpStatus());
    }

    @ExceptionHandler
    public ResponseEntity<AppResponse<Object>> handleValidationException(MethodArgumentNotValidException ex) {
        ErrorCode errorCode = ErrorCode.BAD_REQUEST;
        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(AppResponseBuilder.error(errorCode,errorCode.getMessageKey(),
                errors),errorCode.getHttpStatus());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<AppResponse<Object>> handleDataIntegrityViolationException() {
        ErrorCode errorCode = ErrorCode.DATA_INTEGRITY_VIOLATION;
        return new ResponseEntity<>(AppResponseBuilder.error(errorCode,errorCode.getMessageKey()),
                errorCode.getHttpStatus());
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

    public void handleJwtException(HttpServletResponse response, String message) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String jsonResponse = String.format("{\"error\": \"%s\", \"message\": \"%s\"}", "Authentication error", message);
        response.getWriter().write(jsonResponse);
    }

    public void logError(Exception ex, HttpServletRequest request, HttpStatus status) throws JacksonException {

        if (!logger.isErrorEnabled()){
            return;
        }
        LogContext logContext = LogContext.builder()
                .timestamp(Instant.now())
                .correlationId(MDC.get(AppConstant.X_CORRELATION_ID))
                .logger(logger.getName())
                .thread(Thread.currentThread().getName())
                .httpMethod(request.getMethod())
                .uri(request.getRequestURI())
                .responseStatus(status.value())
                .responseTimMs(System.currentTimeMillis() - Long.parseLong(MDC.get(AppConstant.REQUEST_START_TIME)))
                .errorType(ex.getClass().getSimpleName())
                .errorMessage(ex.getMessage())
                .rootCause(ex.getCause() != null ? ex.getCause().getMessage() : null)
                .stackTrace(getStackTrace(ex))
                .build();

        logger.error(mapper.writeValueAsString(logContext));
    }
}
