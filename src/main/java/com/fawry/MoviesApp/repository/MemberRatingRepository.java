package com.fawry.MoviesApp.repository;

import com.fawry.MoviesApp.model.MemberRating;
import com.fawry.MoviesApp.model.Movie;
import com.fawry.MoviesApp.model.User;
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
    int getMemberRatingForAMovie(@Param("imdbID") String imdbId, @Param("userId") long userId);
}

