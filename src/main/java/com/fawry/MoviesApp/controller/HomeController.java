package com.fawry.MoviesApp.controller;

import com.fawry.MoviesApp.dto.MovieInfoDetails;
import com.fawry.MoviesApp.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fawry/home/")
@RequiredArgsConstructor
public class HomeController {

    private final MovieService movieService;

    @GetMapping("/movies/get/{imdbID}")
    public ResponseEntity<MovieInfoDetails> getMovieDetails(@PathVariable("imdbID") String imdbID){
        return new ResponseEntity<>(movieService.getMovieInfoByImdbID(imdbID),HttpStatus.OK);
    }


}
