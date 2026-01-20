package com.alaa.MoviesApp.repository;

import com.alaa.MoviesApp.dto.MovieListInfo;
import com.alaa.MoviesApp.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

//    @Transactional
//    @Query("SELECT new com.fawry.MoviesApp.dto.MovieInfoDetails (" +
//            "m.imdbID, m.title, m.year, m.rated, m.released, m.runtime, m.genre, " +
//            "m.director, m.writer, m.actors, m.plot, m.language, m.country, " +
//            "m.awards, m.poster, null , m.imdbRating, m.imdbVotes, m.type, " +
//            "m.boxOffice, m.addedAt ) " +
//            "FROM Movie m WHERE m.imdbID = :imdbId")
//    Optional<MovieInfoDetails> getMovieInfoDetailsByImdbId(@Param("imdbId") String imdbId);

    @Transactional
    @Query("SELECT m FROM Movie m WHERE m.imdbID = :imdbId")
    Optional<Movie> getMovieByImdbId(@Param("imdbId") String imdbId);

    @Transactional
    @Query("SELECT m FROM Movie m WHERE " +
            "LOWER(m.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(m.director) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(m.actors) LIKE LOWER(CONCAT('%', :keyword, '%')) ")
    Page<Movie> searchForMovie(@Param("keyword") String keyword, Pageable pageable);

    @Transactional
    @Query("SELECT new com.alaa.MoviesApp.dto.MovieListInfo(m.title, m.year, m.poster, m.imdbID,m ) " +
            "FROM Movie m WHERE m.isDeleted = false")
    Page<MovieListInfo> getMoviesFromDB(Pageable pageable);

}
