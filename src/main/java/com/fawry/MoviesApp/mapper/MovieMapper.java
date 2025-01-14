package com.fawry.MoviesApp.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fawry.MoviesApp.dto.MovieInfoDetails;
import com.fawry.MoviesApp.dto.MovieListInfo;
import com.fawry.MoviesApp.model.Movie;
import com.fawry.MoviesApp.model.Rating;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MovieMapper {

    private final ObjectMapper objectMapper;

    public Movie mapToMovie(String json) throws JsonProcessingException {
//
//        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);

        Movie movie = new Movie();
        movie.setTitle(jsonNode.get("Title").asText());
        movie.setYear(jsonNode.get("Year").asText());
        movie.setRated(jsonNode.get("Rated").asText());
        movie.setReleased(jsonNode.get("Released").asText());
        movie.setRuntime(jsonNode.get("Runtime").asText());
        movie.setGenre(jsonNode.get("Genre").asText());
        movie.setDirector(jsonNode.get("Director").asText());
        movie.setWriter(jsonNode.get("Writer").asText());
        movie.setPlot(jsonNode.get("Plot").asText());
        movie.setActors(jsonNode.get("Actors").asText());
        movie.setLanguage(jsonNode.get("Language").asText());
        movie.setCountry(jsonNode.get("Country").asText());
        movie.setAwards(jsonNode.get("Awards").asText());
        movie.setPoster(jsonNode.get("Poster").asText());
        movie.setImdbRating(jsonNode.get("imdbRating").asText());
        movie.setImdbVotes(jsonNode.get("imdbVotes").asText());
        movie.setImdbID(jsonNode.get("imdbID").asText());
        movie.setType(jsonNode.get("Type").asText());
        movie.setDVD(jsonNode.get("DVD").asText());
        movie.setBoxOffice(jsonNode.get("BoxOffice").asText());
        movie.setProduction(jsonNode.get("Production").asText());

        List<Rating> ratings = new ArrayList<>();
        JsonNode ratingsJsonNode = jsonNode.get("Ratings");
        if (ratingsJsonNode != null & ratingsJsonNode.isArray()) {
            for (JsonNode ratingJsonNode : ratingsJsonNode) {
                Rating rating = new Rating();
                rating.setSourceWebsite(ratingJsonNode.get("Source").asText());
                rating.setRatingValue(ratingJsonNode.get("Value").asText());
                ratings.add(rating);
            }
        }
        movie.setRatings(ratings);
        return movie;

    }

    public MovieInfoDetails mapToMovieInfoDetails(Movie movie){

        return objectMapper.convertValue(movie, MovieInfoDetails.class);
    }

    public MovieListInfo mapToMovieInfoList(Movie movie){
        return objectMapper.convertValue(movie, MovieListInfo.class);
    }


}
