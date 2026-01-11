package com.fawry.MoviesApp.filters;

import com.fawry.MoviesApp.context.LoggedInUserContext;
import com.fawry.MoviesApp.context.UserContextHolder;
import com.fawry.MoviesApp.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//@Component
//@Order(3)
public class UserContextFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()){
            User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            //fill user context
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
