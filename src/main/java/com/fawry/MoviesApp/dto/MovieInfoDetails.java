package com.fawry.MoviesApp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fawry.MoviesApp.model.Rating;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/*
Show when user click on a movie
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovieInfoDetails {

    private String imdbID;
    @JsonProperty("Title")
    private String title;
    private String year;
    private String rated;
    private String released;
    private String runtime;
    private String genre;
    private String director;
    private String writer;
    private String actors;
    @Lob
    private String plot;
    private String language;
    private String country;
    private String awards;
    private String poster;
    @JsonProperty("Website Ratings")
    private List<Rating> ratings;
    private String imdbRating;
    private String imdbVotes;
    private String type;
    private String boxOffice;
    @JsonProperty("Added At")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    private LocalDate addedAt;



}
