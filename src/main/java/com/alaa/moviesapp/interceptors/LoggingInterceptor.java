package com.alaa.moviesapp.interceptors;

import com.alaa.moviesapp.constants.AppConstant;
import com.alaa.moviesapp.context.LogContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class LoggingInterceptor implements org.springframework.web.servlet.HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);
    private final ObjectMapper mapper;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        return true;
    }

    @Override
    public void postHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, Exception ex) {

        LogContext logContext;
        if (ex == null){
            logContext = LogContext.builder()
                    .timestamp(Instant.now())
                    .correlationId(MDC.get(AppConstant.X_CORRELATION_ID))
                    .logger(logger.getName())
                    .thread(Thread.currentThread().getName())
                    .httpMethod(request.getMethod())
                    .uri(request.getRequestURI())
                    .responseStatus(response.getStatus())
                    .responseTimMs(System.currentTimeMillis() - Long.parseLong(MDC.get(AppConstant.REQUEST_START_TIME)))
                    .build();

            String logContextString = mapper.writeValueAsString(logContext);
            logger.info(logContextString);
        }
    }
}
