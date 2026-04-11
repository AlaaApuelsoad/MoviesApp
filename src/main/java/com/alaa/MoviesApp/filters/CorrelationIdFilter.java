package com.alaa.MoviesApp.filters;

import com.alaa.MoviesApp.constants.AppConstant;
import com.alaa.MoviesApp.context.UserContextHolder;
import com.alaa.MoviesApp.utils.SystemUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@Order(value = 1)
public class CorrelationIdFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String correlationId = request.getHeader(AppConstant.X_CORRELATION_ID);

        if (correlationId == null || correlationId.isBlank()) {
            correlationId = SystemUtils.generateUUIDCode();
        }

        try {

            MDC.put(AppConstant.X_CORRELATION_ID, correlationId);
            MDC.put(AppConstant.REQUEST_START_TIME, String.valueOf(System.currentTimeMillis()));
            request.setAttribute(AppConstant.X_CORRELATION_ID, correlationId);
            response.setHeader(AppConstant.X_CORRELATION_ID, correlationId);
            filterChain.doFilter(request, response);

        } finally {
            MDC.clear();
            UserContextHolder.clearRequestContext();
        }

    }
}
