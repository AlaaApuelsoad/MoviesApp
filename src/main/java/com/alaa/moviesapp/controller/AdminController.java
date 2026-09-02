package com.alaa.moviesapp.controller;

import com.alaa.moviesapp.constants.Navigation;
import com.alaa.moviesapp.dto.*;
import com.alaa.moviesapp.dto.IntegrationSearch;
import com.alaa.moviesapp.dto.UserRegisterDto;
import com.alaa.moviesapp.dto.UserRegisterResponse;
import com.alaa.moviesapp.model.Movie;
import com.alaa.moviesapp.service.MovieService;
import com.alaa.moviesapp.service.IntegrationService;
import com.alaa.moviesapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tools.jackson.core.JacksonException;

@RestController
@RequiredArgsConstructor
@RequestMapping(Navigation.ADMIN_DASHBOARD_ROUTE)
public class AdminController {

    private final UserService userService;
    private final IntegrationService integrationService;
    private final MovieService movieService;

    @PostMapping("/create/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AppResponse<UserRegisterResponse>> createAdmin(@Validated @RequestBody UserRegisterDto userRegisterDto) throws JacksonException {
        return new ResponseEntity<>(userService.createAdmin(userRegisterDto), HttpStatus.CREATED);
    }

    @GetMapping("/movies/load/list")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AppResponse<IntegrationSearch>> moviesListSearch
            (@RequestParam(value = "title", required = false, defaultValue = "Dark") String title,
             @RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber) throws JacksonException {
        return new ResponseEntity<>(integrationService.searchMovies(title, pageNumber), HttpStatus.OK);
    }

    @GetMapping("/add/movie/{imdbID}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AppResponse<Movie>> addMovie(@PathVariable String imdbID) throws JacksonException {
        return new ResponseEntity<>(movieService.addMovie(imdbID), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/movie/{imdbId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AppResponse<?>> deleteMovieFromDB(@PathVariable String imdbId) {
        return new ResponseEntity<>(movieService.deleteMovieByImdbId(imdbId), HttpStatus.OK);
    }

}
