package com.fawry.MoviesApp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fawry.MoviesApp.dto.*;
import com.fawry.MoviesApp.model.Movie;
import com.fawry.MoviesApp.model.User;
import com.fawry.MoviesApp.service.LoginService;
import com.fawry.MoviesApp.service.MovieService;
import com.fawry.MoviesApp.dao.OMDBDao;
import com.fawry.MoviesApp.service.UserService;
import com.fawry.MoviesApp.constants.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppConstants.ADMIN_DASHBOARD_ROUTE)
@Tag(name = "Admin Dashboard", description = "Endpoints for managing the admin dashboard functionalities.")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminController {

    private final UserService userService;
    private final OMDBDao omdbDao;
    private final MovieService movieService;
    private final LoginService loginService;


    @PostMapping("/auth/login")
    @Operation(summary = "Admin Authentication",
            description = "Allows an admin to log in by providing their credentials. Returns an authentication token upon successful login.",
            responses = {
                    @ApiResponse(
                            description = "Login successful. Returns authentication token.", responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))
                    ),
                    @ApiResponse(description = "Invalid credentials or unauthorized access.", responseCode = "401",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(description = "Bad Request - Missing or invalid fields in the login request.", responseCode = "400",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        return new ResponseEntity<>(loginService.login(loginRequest), HttpStatus.OK);
    }

    @PostMapping("/create/admin")
    @Operation(summary = "Create admin", description = "Admin can create a new admin by providing necessary details.", tags = {"Admin Operations"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Admin Creation Successful", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))
            }),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(type = "String"))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(type = "String")))
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserRegisterResponse> createAdmin(@Validated @RequestBody UserRegisterDto userRegisterDto) {
        return new ResponseEntity<>(userService.createAdmin(userRegisterDto), HttpStatus.CREATED);
    }

    @GetMapping("/movies/load/list")
    @Operation(
            summary = "Load a list of movies from OMDB API",
            description = "This endpoint allows the admin to fetch a list of movies from the OMDB API by providing a movie title and an optional page number. If no title is provided, it defaults to 'Dark'. If no page is provided, it defaults to the first page.",
            responses = {
                    @ApiResponse(description = "Successfully fetched the movie list",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MovieSearchResponse.class))),
                    @ApiResponse(description = "Bad Request - Invalid parameters", responseCode = "400"),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500")
            })
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<MovieSearchResponse> moviesListSearch
            (@RequestParam(value = "title", required = false, defaultValue = "Dark") String title,
             @RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber) throws JsonProcessingException {
        return new ResponseEntity<>(omdbDao.searchMovies(title, pageNumber), HttpStatus.OK);
    }

    @GetMapping("/add/movie/{imdbID}")
    @Operation(
            summary = "Add a movie to the system using its IMDB ID",
            description = "This endpoint allows the admin to add a movie to the system by providing the IMDB ID. The movie will be fetched from the OMDB API and saved into the database.",
            responses = {
                    @ApiResponse(description = "Successfully added the movie to the system",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Movie.class))),
                    @ApiResponse(description = "Movie not found or invalid IMDB ID", responseCode = "404"),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500")
            }
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Movie> addMovieTODB(@PathVariable("imdbID") String imdbID) throws JsonProcessingException {
        return new ResponseEntity<>(movieService.addMovie(imdbID), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/movie/{imdbId}")
    @Operation(
            summary = "Delete a movie by its IMDB ID",
            description = "This endpoint allows the admin to delete a movie from the system using its IMDB ID. The movie is identified in the database by the provided IMDB ID.",
            responses = {
                    @ApiResponse(description = "Successfully deleted the movie", responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseMessage.class))),
                    @ApiResponse(description = "Movie not found in the system", responseCode = "404"),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500")})
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseMessage> deleteMovieFromDB(@PathVariable("imdbId") String imdbId) {
        return new ResponseEntity<>(movieService.deleteMovieByImdbId(imdbId), HttpStatus.OK);
    }


    @GetMapping("/hello")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String hello() {
        return "Hello World";
    }

}
