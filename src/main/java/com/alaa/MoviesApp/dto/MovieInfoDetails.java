package com.alaa.MoviesApp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.alaa.MoviesApp.model.Rating;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/*
Movie Information Details shown when a user click on a movie
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieInfoDetails {

    private String imdbID;

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
    private List<Rating> ratings;
    private String imdbRating;
    private String imdbVotes;
    private String type;
    private String boxOffice;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    private LocalDate addedAt;

    @JsonProperty("MemberRating")
    private int memberRating;

    @JsonProperty("MembersAverageRatings")
    private double averageRating;

}
