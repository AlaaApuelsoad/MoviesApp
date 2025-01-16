package com.fawry.MoviesApp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fawry.MoviesApp.dao.OMDBDao;
import com.fawry.MoviesApp.dto.CustomPageDto;
import com.fawry.MoviesApp.dto.MovieInfoDetails;
import com.fawry.MoviesApp.dto.MovieListInfo;
import com.fawry.MoviesApp.dto.ResponseMessage;
import com.fawry.MoviesApp.enums.ErrorCode;
import com.fawry.MoviesApp.exception.CustomException;
import com.fawry.MoviesApp.mapper.MovieMapper;
import com.fawry.MoviesApp.mapper.PageMapper;
import com.fawry.MoviesApp.model.Movie;
import com.fawry.MoviesApp.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class MovieService {

    public final OMDBDao omdbDao;
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;
    private final PageMapper pageMapper;


    @Transactional
    public Movie addMovie(String imdbId) throws JsonProcessingException {
        if (movieRepository.getMovieByImdbId(imdbId).isPresent()) {
            throw new CustomException(ErrorCode.MOVIE_EXISTS);
        }

        String movieResponse = omdbDao.getMovieByImdbId(imdbId);
        Movie movie = movieMapper.mapToMovie(movieResponse);
        return movieRepository.save(movie);
    }

    @Transactional
    public ResponseMessage deleteMovieByImdbId(String imdbId) {
        Movie movie = movieRepository.findByIdImdbId(imdbId).orElseThrow(
                () -> new CustomException(ErrorCode.MOVIE_NOT_FOUND)
        );
        if (movie.isDeleted()){
            throw new CustomException(ErrorCode.ALREADY_DELETED);
        }

        movie.setDeleted(true);
        movieRepository.save(movie);
        return new ResponseMessage("Movie deleted successfully");
    }

    public MovieInfoDetails getMovieInfoByImdbID(String imdbID) {

        return movieRepository.getMovieInfoDetailsByImdbId(imdbID).orElseThrow(
                () -> new CustomException(ErrorCode.MOVIE_NOT_FOUND)
        );
    }

    public MovieInfoDetails getMovieByImdbId(String imdbId) {

        Movie movie = movieRepository.getMovieByImdbId(imdbId).orElseThrow(
                () -> new CustomException(ErrorCode.MOVIE_NOT_FOUND)
        );
        return movieMapper.mapToMovieInfoDetails(movie);
    }

    public CustomPageDto<MovieListInfo> searchMovies(String keyword, Pageable pageable) {
        Page<Movie> moviePage = movieRepository.searchForMovie(keyword,pageable);
        if (moviePage.getContent().isEmpty()) {
            throw new CustomException(ErrorCode.NO_DATA_FOUND);
        }
        return pageMapper.customPageDto(moviePage.map(movieMapper::mapToMovieInfoList));
    }

    public CustomPageDto<MovieListInfo> getMoviePage(Pageable pageable) {
        Page<Movie> moviePage = movieRepository.findAll(pageable);
        if (moviePage.getContent().isEmpty()) {
            throw new CustomException(ErrorCode.NO_DATA_FOUND);
        }
        return pageMapper.customPageDto(moviePage.map(movieMapper::mapToMovieInfoList));
    }
}
