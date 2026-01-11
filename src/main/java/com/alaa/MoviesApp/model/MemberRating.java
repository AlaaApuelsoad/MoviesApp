package com.alaa.MoviesApp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class MemberRating extends AuditEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    @JsonBackReference("userRatingReference")
    private User user;

    @ManyToOne
    @JoinColumn(name = "movie_id",nullable = false)
    @JsonBackReference("movieRatingReference")
    private Movie movie;

    @Column(name = "rating_value")
    private int rating;

}
