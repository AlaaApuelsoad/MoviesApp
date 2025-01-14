package com.fawry.MoviesApp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
}
