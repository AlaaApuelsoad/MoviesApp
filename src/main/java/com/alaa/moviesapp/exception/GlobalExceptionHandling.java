package com.alaa.moviesapp.exception;

import com.alaa.moviesapp.constants.AppConstant;
import com.alaa.moviesapp.context.LogContext;
import com.alaa.moviesapp.dto.AppResponse;
import com.alaa.moviesapp.service.MessageService;
import com.alaa.moviesapp.utils.AppResponseBuilder;
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
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.io.StringWriter;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandling {

    private final ObjectMapper mapper;
    private final MessageService messageService;
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandling.class);


    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<AppResponse<Object>> handleBusinessException(BusinessException ex, HttpServletRequest request) throws JacksonException {

        LogContext logContext = LogContext.builder()
                .timestamp(Instant.now())
                .correlationId(MDC.get(AppConstant.X_CORRELATION_ID))
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

        String logContextString = mapper.writeValueAsString(logContext);
        logger.error(logContextString);

        return new ResponseEntity<>(AppResponseBuilder.buildResponse(false, null, ex.getMessage(),
                ex.getErrorCode().getHttpStatus(), null, null),ex.getErrorCode().getHttpStatus());

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<AppResponse<Object>> handleGenericException(Exception ex, HttpServletRequest request) throws JacksonException {

        LogContext logContext = LogContext.builder()
                .timestamp(Instant.now())
                .correlationId(MDC.get(AppConstant.X_CORRELATION_ID))
                .logger(logger.getName())
                .thread(Thread.currentThread().getName())
                .httpMethod(request.getMethod())
                .uri(request.getRequestURI())
                .responseStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .responseTimMs(System.currentTimeMillis() - Long.parseLong(MDC.get(AppConstant.REQUEST_START_TIME)))
                .errorType(ex.getClass().getSimpleName())
                .errorMessage(ex.getMessage())
                .rootCause(ex.getCause() != null ? ex.getCause().getMessage() : null)
                .stackTrace(writeStackTrace(ex))
                .build();

        String logContextString = mapper.writeValueAsString(logContext);
        logger.error(logContextString);
        return new ResponseEntity<>(AppResponseBuilder.buildResponse(
                false,null, messageService.getMessage("error.internal.server"), HttpStatus.INTERNAL_SERVER_ERROR,
                null,null),HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<AppResponse<Object>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(
                AppResponseBuilder.buildResponse(false,null, messageService.getMessage("error.bad.request"),
                        HttpStatus.BAD_REQUEST,errors,null),HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler()
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ResponseEntity<AppResponse<Object>> handleBadRequestException() {
//        return new ResponseEntity<>(AppResponseBuilder.buildResponse(
//                false,null, messageService.getMessage("error.bad.request"), HttpStatus.BAD_REQUEST,
//                null,null),HttpStatus.BAD_REQUEST);
//    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<AppResponse<Object>> handleDataIntegrityViolationException() {
        return new ResponseEntity<>(AppResponseBuilder.buildResponse(
                false,null,messageService.getMessage("database.unique.constraint"),HttpStatus.BAD_REQUEST,
                null,null),HttpStatus.BAD_REQUEST);
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
