package com.alaa.MoviesApp.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.alaa.MoviesApp.dto.MovieOMDBInfo;
import com.alaa.MoviesApp.dto.MovieSearchResponse;
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

        List<MovieOMDBInfo> movieList = new ArrayList<>();
        JsonNode searchArray = jsonNode.get("Search");
        if (searchArray != null && searchArray.isArray()) {
            for (JsonNode movieNode : searchArray) {
                MovieOMDBInfo movieOMDBInfo = new MovieOMDBInfo();
                movieOMDBInfo.setTitle(movieNode.get("Title").asText());
                movieOMDBInfo.setType(movieNode.get("Type").asText());
                movieOMDBInfo.setYear(movieNode.get("Year").asText());
                movieOMDBInfo.setImdbID(movieNode.get("imdbID").asText());
                movieOMDBInfo.setPoster(movieNode.get("Poster").asText());
                movieList.add(movieOMDBInfo);
            }
        }
        movieSearchResponse.setSearch(movieList);
        return movieSearchResponse;

    }
}
