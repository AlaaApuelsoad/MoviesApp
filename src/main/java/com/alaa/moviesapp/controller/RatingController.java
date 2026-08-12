package com.alaa.moviesapp.controller;

import com.alaa.moviesapp.constants.Navigation;
import com.alaa.moviesapp.dto.AppResponse;
import com.alaa.moviesapp.service.RatingService;
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
    @PreAuthorize("hasAuthority('MEMBER')")
    public ResponseEntity<AppResponse<?>> memberRatingMovie(
            @RequestParam("ratingValue") int ratingValue, @PathVariable String imdbId) {
        return new ResponseEntity<>(ratingService.userRatingMovie(ratingValue,imdbId), HttpStatus.OK);
    }
}
