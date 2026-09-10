package com.alaa.MoviesApp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class MovieListInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String title;
    private String year;
    @JsonProperty("poster")
    private String posterPath;
    private String imdbID;
    private int memberRating;
    @JsonProperty("MemberAverageRating")
    private double averageRating;

    public MovieListInfo(Long id,String title, String year, String posterPath, String imdbID) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.posterPath = posterPath;
        this.imdbID = imdbID;
    }

}
