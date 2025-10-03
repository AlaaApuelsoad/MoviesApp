package com.fawry.MoviesApp.filters;

import com.fawry.MoviesApp.context.UserContextHolder;
import com.fawry.MoviesApp.utils.SystemUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


@Component
@Order(value = 1)
public class CorrelationIdFilter extends OncePerRequestFilter {

    private static final String CORRELATION_ID = "X-CORRELATION-ID";
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        String correlationId = request.getHeader(CORRELATION_ID);

        try {
            if (correlationId == null) {

                correlationId = SystemUtils.generateUUIDCode();
                request.setAttribute("X-CORRELATION-ID", correlationId);
                response.setHeader("X-CORRELATION-ID", correlationId);
                MDC.put("X-Correlation-ID", correlationId);
                filterChain.doFilter(request, response);

            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            MDC.clear();
            UserContextHolder.clearRequestContext();
        }

    }
}
