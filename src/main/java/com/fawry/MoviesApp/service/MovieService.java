package com.fawry.MoviesApp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fawry.MoviesApp.dao.OMDBDao;
import com.fawry.MoviesApp.dto.ResponseMessage;
import com.fawry.MoviesApp.mapper.MovieMapper;
import com.fawry.MoviesApp.model.Movie;
import com.fawry.MoviesApp.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MovieService {

    public final OMDBDao omdbDao;
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;


    @Transactional
    public Movie addMovie(String imdbId) throws JsonProcessingException {

        String movieResponse = omdbDao.getMovieByImdbId(imdbId);
        Movie movie = movieMapper.mapToMovie(movieResponse);
        return movieRepository.save(movie);
    }

    @Transactional
    public ResponseMessage deleteMovieByImdbId(String imdbId) {
        Movie movie = movieRepository.findByIdImdbId(imdbId).orElseThrow();
        movie.setDeleted(true);
        movieRepository.save(movie);
        return new ResponseMessage("Movie deleted successfully");
    }
}
