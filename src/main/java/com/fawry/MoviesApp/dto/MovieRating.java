package com.fawry.MoviesApp.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovieRating {

    private String message;
    private String title;
    private int rating;

}
