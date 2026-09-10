package com.alaa.MoviesApp.service;

import com.alaa.MoviesApp.dto.AppResponse;
import com.alaa.MoviesApp.enums.ErrorCode;
import com.alaa.MoviesApp.exception.BusinessException;
import com.alaa.MoviesApp.model.MemberRating;
import com.alaa.MoviesApp.model.Movie;
import com.alaa.MoviesApp.model.User;
import com.alaa.MoviesApp.repository.MemberRatingRepository;
import com.alaa.MoviesApp.repository.MovieRepository;
import com.alaa.MoviesApp.utils.AppResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final UserService userService;
    private final MovieRepository movieRepository;
    private final AuthenticationService authenticationService;
    private final MemberRatingRepository memberRatingRepository;

    @Transactional
    public AppResponse<Object> userRatingMovie(int ratingValue, String imdbId) {

        if (imdbId == null || imdbId.isBlank() || ratingValue < 0 || ratingValue > 5) {
            throw new BusinessException(ErrorCode.BAD_REQUEST);
        }

        User user = userService.getUser(authenticationService.getUserCredentials().getUsername());
        Movie movie = movieRepository.getMovieByImdbId(imdbId).orElseThrow(
                () -> new BusinessException(ErrorCode.MOVIE_NOT_FOUND)
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

        return AppResponseBuilder.success("rating.success");
    }

}
