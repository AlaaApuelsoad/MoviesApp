package com.fawry.MoviesApp.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fawry.MoviesApp.dto.MovieInfo;
import com.fawry.MoviesApp.dto.MovieSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MovieSearchResponseMapper {

    private final MovieSearchResponse movieSearchResponse;


    public MovieSearchResponse mapToMovieSearchResponse(String jsonResponse) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);

        int totalMovies = jsonNode.get("totalResults").asInt();
        movieSearchResponse.setTotalMovies(totalMovies);

        List<MovieInfo> movieList = new ArrayList<>();
        JsonNode searchArray = jsonNode.get("Search");
        if (searchArray != null && searchArray.isArray()) {
            for (JsonNode movieNode : searchArray) {
                MovieInfo movieInfo = new MovieInfo();
                movieInfo.setTitle(movieNode.get("Title").asText());
                movieInfo.setType(movieNode.get("Type").asText());
                movieInfo.setYear(movieNode.get("Year").asText());
                movieInfo.setImdbID(movieNode.get("imdbID").asText());
                movieInfo.setPoster(movieNode.get("Poster").asText());
                movieList.add(movieInfo);
            }
        }
        movieSearchResponse.setSearch(movieList);
        return movieSearchResponse;

    }
}
