package com.alaa.MoviesApp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovieOMDBInfo {

    private String Title;
    private String Year;
    private String imdbID;
    private String Type;
    private String Poster;

}
