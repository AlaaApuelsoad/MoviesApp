package com.alaa.MoviesApp.service;

import com.alaa.MoviesApp.dto.MovieRating;
import com.alaa.MoviesApp.enums.ErrorCode;
import com.alaa.MoviesApp.exception.CustomException;
import com.alaa.MoviesApp.model.MemberRating;
import com.alaa.MoviesApp.model.Movie;
import com.alaa.MoviesApp.model.User;
import com.alaa.MoviesApp.repository.MemberRatingRepository;
import com.alaa.MoviesApp.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final AuthenticationService authenticationService;
    private final MovieRepository movieRepository;
    private final MemberRatingRepository memberRatingRepository;
    private final UserService userService;


    @Transactional
    public MovieRating userRatingMovie(int ratingValue, String imdbId) {

        if (imdbId == null || imdbId.isBlank() || ratingValue < 0 || ratingValue > 5) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }

        User user = userService.getUser(authenticationService.getUserCredentials().getUsername());
        Movie movie = movieRepository.getMovieByImdbId(imdbId).orElseThrow(
                () -> new CustomException(ErrorCode.MOVIE_NOT_FOUND)
        );

        Optional<MemberRating> existingRating = memberRatingRepository.findByUserAndMovie(user, movie);

        if (existingRating.isPresent()) {
            MemberRating rating = existingRating.get();
            rating.setRating(ratingValue);
            memberRatingRepository.save(rating);
        } else {
            MemberRating memberRating = MemberRating.builder()
                    .movie(movie)
                    .user(user)
                    .rating(ratingValue)
                    .build();
            memberRatingRepository.save(memberRating);
        }

        return new MovieRating("Movie Rating successful",movie.getTitle(),ratingValue);
    }

}
