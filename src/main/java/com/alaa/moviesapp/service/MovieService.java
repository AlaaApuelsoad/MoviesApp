package com.alaa.moviesapp.service;

import com.alaa.moviesapp.dto.AppResponse;
import com.alaa.moviesapp.dto.MetaData;
import com.alaa.moviesapp.dto.MovieInfoDetails;
import com.alaa.moviesapp.enums.EntityString;
import com.alaa.moviesapp.enums.ErrorCode;
import com.alaa.moviesapp.exception.BusinessException;
import com.alaa.moviesapp.mapper.OmdbMovieMapper;
import com.alaa.moviesapp.mapper.PaginationMetaDataMapper;
import com.alaa.moviesapp.model.Movie;
import com.alaa.moviesapp.repository.MemberRatingRepository;
import com.alaa.moviesapp.repository.MovieRepository;
import com.alaa.moviesapp.utils.AppResponseBuilder;
import com.alaa.moviesapp.utils.SystemUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.core.JacksonException;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class MovieService {

    public final IntegrationService integrationService;
    private final MovieRepository movieRepository;
    private final OmdbMovieMapper omdbMovieMapper;
    private final MemberRatingRepository memberRatingRepository;
    private final UserService userService;
    private final SystemUtils systemUtils;


    @Transactional
    public AppResponse<Movie> addMovie(String imdbId) throws JacksonException {
        if (movieRepository.getMovieByImdbId(imdbId).isPresent()) {
            throw new BusinessException(ErrorCode.MOVIE_EXISTS);
        }

        String movieResponse = integrationService.getMovieByImdbId(imdbId);
        Movie movie = omdbMovieMapper.mapToMovie(movieResponse);
        Movie savedMovie = movieRepository.save(movie);
        return AppResponseBuilder.success(savedMovie,HttpStatus.CREATED,"entity.created.success",
                EntityString.MOVIE.getName());
    }

    @Transactional
    public AppResponse<Object> deleteMovieByImdbId(String imdbId) {
        Movie movie = movieRepository.findByIdImdbId(imdbId).orElseThrow(
                () -> new BusinessException(ErrorCode.MOVIE_NOT_FOUND)
        );
        if (movie.isDeleted()) {
            throw new BusinessException(ErrorCode.ALREADY_DELETED);
        }

        movie.setDeleted(true);
        movie.setDeletedAt(ZonedDateTime.now(ZoneOffset.UTC).toInstant());
        movieRepository.save(movie);
        return AppResponseBuilder.success(null, HttpStatus.NO_CONTENT,
                "entity.deleted.success", EntityString.MOVIE.getName());
    }


    @Transactional
    public AppResponse<MovieInfoDetails> getMovieByImdbId(String imdbId) throws JacksonException {

        Movie movie = movieRepository.getMovieByImdbId(imdbId).orElseThrow(
                () -> new BusinessException(ErrorCode.MOVIE_NOT_FOUND)
        );
        MovieInfoDetails movieInfoDetails = omdbMovieMapper.mapToMovieInfoDetails(movie);
//        movieInfoDetails.setMemberRating(getMemberRatingForMovie(imdbId));
        movieInfoDetails.setAverageRating(movie.getAverageRating());

        return AppResponseBuilder.success(movieInfoDetails, "generic.get.success");

    }

    @Transactional
    public AppResponse<List<Movie>> searchMovies(String keyword, int pageNumber) {
        Page<Movie> moviePage = movieRepository.searchForMovie(keyword, systemUtils.buildPageableObj(pageNumber));
        MetaData metaData = PaginationMetaDataMapper.fromPage(moviePage);

        return AppResponseBuilder.success(moviePage.getContent(),metaData,"generic.get.success");
    }

    @Transactional
    public AppResponse<List<Movie>> getAllMovies(int pageNumber) {
        Page<Movie> movies = movieRepository.getAllMovies(systemUtils.buildPageableObj(pageNumber));
        MetaData metaData = PaginationMetaDataMapper.fromPage(movies);

        return AppResponseBuilder.success(movies.getContent(),metaData,"generic.get.success");
    }

//    public int getMemberRatingForMovie(String imdbId) {
//        String username = Objects.requireNonNull(User.getCredentials()).getUsername();
//        User user = userService.getUser(username);
//        Integer rating = memberRatingRepository.getMemberRatingForAMovie(imdbId, user.getId());
//        return rating != null ? rating : 0;
//    }
}
