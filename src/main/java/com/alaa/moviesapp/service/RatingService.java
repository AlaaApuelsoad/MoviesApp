package com.alaa.moviesapp.service;

import com.alaa.moviesapp.dto.AppResponse;
import com.alaa.moviesapp.enums.ErrorCode;
import com.alaa.moviesapp.exception.BusinessException;
import com.alaa.moviesapp.model.MemberRating;
import com.alaa.moviesapp.model.Movie;
import com.alaa.moviesapp.model.User;
import com.alaa.moviesapp.repository.MemberRatingRepository;
import com.alaa.moviesapp.repository.MovieRepository;
import com.alaa.moviesapp.utils.AppResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public AppResponse<?> userRatingMovie(int ratingValue, String imdbId) {

        if (imdbId == null || imdbId.isBlank() || ratingValue < 0 || ratingValue > 5) {
            throw new BusinessException(ErrorCode.INVALID_INPUT);
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

        return AppResponseBuilder.buildResponse(
                true,null,"Movie Rating successful", HttpStatus.OK,null,null);
    }

}
