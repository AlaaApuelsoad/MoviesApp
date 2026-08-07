package com.alaa.moviesapp.controller;

import com.alaa.moviesapp.constants.Navigation;
import com.alaa.moviesapp.dto.AppResponse;
import com.alaa.moviesapp.dto.AuthResponse;
import com.alaa.moviesapp.dto.LoginRequest;
import com.alaa.moviesapp.service.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Navigation.AUTH_ROUTE)
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AppResponse<AuthResponse>> login(@RequestBody LoginRequest loginRequest) {
        return new ResponseEntity<>(authenticationService.login(loginRequest), HttpStatus.OK);
    }


    @PostMapping("/login/cookie")
    public ResponseEntity<AppResponse<AuthResponse>> loginCookie(@RequestBody LoginRequest loginRequest, HttpServletResponse response){
        AppResponse<AuthResponse> appResponse = authenticationService.login(loginRequest);
        ResponseCookie cookie = ResponseCookie.from("SEC-TOKEN",appResponse.getData().getToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite(Cookie.SameSite.STRICT.toString())
                .maxAge(15)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE,cookie.toString());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /*
    httpOnly: Ensures cookies are inaccessible to client-side JavaScript (like via document.cookie),
    protecting them from XSS attacks and restricting their use to server-side processes only during HTTP(S) requests.

    secure: Ensures cookies are transmitted only over HTTPS, preventing interception during transit
    and safeguarding sensitive information from man-in-the-middle attacks.

    path: Specifies the cookie's scope (/ makes it accessible across the entire domain).

    maxAge: Defines the cookie's expiration time.

    sameSite: Used to control when the cookie is sent with requests originating from different sites.
    It helps protect against Cross-Site Request Forgery (CSRF) attacks.
     */

}
