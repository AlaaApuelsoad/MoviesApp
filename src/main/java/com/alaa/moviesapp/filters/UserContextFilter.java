package com.alaa.moviesapp.filters;

import com.alaa.moviesapp.context.LoggedInUserContext;
import com.alaa.moviesapp.context.UserContextHolder;
import com.alaa.moviesapp.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// @Component
// @Order(3)
public class UserContextFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()){
            User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            // fill user context
            LoggedInUserContext context = LoggedInUserContext.builder()
                    .userId(loggedInUser.getId())
                    .userName(loggedInUser.getUsername())
                    .role(loggedInUser.getRole().getRoleName())
                    .type(loggedInUser.getType())
                    .email(loggedInUser.getEmail())
                    .build();

            UserContextHolder.setLoggedInUserContext(context);
        }
        try {
            filterChain.doFilter(request,response);
        }finally {
            UserContextHolder.clearRequestContext();
        }
    }
}
