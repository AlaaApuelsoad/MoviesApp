package com.alaa.MoviesApp.controller;

import com.alaa.MoviesApp.dto.IntegrationSearch;
import com.alaa.MoviesApp.dto.UserRegisterDto;
import com.alaa.MoviesApp.dto.UserRegisterResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.alaa.MoviesApp.dto.*;
import com.alaa.MoviesApp.model.Movie;
import com.alaa.MoviesApp.service.MovieService;
import com.alaa.MoviesApp.service.OmdbIntegrationService;
import com.alaa.MoviesApp.service.UserService;
import com.alaa.MoviesApp.constants.Navigation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(Navigation.ADMIN_DASHBOARD_ROUTE)
public class AdminController {

    private final UserService userService;
    private final OmdbIntegrationService omdbIntegrationService;
    private final MovieService movieService;

    @PostMapping("/create/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AppResponse<UserRegisterResponse>> createAdmin(@Validated @RequestBody UserRegisterDto userRegisterDto) throws JsonProcessingException {
        return new ResponseEntity<>(userService.createAdmin(userRegisterDto), HttpStatus.CREATED);
    }

    @GetMapping("/movies/load/list")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AppResponse<IntegrationSearch>> moviesListSearch
            (@RequestParam(value = "title", required = false, defaultValue = "Dark") String title,
             @RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber) throws JsonProcessingException {
        return new ResponseEntity<>(omdbIntegrationService.searchMovies(title, pageNumber), HttpStatus.OK);
    }

    @GetMapping("/add/movie/{imdbID}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AppResponse<Movie>> addMovie(@PathVariable String imdbID) throws JsonProcessingException {
        return new ResponseEntity<>(movieService.addMovie(imdbID), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/movie/{imdbId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AppResponse<?>> deleteMovieFromDB(@PathVariable String imdbId) {
        return new ResponseEntity<>(movieService.deleteMovieByImdbId(imdbId), HttpStatus.OK);
    }

}
