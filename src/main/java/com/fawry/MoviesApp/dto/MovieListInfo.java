package com.fawry.MoviesApp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fawry.MoviesApp.model.Movie;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
Paginated Object for movie list  */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovieListInfo {

    private String title;
    private String year;
    @JsonProperty("poster")
    private String posterPath;
    private String imdbID;

    @JsonProperty("MemberAverageRating")
    private double averageRating;

    public MovieListInfo(String title, String year, String posterPath, String imdbID,Movie movie) {
        this.title = title;
        this.year = year;
        this.posterPath = posterPath;
        this.imdbID = imdbID;
        this.averageRating = movie.getAverageRating();
    }
}
