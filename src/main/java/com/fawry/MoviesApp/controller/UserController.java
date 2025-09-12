package com.fawry.MoviesApp.controller;

import com.fawry.MoviesApp.dto.*;
import com.fawry.MoviesApp.enums.VerificationStatus;
import com.fawry.MoviesApp.model.User;
import com.fawry.MoviesApp.service.LoginService;
import com.fawry.MoviesApp.service.UserService;
import com.fawry.MoviesApp.service.VerifyService;
import com.fawry.MoviesApp.constants.AppConstants;
import freemarker.template.TemplateException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(AppConstants.USER_ROUTE)
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserService userService;
    private final VerifyService verifyService;
    private final LoginService loginService;


    @PostMapping(value = "/register",consumes = "application/json")
    @Operation(summary = "User Account Register",description = "User Registration",tags = {"User Operations"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",description = "User Register Successful",content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400",description = "Bad Request",content = @Content(schema = @Schema(type = "String"))),
    })
    public ResponseEntity<UserRegisterResponse> userRegister(@Validated @RequestBody UserRegisterDto userRegisterDto) throws MessagingException, TemplateException, IOException {
        return new ResponseEntity<>(userService.userRegister(userRegisterDto),HttpStatus.CREATED);
    }

    @GetMapping(value = "/verify/account/{verificationCode}")
    @Operation(summary = "User Account Verification",description = "User send verification code to verify his account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202",description = "Account Verified successfully",content = @Content(mediaType = "application/json",schema = @Schema(implementation = ResponseMessage.class))),
            @ApiResponse(responseCode = "410",description = "Verified Failed Code Expire",content = @Content(mediaType = "application/json",schema = @Schema(implementation = ResponseMessage.class))),
            @ApiResponse(responseCode = "400",description = "Verified Failed Invalid Code",content = @Content(mediaType = "application/json",schema = @Schema(implementation = ResponseMessage.class))),
            @ApiResponse(responseCode = "404",description = "Verified Failed Code Not Found",content = @Content(mediaType = "application/json",schema = @Schema(implementation = ResponseMessage.class))),
    })
    public ResponseEntity<ResponseMessage> verifyAccount(@PathVariable("verificationCode") String verificationCode) {

        VerificationStatus status = verifyService.verifyUser(verificationCode);

        return switch (status){
            case SUCCESS -> ResponseEntity.status(HttpStatus.ACCEPTED).
                    body(new ResponseMessage("Account verified successfully"));
            case EXPIRED -> ResponseEntity.status(HttpStatus.GONE).body(new ResponseMessage("Code expired"));
            case INVALID_CODE -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("Invalid verification code"));
            case CODE_NOT_FOUND -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage("Code not found"));
        };

    }


    @PostMapping("/auth/login")
    @Operation(summary = "User login ",description = "User Can Login")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "Admin login successfully"),
            @ApiResponse(responseCode = "201",description = "Invalid Credentials"),
            @ApiResponse(responseCode = "500",description = "Internal Server Error")
    })
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        return new ResponseEntity<>(loginService.login(loginRequest),HttpStatus.OK);
    }



}
