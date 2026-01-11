package com.alaa.MoviesApp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Rating {

    @Column(name = "website")
    private String sourceWebsite;
    @Column(name = "rating")
    private String ratingValue;
}
