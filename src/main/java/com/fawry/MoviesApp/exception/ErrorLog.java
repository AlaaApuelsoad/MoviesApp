package com.fawry.MoviesApp.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Component
public class ErrorLog {

    @JsonIgnore
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp = LocalDateTime.now();

    private String level;
    @JsonIgnore
    private String logger;

    private String message;

    private String code;

    @JsonIgnore
    private String errorTrace;

    @JsonIgnore
    private String requestedUrl;

    @Override
    public String toString() {
        return "ErrorLog{" +
                "timestamp=" + timestamp +
                ", level='" + level + '\'' +
                ", logger='" + logger + '\'' +
                ", message='" + message + '\'' +
                ", errorTrace='" + errorTrace + '\'' +
                ", requestedUrl='" + requestedUrl + '\'' +
                '}';
    }
}
