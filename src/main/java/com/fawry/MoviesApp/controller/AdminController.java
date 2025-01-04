package com.fawry.MoviesApp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fawry.MoviesApp.dto.UserRegisterDto;
import com.fawry.MoviesApp.model.User;
import com.fawry.MoviesApp.service.OMDBService;
import com.fawry.MoviesApp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/dashboard")
public class AdminController {

    private final UserService userService;
    private final OMDBService omdbService;


    @PostMapping("/create")
    @Operation(summary = "Create admin", description = "Admin can create a new admin by providing necessary details.", tags = {"Admin Operations"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Admin Creation Successful", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))
            }),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(type = "String"))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(type = "String")))
    })
    public ResponseEntity<User> createAdmin(@Validated @RequestBody UserRegisterDto userRegisterDto) {
        return new ResponseEntity<>(userService.createAdmin(userRegisterDto), HttpStatus.CREATED);
    }

    @GetMapping("/movies/load")
    public ResponseEntity<String> moviesSearch(@RequestParam("title") String title) throws JsonProcessingException {
        return new ResponseEntity<>(omdbService.searchMovies(title), HttpStatus.OK);
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }

}
