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

    private String title;
    private String year;
    private String imdbID;
    private String type;
    private String poster;

}
