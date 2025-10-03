package com.fawry.MoviesApp.interceptors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fawry.MoviesApp.context.LogContext;
import com.fawry.MoviesApp.context.UserContextHolder;
import com.fawry.MoviesApp.utils.EnvUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class LogInterceptor implements org.springframework.web.servlet.HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LogInterceptor.class);
    private final ObjectMapper mapper;
    private final EnvUtils envUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        LogContext logContext;
        if (ex == null){
            logContext = LogContext.builder()
                    .timestamp(LocalDateTime.now().toString())
                    .correlationId(MDC.get("X-Correlation-ID"))
                    .level("INFO")
                    .environment(envUtils.getActiveProfile())
                    .logger(logger.getName())
                    .thread(Thread.currentThread().getName())
                    .httpMethod(request.getMethod())
                    .uri(request.getRequestURI())
                    .responseStatus(response.getStatus())
                    .responseTimMs(System.currentTimeMillis() - Long.parseLong(MDC.get("Start-Time")))
                    .userId(UserContextHolder.getLoggedInUserContext().getUserId())
                    .userName(UserContextHolder.getLoggedInUserContext().getUserName())
                    .role(UserContextHolder.getLoggedInUserContext().getRole())
                    .build();


            logger.info(mapper.writeValueAsString(logContext));
        }
    }
}
