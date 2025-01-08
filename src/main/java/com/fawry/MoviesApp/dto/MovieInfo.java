package com.fawry.MoviesApp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovieInfo {

    private String Title;
    private String Year;
    private String imdbID;
    private String Type;
    private String Poster;

}
