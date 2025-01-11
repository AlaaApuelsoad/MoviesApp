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
@Table(indexes = {
        @Index(name = "idx_movie_imdbId",columnList = "imdbID")
})
public class Movie extends AuditEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id")
    private long id;

    @Column(unique = true, nullable = false)
    private String imdbID;
    @Column(nullable = false)
    private String Title;
    @Column(nullable = false)
    private String Year;
    @Column(nullable = false)
    private String Rated;
    @Column(nullable = false)
    private String Released;
    @Column(nullable = false)
    private String Runtime;
    @Column(nullable = false)
    private String Genre;
    @Column(nullable = false)
    private String Director;
    @Column(nullable = false)
    private String Writer;

    @Column(name = "actor",nullable = false)
    private String actors;

    @Lob
    private String Plot;

    @Column(nullable = false)
    private String Language;
    @Column(nullable = false)
    private String Country;
    private String Awards;
    @Column(nullable = false)
    private String Poster;

    @ElementCollection
    @CollectionTable(name = "movie_source_ratings", joinColumns = @JoinColumn(name = "movie_id"))
    private List<Rating> Ratings;

    @Column(nullable = false)
    private String imdbRating;
    @Column(nullable = false)
    private String imdbVotes;
    @Column(nullable = false)
    private String Type;
    @Column(nullable = false)
    private String DVD;
    @Column(nullable = false)
    private String BoxOffice;
    @Column(nullable = false)
    private String Production;

    private boolean isDeleted = false;

}
