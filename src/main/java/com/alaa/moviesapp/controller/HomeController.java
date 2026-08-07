package com.alaa.moviesapp.controller;

import com.alaa.moviesapp.constants.Navigation;
import com.alaa.moviesapp.dto.AppResponse;
import com.alaa.moviesapp.dto.MovieInfoDetails;
import com.alaa.moviesapp.enums.ErrorCode;
import com.alaa.moviesapp.exception.BusinessException;
import com.alaa.moviesapp.model.Movie;
import com.alaa.moviesapp.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tools.jackson.core.JacksonException;

import java.util.List;


@RestController
@RequestMapping(value = Navigation.HOME_ROUTE)
@RequiredArgsConstructor
public class HomeController {

    private final MovieService movieService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('MEMBER','ADMIN')")
    public ResponseEntity<AppResponse<List<Movie>>> getAllMovies(@RequestParam(defaultValue = "1") int pageNumber) {
        return new ResponseEntity<>(movieService.getAllMovies(pageNumber),HttpStatus.OK);
    }

    @GetMapping("/movies/get/{imdbID}")
    public ResponseEntity<AppResponse<MovieInfoDetails>> getMovieByImdbID(@PathVariable String imdbID) throws JacksonException {
        return new ResponseEntity<>(movieService.getMovieByImdbId(imdbID),HttpStatus.OK);
    }

    @GetMapping("/movies/search")
    public ResponseEntity<AppResponse<List<Movie>>>searchForMovies(
            @RequestParam("keyword") String keyword, @RequestParam(defaultValue = "1") int pageNumber) {
        return new ResponseEntity<>(movieService.searchMovies(keyword,pageNumber),HttpStatus.OK);
    }

    @GetMapping("/exc")
    public ResponseEntity<AppResponse<?>> throwException() {
        throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

}
