package com.alaa.moviesapp.repository;

import com.alaa.moviesapp.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie,Long> {

    @NativeQuery("SELECT * FROM movie where is_deleted = false")
    Page<Movie> getAllMovies(Pageable pageable);

    @Query("SELECT m FROM Movie m WHERE m.imdbID= :imdbId")
    Optional<Movie> findByIdImdbId(@Param("imdbId") String imdbId);

    @Query("SELECT m FROM Movie m WHERE m.imdbID = :imdbId")
    Optional<Movie> getMovieByImdbId(@Param("imdbId") String imdbId);

    @NativeQuery("""
        SELECT *
        FROM movie m
        WHERE (
            LOWER(m.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(m.director) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(m.actor) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
        AND m.is_deleted = false
        """)
    Page<Movie> searchForMovie(@Param("keyword") String keyword, Pageable pageable);

}
