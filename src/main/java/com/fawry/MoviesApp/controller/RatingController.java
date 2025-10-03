package com.fawry.MoviesApp.controller;

import com.fawry.MoviesApp.dto.MovieRating;
import com.fawry.MoviesApp.service.RatingService;
import com.fawry.MoviesApp.constants.Navigation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Navigation.RATING_ROUTE)
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;


    @GetMapping("/movie/{imdbId}")
    @Operation(summary = "Rate a movie",description = "Member can rate a movie from (0,5)")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "Movie rating recorded successfully"),
            @ApiResponse(responseCode = "400",description = "Invalid input provided"),
            @ApiResponse(responseCode = "404",description = "Movie or User Not Found"),
            @ApiResponse(responseCode = "404",description = "Internal Server Error")
    })
    @PreAuthorize("hasAuthority('MEMBER')")
    public ResponseEntity<MovieRating> MemberRatingMovie(
            @RequestParam("ratingValue") int ratingValue, @PathVariable("imdbId") String imdbId) {
        return new ResponseEntity<>(ratingService.userRatingMovie(ratingValue,imdbId), HttpStatus.OK);
    }
}
