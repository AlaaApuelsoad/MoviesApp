package com.fawry.MoviesApp.repository;

import com.fawry.MoviesApp.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie,Long> {

    @Transactional
    @Query("SELECT m FROM Movie m WHERE m.imdbID= :imdbId")
    Optional<Movie> findByIdImdbId(@Param("imdbId") String imdbId);

}
