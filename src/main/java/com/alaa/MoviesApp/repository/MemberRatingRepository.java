package com.alaa.MoviesApp.repository;

import com.alaa.MoviesApp.model.MemberRating;
import com.alaa.MoviesApp.model.Movie;
import com.alaa.MoviesApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRatingRepository extends JpaRepository<MemberRating,Long> {



    @Query("SELECT m from MemberRating m WHERE m.user= :user AND m.movie= :movie")
    Optional<MemberRating> findByUserAndMovie(@Param("user") User user,@Param("movie") Movie movie);

    @Query("SELECT mr.rating FROM MemberRating mr WHERE mr.movie.imdbID = :imdbID AND mr.user.id = :userId")
    Integer getMemberRatingForAMovie(@Param("imdbID") String imdbId, @Param("userId") long userId);
}

