package com.fawry.MoviesApp.utils;

import com.fawry.MoviesApp.enums.ErrorCode;
import com.fawry.MoviesApp.exception.CustomException;
import com.fawry.MoviesApp.model.Movie;
import com.fawry.MoviesApp.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MovieUtils {

    private final MovieRepository movieRepository;


    public Movie getMovie(String imdbID){

        return movieRepository.getMovieByImdbId(imdbID).orElseThrow(
                () -> new CustomException(ErrorCode.MOVIE_NOT_FOUND)
        );
    }
}
