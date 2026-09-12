package com.alaa.MoviesApp.controller;

import com.alaa.MoviesApp.constants.Navigation;
import com.alaa.MoviesApp.dto.AppResponse;
import com.alaa.MoviesApp.dto.UserRegisterDto;
import com.alaa.MoviesApp.dto.UserRegisterResponse;
import com.alaa.MoviesApp.service.UserService;
import com.alaa.MoviesApp.service.VerifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tools.jackson.core.JacksonException;

@RestController
@RequestMapping(Navigation.USER_ROUTE)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final VerifyService verifyService;


    @PostMapping(value = "/register")
    public ResponseEntity<AppResponse<UserRegisterResponse>> userRegister(@Validated @RequestBody UserRegisterDto userRegisterDto) throws JacksonException {
        return new ResponseEntity<>(userService.userRegister(userRegisterDto),HttpStatus.CREATED);
    }

    @GetMapping(value = "/verify/account/{verificationCode}")
    public ResponseEntity<AppResponse<String>> verifyAccount(@PathVariable String verificationCode) {
        AppResponse<String> response = verifyService.verifyUser(verificationCode);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
