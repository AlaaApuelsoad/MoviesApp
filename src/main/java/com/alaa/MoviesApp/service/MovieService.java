package com.alaa.MoviesApp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.alaa.MoviesApp.dto.CustomPageDto;
import com.alaa.MoviesApp.dto.MovieInfoDetails;
import com.alaa.MoviesApp.dto.MovieListInfo;
import com.alaa.MoviesApp.dto.ResponseMessage;
import com.alaa.MoviesApp.enums.ErrorCode;
import com.alaa.MoviesApp.exception.CustomException;
import com.alaa.MoviesApp.mapper.MovieMapper;
import com.alaa.MoviesApp.mapper.PageMapper;
import com.alaa.MoviesApp.model.Movie;
import com.alaa.MoviesApp.model.User;
import com.alaa.MoviesApp.repository.MemberRatingRepository;
import com.alaa.MoviesApp.repository.MovieRepository;
import com.alaa.MoviesApp.utils.UserHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;


@Service
@RequiredArgsConstructor
public class MovieService {

    public final OmdbIntegrationService omdbIntegrationService;
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;
    private final PageMapper pageMapper;
    private final UserHelper userHelper;
    private final MemberRatingRepository memberRatingRepository;


    @Transactional
    public Movie addMovie(String imdbId) throws JsonProcessingException {
        if (movieRepository.getMovieByImdbId(imdbId).isPresent()) {
            throw new CustomException(ErrorCode.MOVIE_EXISTS);
        }

        String movieResponse = omdbIntegrationService.getMovieByImdbId(imdbId);
        Movie movie = movieMapper.mapToMovie(movieResponse);
        return movieRepository.save(movie);
    }

    @Transactional
    public ResponseMessage deleteMovieByImdbId(String imdbId) {
        Movie movie = movieRepository.findByIdImdbId(imdbId).orElseThrow(
                () -> new CustomException(ErrorCode.MOVIE_NOT_FOUND)
        );
        if (movie.isDeleted()) {
            throw new CustomException(ErrorCode.ALREADY_DELETED);
        }

        movie.setDeleted(true);
        movieRepository.save(movie);
        return new ResponseMessage("Movie deleted successfully");
    }


    @Transactional
    public MovieInfoDetails getMovieByImdbId(String imdbId) {

        Movie movie = movieRepository.getMovieByImdbId(imdbId).orElseThrow(
                () -> new CustomException(ErrorCode.MOVIE_NOT_FOUND)
        );
        MovieInfoDetails movieInfoDetails = movieMapper.mapToMovieInfoDetails(movie);
        movieInfoDetails.setMemberRating(getMemberRatingForMovie(imdbId));
        movieInfoDetails.setAverageRating(movie.getAverageRating());
        return movieInfoDetails;
    }

    @Transactional
    public CustomPageDto<MovieListInfo> searchMovies(String keyword, Pageable pageable) {
        Page<Movie> moviePage = movieRepository.searchForMovie(keyword, pageable);
        if (moviePage.getContent().isEmpty()) {
            throw new CustomException(ErrorCode.NO_DATA_FOUND);
        }
        return pageMapper.customPageDto(moviePage.map(movieMapper::mapToMovieInfoList));
    }

    @Transactional
    public CustomPageDto<MovieListInfo> getMoviePaginatedFromDB(Pageable pageable) {
        Page<MovieListInfo> moviePage = movieRepository.getMoviesFromDB(pageable);
        moviePage.getContent().forEach(movieListInfo -> {
            String imdbId = movieListInfo.getImdbID();
            int memberRating = getMemberRatingForMovie(imdbId);
            movieListInfo.setMemberRating(memberRating);
        });
        if (moviePage.getContent().isEmpty()) {
            throw new CustomException(ErrorCode.NO_DATA_FOUND);
        }
        return pageMapper.customPageDto(moviePage);
    }

    public int getMemberRatingForMovie(String imdbId) {
        String username = Objects.requireNonNull(User.getCredentials()).getUsername();
        User user = userHelper.getUser(username);
        Integer rating = memberRatingRepository.getMemberRatingForAMovie(imdbId, user.getId());
        return rating != null ? rating : 0;
    }
}
