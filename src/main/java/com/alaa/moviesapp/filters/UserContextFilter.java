package com.alaa.MoviesApp.filters;

import com.alaa.MoviesApp.constants.AppConstant;
import com.alaa.MoviesApp.context.LoggedInUserContext;
import com.alaa.MoviesApp.context.UserContextHolder;
import com.alaa.MoviesApp.securityconfiguration.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(3)
public class UserContextFilter extends OncePerRequestFilter {

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().startsWith("/auth/login");
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userAgent = request.getHeader(AppConstant.USER_AGENT);

        try {
            if (authentication != null
                    && authentication.isAuthenticated()
                    && authentication.getPrincipal() instanceof CustomUserDetails loggedInUser){ //check and create
                LoggedInUserContext context = LoggedInUserContext.builder()
                        .userId(loggedInUser.getId())
                        .userName(loggedInUser.getUsername())
                        .role(loggedInUser.getRole())
                        .email(loggedInUser.getEmail())
                        .userAgent(userAgent)
                        .build();

                UserContextHolder.setLoggedInUserContext(context);
            }
            filterChain.doFilter(request,response);
        } finally {
            UserContextHolder.clearRequestContext();
        }
    }
}
