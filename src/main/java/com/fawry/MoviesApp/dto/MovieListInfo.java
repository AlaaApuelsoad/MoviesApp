package com.fawry.MoviesApp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fawry.MoviesApp.model.Movie;
import com.fawry.MoviesApp.service.MovieService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/*
Paginated Object for movie list  */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class MovieListInfo {

    private String title;
    private String year;
    @JsonProperty("poster")
    private String posterPath;
    private String imdbID;
    private int memberRating;
    @JsonProperty("MemberAverageRating")
    private double averageRating;

    public MovieListInfo(String title, String year, String posterPath, String imdbID,Movie movie) {
        this.title = title;
        this.year = year;
        this.posterPath = posterPath;
        this.imdbID = imdbID;
        this.averageRating = movie.getAverageRating();
    }


    public void setMemberRating(int memberRating) {
        this.memberRating = memberRating;
    }
}
