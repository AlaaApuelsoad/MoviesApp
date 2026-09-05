package com.alaa.MoviesApp.mapper;

import com.alaa.MoviesApp.dto.IntegrationSearch;
import com.alaa.MoviesApp.dto.MovieInfoDetails;
import com.alaa.MoviesApp.dto.MovieOMDBInfo;
import com.alaa.MoviesApp.model.Movie;
import com.alaa.MoviesApp.model.Rating;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class OmdbMovieMapper {

    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;

    public Movie mapToMovie(String json) throws JacksonException {

        JsonNode jsonNode = objectMapper.readTree(json);

        Movie movie = new Movie();
        movie.setTitle(jsonNode.get("Title").asString(null));
        movie.setYear(jsonNode.get("Year").asString(null));
        movie.setRated(jsonNode.get("Rated").asString());
        movie.setReleased(jsonNode.get("Released").asString());
        movie.setRuntime(jsonNode.get("Runtime").asString());
        movie.setGenre(jsonNode.get("Genre").asString());
        movie.setDirector(jsonNode.get("Director").asString());
        movie.setWriter(jsonNode.get("Writer").asString());
        movie.setPlot(jsonNode.get("Plot").asString());
        movie.setActors(jsonNode.get("Actors").asString());
        movie.setLanguage(jsonNode.get("Language").asString());
        movie.setCountry(jsonNode.get("Country").asString());
        movie.setAwards(jsonNode.get("Awards").asString());
        movie.setPoster(jsonNode.get("Poster").asString());
        movie.setImdbRating(jsonNode.get("imdbRating").asString());
        movie.setImdbVotes(jsonNode.get("imdbVotes").asString());
        movie.setImdbID(jsonNode.get("imdbID").asString());
        movie.setType(jsonNode.get("Type").asString());
        movie.setDvd(jsonNode.get("DVD").asString());
        movie.setBoxOffice(jsonNode.get("BoxOffice").asString());
        movie.setProduction(jsonNode.get("Production").asString());
        movie.setAddedAt(Instant.now());

        movie.setRatings(mapRatings(jsonNode.get("Ratings")));

        return movie;

    }

    private List<Rating> mapRatings(JsonNode ratingsJsonNode) {
        List<Rating> ratings = new ArrayList<>();
        if (!Objects.isNull(ratingsJsonNode) && ratingsJsonNode.isArray()) {
            for (JsonNode ratingJsonNode : ratingsJsonNode) {
                Rating rating = new Rating();
                rating.setSourceWebsite(ratingJsonNode.get("Source").asString());
                rating.setRatingValue(ratingJsonNode.get("Value").asString());
                ratings.add(rating);
            }
        }
        return ratings;
    }

    public IntegrationSearch mapSearchResponse(String json) throws JacksonException {
        JsonNode node = objectMapper.readTree(json);

        IntegrationSearch response = new IntegrationSearch();
        response.setTotalMovies(node.path("totalResults").asInt());

        List<MovieOMDBInfo> movies = new ArrayList<>();
        for (JsonNode m : node.path("Search")) {
            MovieOMDBInfo info = new MovieOMDBInfo();
            info.setTitle(m.path("Title").asString());
            info.setYear(m.path("Year").asString());
            info.setType(m.path("Type").asString());
            info.setImdbID(m.path("imdbID").asString());
            info.setPoster(m.path("Poster").asString());
            movies.add(info);
        }

        response.setSearch(movies);
        return response;
    }

    public MovieInfoDetails mapToMovieInfoDetails(Movie movie) throws JacksonException {
        return modelMapper.map(movie,MovieInfoDetails.class);
    }

}
