package com.fawry.MoviesApp.controller;

import com.fawry.MoviesApp.dto.AuthResponse;
import com.fawry.MoviesApp.dto.LoginRequest;
import com.fawry.MoviesApp.dto.UserRegisterDto;
import com.fawry.MoviesApp.enums.VerificationStatus;
import com.fawry.MoviesApp.model.User;
import com.fawry.MoviesApp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/users",consumes = "application/json")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping("/register")
    @Operation(summary = "User Account Register",description = "User Registration",tags = {"User Operations"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",description = "User Register Successful",content = {
                    @Content(mediaType = "application/json",schema = @Schema(implementation = User.class))
            }),
            @ApiResponse(responseCode = "400",description = "Bad Request",content = @Content(schema = @Schema(type = "String"))),
    })
    public ResponseEntity<User> userRegister(@RequestBody UserRegisterDto userRegisterDto) {
        return new ResponseEntity<>(userService.userRegister(userRegisterDto),HttpStatus.CREATED);
    }

//    @GetMapping("/verify/account/{verificationCode}")
//    public ResponseEntity<String> verifyAccount(@PathVariable String verificationCode) {
//
//        VerificationStatus status = userService.verifyUserAccount(verificationCode);
//        return new ResponseEntity<>(userService.verifyUserAccount(verificationCode),HttpStatus.OK);
//    }



    @PostMapping("/login")
    @Operation(summary = "User login ",description = "User Can Login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        return new ResponseEntity<>(userService.login(loginRequest),HttpStatus.OK);
    }




}
