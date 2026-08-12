package com.alaa.moviesapp.filters;

import com.alaa.moviesapp.constants.AppConstant;
import com.alaa.moviesapp.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Order(value = 2)
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token;
        String userIdentifier;


        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            token = authHeader.substring(7);
            userIdentifier = jwtService.extractUserIdentifier(token);

            if (userIdentifier != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userIdentifier);
                if (jwtService.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken
                            (userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(userDetails);
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            handleJwtException(response, "Token Expired for RequestID: " + MDC.get(AppConstant.X_CORRELATION_ID));

        } catch (SignatureException e) {
            handleJwtException(response, "Invalid Token Signature for RequestID: " + MDC.get(AppConstant.X_CORRELATION_ID));
        }
    }


    private void handleJwtException(HttpServletResponse response, String message) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String jsonResponse = String.format("{\"error\": \"%s\", \"message\": \"%s\"}", "Authentication error", message);
        response.getWriter().write(jsonResponse);
    }
}
