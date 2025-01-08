package com.fawry.MoviesApp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fawry.MoviesApp.dto.MovieSearchResponse;
import com.fawry.MoviesApp.mapper.MovieResponseMapper;
import com.fawry.MoviesApp.model.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class OMDBService {

    private final RestTemplate restTemplate;
    private final MovieResponseMapper movieResponseMapper;
    @Value("${app.ombd.api.integration.key}")
    private String API_KEY;
    private static final String BASE_URL = "https://www.omdbapi.com/?apikey=";

    public MovieSearchResponse searchMovies(String title,int pageNumber) throws JsonProcessingException {
//        String url = BASE_URL+API_KEY+"&s="+title+"&page="+pageNumber;
        String url = "https://www.omdbapi.com/?s=open&apikey=f24617b3&page=3";
        String response = formattedResponse(restTemplate.getForObject(url, String.class));
        return movieResponseMapper.mapToMovieSearchResponse(response);

//        https://www.omdbapi.com/?s=captain&apikey=f24617b3&page=3
    }


    private String formattedResponse(String response) throws JsonProcessingException {

        String jsonResponse = "{ \"Search\": [ { \"Title\": \"Captain Marvel\", \"Year\": \"2019\", \"imdbID\": \"tt4154664\", \"Type\": \"movie\", \"Poster\": \"https://m.media-amazon.com/images/M/MV5BZDI1NGU2ODAtNzBiNy00MWY5LWIyMGEtZjUxZjUwZmZiNjBlXkEyXkFqcGc@._V1_SX300.jpg\" }, { \"Title\": \"Marvel Studios' Captain Marvel LIVE Red Carpet World Premiere\", \"Year\": \"2019\", \"imdbID\": \"tt9908924\", \"Type\": \"movie\", \"Poster\": \"https://m.media-amazon.com/images/M/MV5BMjgzYjU2MDMtZWNjZC00MTM5LTk1ZWYtYzQxZjQ0YjI4OGFkXkEyXkFqcGc@._V1_SX300.jpg\" } ] , \"totalResults\": \"11\", \"Response\": \"True\" }";

        ObjectMapper objectMapper = new ObjectMapper();
        Object json = objectMapper.readValue(jsonResponse, Object.class);
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
    }


}
