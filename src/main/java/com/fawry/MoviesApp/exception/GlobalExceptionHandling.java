package com.fawry.MoviesApp.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandling {

    private final ErrorLog errorLog;
    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandling.class);


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorLog> handleCustomException(CustomException ex, HttpServletRequest request) {

        errorLog.setLevel("ERROR");
        errorLog.setLogger(GlobalExceptionHandling.class.getName());
        errorLog.setMessage(ex.getMessage());
        errorLog.setCode(ex.getErrorCodeValue());
        errorLog.setRequestedUrl(request.getRequestURI());
        errorLog.setErrorTrace(getStackTrace(ex));
        logger.error(errorLog);

        return new ResponseEntity<>(errorLog, ex.getErrorCode().getHttpStatus());

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


    @org.springframework.web.bind.annotation.ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleBadRequestException(DataIntegrityViolationException ex) {
        return new ResponseEntity<>(ex.getMostSpecificCause().getMessage(), HttpStatus.BAD_REQUEST);

    }
}
