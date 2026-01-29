package com.alaa.MoviesApp.mapper;

import com.alaa.MoviesApp.dto.MovieOMDBInfo;
import com.alaa.MoviesApp.dto.MovieSearchResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.alaa.MoviesApp.dto.MovieInfoDetails;
import com.alaa.MoviesApp.dto.MovieListInfo;
import com.alaa.MoviesApp.model.Movie;
import com.alaa.MoviesApp.model.Rating;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class OmdbMovieMapper {

    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;

    public Movie mapToMovie(String json) throws JsonProcessingException {

        JsonNode jsonNode = objectMapper.readTree(json);

        Movie movie = new Movie();
        movie.setTitle(jsonNode.get("Title").asText(null));
        movie.setYear(jsonNode.get("Year").asText(null));
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
        movie.setAddedAt(LocalDate.now());

        movie.setRatings(mapRatings(jsonNode.get("Ratings")));

        return movie;

    }

    private List<Rating> mapRatings(JsonNode ratingsJsonNode) {
        List<Rating> ratings = new ArrayList<>();
        if (!Objects.isNull(ratingsJsonNode) & ratingsJsonNode.isArray()) {
            for (JsonNode ratingJsonNode : ratingsJsonNode) {
                Rating rating = new Rating();
                rating.setSourceWebsite(ratingJsonNode.get("Source").asText());
                rating.setRatingValue(ratingJsonNode.get("Value").asText());
                ratings.add(rating);
            }
        }
        return ratings;
    }

    public MovieSearchResponse mapSearchResponse(String json) throws JsonProcessingException {
        JsonNode node = objectMapper.readTree(json);

        MovieSearchResponse response = new MovieSearchResponse();
        response.setTotalMovies(node.path("totalResults").asInt());

        List<MovieOMDBInfo> movies = new ArrayList<>();
        for (JsonNode m : node.path("Search")) {
            MovieOMDBInfo info = new MovieOMDBInfo();
            info.setTitle(m.path("Title").asText());
            info.setYear(m.path("Year").asText());
            info.setType(m.path("Type").asText());
            info.setImdbID(m.path("imdbID").asText());
            info.setPoster(m.path("Poster").asText());
            movies.add(info);
        }

        response.setSearch(movies);
        return response;
    }


    public MovieInfoDetails mapToMovieInfoDetails(Movie movie) throws JsonProcessingException {
        return modelMapper.map(movie,MovieInfoDetails.class);
    }

    public MovieListInfo mapToMovieInfoList(Movie movie) throws JsonProcessingException {
        return modelMapper.map(movie, MovieListInfo.class);
    }


}
