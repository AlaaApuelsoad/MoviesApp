package com.fawry.MoviesApp.jwt;

import com.fawry.MoviesApp.enums.ErrorCode;
import com.fawry.MoviesApp.exception.CustomException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {


    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token;
        String userIdentifier;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            response.sendError(HttpServletResponse.SC_NOT_FOUND);
//            filterChain.doFilter(request, response);
            handleJwtException(response,"No Token",HttpServletResponse.SC_UNAUTHORIZED);
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
            handleJwtException(response, "Token Expired", HttpServletResponse.SC_UNAUTHORIZED);
//            throw new CustomException(ErrorCode.TOKEN_EXPIRED);

        } catch (SignatureException e) {
            handleJwtException(response, "Invalid Token Signature", HttpServletResponse.SC_UNAUTHORIZED);
//            throw new CustomException(ErrorCode.INVALID_TOKEN_SIGNATURE);
        }
    }


    private void handleJwtException(HttpServletResponse response, String message, int status) throws IOException {

        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String jsonResponse = String.format("{\"error\": \"%s\", \"message\": \"%s\"}", "Authentication error", message);
        response.getWriter().write(jsonResponse);
    }
}
