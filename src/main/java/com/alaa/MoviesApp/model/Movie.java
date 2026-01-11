package com.alaa.MoviesApp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
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
    private String title;
    @Column(nullable = false)
    private String year;
    @Column(nullable = false)
    private String rated;
    @Column(nullable = false)
    private String released;
    @Column(nullable = false)
    private String runtime;
    @Column(nullable = false)
    private String genre;
    @Column(nullable = false)
    private String director;
    @Column(nullable = false)
    private String writer;

    @Column(name = "actor",nullable = false)
    private String actors;

    @Lob
    private String plot;

    @Column(nullable = false)
    private String language;
    @Column(nullable = false)
    private String country;
    private String awards;
    @Column(nullable = false)
    private String poster;

    @ElementCollection
    @CollectionTable(name = "movie_source_ratings", joinColumns = @JoinColumn(name = "movie_id"))
    private List<Rating> Ratings;

    @Column(nullable = false)
    private String imdbRating;
    @Column(nullable = false)
    private String imdbVotes;
    @Column(nullable = false)
    private String type;
    @Column(nullable = false)
    private String DVD;
    @Column(nullable = false)
    private String boxOffice;
    @Column(nullable = false)
    private String production;

    @Column(name = "added_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    private LocalDate addedAt;

    private boolean isDeleted = false;

    @OneToMany(mappedBy = "movie")
    @JsonManagedReference("movieRatingReference")
    private List<MemberRating> memberRatings;


    @Transient
    public double getAverageRating() {
        if (memberRatings == null || memberRatings.isEmpty()) {
            return 0.0;
        }
        return memberRatings.stream()
                .mapToInt(MemberRating::getRating)
                .average().orElse(0.0);
    }
}
