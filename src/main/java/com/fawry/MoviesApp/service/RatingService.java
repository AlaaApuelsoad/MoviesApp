package com.fawry.MoviesApp.service;

import com.fawry.MoviesApp.dto.MovieRating;
import com.fawry.MoviesApp.enums.ErrorCode;
import com.fawry.MoviesApp.exception.CustomException;
import com.fawry.MoviesApp.model.MemberRating;
import com.fawry.MoviesApp.model.Movie;
import com.fawry.MoviesApp.model.User;
import com.fawry.MoviesApp.repository.MemberRatingRepository;
import com.fawry.MoviesApp.utils.MovieUtils;
import com.fawry.MoviesApp.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final LoginService loginService;
    private final UserUtils userUtils;
    private final MovieUtils movieUtils;
    private final MemberRatingRepository memberRatingRepository;


    @Transactional
    public MovieRating userRatingMovie(int ratingValue, String imdbId) {

        if (imdbId == null || imdbId.isBlank() || ratingValue < 0 || ratingValue > 5) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }

        User user = userUtils.getUser(loginService.getUserCredentials().getUsername());
        Movie movie = movieUtils.getMovie(imdbId);

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
