package com.fawry.MoviesApp.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id")
    private long id;

    private String imdbID;

    private String Title;
    private String Year;
    private String Rated;
    private String Released;
    private String Runtime;
    private String Genre;
    private String Director;
    private String Writer;

    @ElementCollection
    @CollectionTable(name = "movie_actors", joinColumns = @JoinColumn(name = "movie_id"))
    @Column(name = "actor")
    private List<String> Actors;

    @Lob
    private String Plot;

    private String Language;
    private String Country;
    private String Awards;
    private String Poster;

    @ElementCollection
    @CollectionTable(name = "movie_ratings", joinColumns = @JoinColumn(name = "movie_id"))
    private List<Rating> Ratings;

    private String imdbRating;
    private String imdbVotes;
    private String Type;
    private String DVD;
    private String BoxOffice;
    private String Production;

}
