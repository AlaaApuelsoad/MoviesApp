package com.fawry.MoviesApp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Rating {

    @Column(name = "website")
    private String sourceWebsite;
    @Column(name = "rating")
    private String ratingValue;
}
