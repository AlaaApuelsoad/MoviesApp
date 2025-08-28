package com.fawry.MoviesApp.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


@Order(value = 1)
@Component
public class RequestIdFilter extends OncePerRequestFilter {
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        String contextId = RequestContext.generateUUID();

        try {
            if (request.getHeader("Context-UUID") == null) {
                RequestContext.setRequestContext(contextId);
                response.setHeader("Context-UUID", contextId);
                filterChain.doFilter(request, response); //pass to the next filter in chain
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            RequestContext.clearRequestContext();//clear context
        }

    }
}
