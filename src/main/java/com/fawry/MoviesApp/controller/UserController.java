package com.fawry.MoviesApp.controller;

import com.fawry.MoviesApp.dto.AuthResponse;
import com.fawry.MoviesApp.dto.LoginRequest;
import com.fawry.MoviesApp.dto.UserRegisterDto;
import com.fawry.MoviesApp.model.User;
import com.fawry.MoviesApp.repository.UserRepository;
import com.fawry.MoviesApp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/users",consumes = "application/json")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping("/register")
    @Operation(summary = "User Account Register",description = "User Registration")
    public ResponseEntity<User> userRegister(@RequestBody UserRegisterDto userRegisterDto) {
        return new ResponseEntity<>(userService.userRegister(userRegisterDto),HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "User login ",description = "User login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        return new ResponseEntity<>(userService.login(loginRequest),HttpStatus.OK);
    }


}
