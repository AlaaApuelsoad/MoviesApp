package com.alaa.MoviesApp.controller;

import com.alaa.MoviesApp.dto.MovieSearchResponse;
import com.alaa.MoviesApp.dto.ResponseMessage;
import com.alaa.MoviesApp.dto.UserRegisterDto;
import com.alaa.MoviesApp.dto.UserRegisterResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.alaa.MoviesApp.dto.*;
import com.alaa.MoviesApp.model.Movie;
import com.alaa.MoviesApp.model.User;
import com.alaa.MoviesApp.service.MovieService;
import com.alaa.MoviesApp.service.OmdbIntegrationService;
import com.alaa.MoviesApp.service.UserService;
import com.alaa.MoviesApp.constants.Navigation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(Navigation.ADMIN_DASHBOARD_ROUTE)
@Tag(name = "Admin Dashboard", description = "Endpoints for managing the admin dashboard functionalities.")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminController {

    private final UserService userService;
    private final OmdbIntegrationService omdbIntegrationService;
    private final MovieService movieService;

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
        return new ResponseEntity<>(omdbIntegrationService.searchMovies(title, pageNumber), HttpStatus.OK);
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
