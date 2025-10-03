package com.fawry.MoviesApp.controller;

import com.fawry.MoviesApp.constants.Navigation;
import com.fawry.MoviesApp.dto.AuthResponse;
import com.fawry.MoviesApp.dto.LoginRequest;
import com.fawry.MoviesApp.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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


    //normal login
    @PostMapping("/login")
    @Operation(summary = "User login ",description = "User Can Login")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "Admin login successfully"),
            @ApiResponse(responseCode = "201",description = "Invalid Credentials"),
            @ApiResponse(responseCode = "500",description = "Internal Server Error")
    })
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        return new ResponseEntity<>(authenticationService.login(loginRequest), HttpStatus.OK);
    }


    //cookie
    @PostMapping("/login/cookie")
    public ResponseEntity<Object> loginCookie(@RequestBody LoginRequest loginRequest, HttpServletResponse response){
        AuthResponse authResponse = authenticationService.login(loginRequest);
        ResponseCookie cookie = ResponseCookie.from("SEC-TOKEN",authResponse.getToken())
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
