package com.alaa.MoviesApp.service;

import com.alaa.MoviesApp.dto.*;
import com.alaa.MoviesApp.mapper.PaginationMetaDataMapper;
import com.alaa.MoviesApp.utils.AppResponseBuilder;
import com.alaa.MoviesApp.utils.SystemUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.alaa.MoviesApp.enums.ErrorCode;
import com.alaa.MoviesApp.exception.BusinessException;
import com.alaa.MoviesApp.mapper.OmdbMovieMapper;
import com.alaa.MoviesApp.model.Movie;
import com.alaa.MoviesApp.model.User;
import com.alaa.MoviesApp.repository.MemberRatingRepository;
import com.alaa.MoviesApp.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class MovieService {

    public final OmdbIntegrationService omdbIntegrationService;
    private final MovieRepository movieRepository;
    private final OmdbMovieMapper omdbMovieMapper;
    private final MemberRatingRepository memberRatingRepository;
    private final UserService userService;
    private final SystemUtils systemUtils;


    @Transactional
    public AppResponse<Movie> addMovie(String imdbId) throws JsonProcessingException {
        if (movieRepository.getMovieByImdbId(imdbId).isPresent()) {
            throw new BusinessException(ErrorCode.MOVIE_EXISTS);
        }

        String movieResponse = omdbIntegrationService.getMovieByImdbId(imdbId);
        Movie movie = omdbMovieMapper.mapToMovie(movieResponse);
        return AppResponseBuilder.buildResponse(true,movieRepository.save(movie),"Movie Added Successfully",
                HttpStatus.OK,null,null);
    }

    @Transactional
    public AppResponse<?> deleteMovieByImdbId(String imdbId) {
        Movie movie = movieRepository.findByIdImdbId(imdbId).orElseThrow(
                () -> new BusinessException(ErrorCode.MOVIE_NOT_FOUND)
        );
        if (movie.isDeleted()) {
            throw new BusinessException(ErrorCode.ALREADY_DELETED);
        }

        movie.setDeleted(true);
        movieRepository.save(movie);
        return AppResponseBuilder.buildResponse(true,null,"Movie Deleted Successfully",
                HttpStatus.OK,null,null);
    }


    @Transactional
    public AppResponse<MovieInfoDetails> getMovieByImdbId(String imdbId) throws JsonProcessingException {

        Movie movie = movieRepository.getMovieByImdbId(imdbId).orElseThrow(
                () -> new BusinessException(ErrorCode.MOVIE_NOT_FOUND)
        );
        MovieInfoDetails movieInfoDetails = omdbMovieMapper.mapToMovieInfoDetails(movie);
        movieInfoDetails.setMemberRating(getMemberRatingForMovie(imdbId));
        movieInfoDetails.setAverageRating(movie.getAverageRating());
        return AppResponseBuilder.buildResponse(
                true,movieInfoDetails,"Movie details fetched successfully",HttpStatus.OK,
                null,null);

    }

    @Transactional
    public AppResponse<List<Movie>> searchMovies(String keyword, int pageNumber) {
        Page<Movie> moviePage = movieRepository.searchForMovie(keyword, systemUtils.buildPageableObj(pageNumber));
        if (moviePage.getContent().isEmpty()) {
            throw new BusinessException(ErrorCode.NO_DATA_FOUND);
        }
        MetaData metaData = PaginationMetaDataMapper.fromPage(moviePage);
        return AppResponseBuilder.buildResponse(
                true,moviePage.getContent(),"Movies fetched successfully",HttpStatus.OK,null,metaData
        );
    }

    @Transactional
    public AppResponse<List<Movie>> getAllMovies(int pageNumber) {
        Page<Movie> movies = movieRepository.getAllMovies(systemUtils.buildPageableObj(pageNumber));
        MetaData metaData = PaginationMetaDataMapper.fromPage(movies);
        return AppResponseBuilder.buildResponse(
                true, movies.getContent(), "Movies fetched successfully", HttpStatus.OK,
                null, metaData
        );
    }

    public int getMemberRatingForMovie(String imdbId) {
        String username = Objects.requireNonNull(User.getCredentials()).getUsername();
        User user = userService.getUser(username);
        Integer rating = memberRatingRepository.getMemberRatingForAMovie(imdbId, user.getId());
        return rating != null ? rating : 0;
    }
}
