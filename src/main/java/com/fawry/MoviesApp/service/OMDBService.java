package com.fawry.MoviesApp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class OMDBService {

    private final RestTemplate restTemplate;
    @Value("${app.ombd.api.integration.key}")
    private String API_KEY;
    private static final String BASE_URL = "https://www.omdbapi.com/?apikey=";

    public String searchMovies(String title) throws JsonProcessingException {
        String url = BASE_URL+API_KEY+"&s="+title;
        String response = restTemplate.getForObject(url, String.class);
        return formattedResponse(response);
    }


    private String formattedResponse(String response) throws JsonProcessingException {

        String jsonResponse = "{ \"Search\": [ { \"Title\": \"Captain Marvel\", \"Year\": \"2019\", \"imdbID\": \"tt4154664\", \"Type\": \"movie\", \"Poster\": \"https://m.media-amazon.com/images/M/MV5BZDI1NGU2ODAtNzBiNy00MWY5LWIyMGEtZjUxZjUwZmZiNjBlXkEyXkFqcGc@._V1_SX300.jpg\" }, { \"Title\": \"Marvel Studios' Captain Marvel LIVE Red Carpet World Premiere\", \"Year\": \"2019\", \"imdbID\": \"tt9908924\", \"Type\": \"movie\", \"Poster\": \"https://m.media-amazon.com/images/M/MV5BMjgzYjU2MDMtZWNjZC00MTM5LTk1ZWYtYzQxZjQ0YjI4OGFkXkEyXkFqcGc@._V1_SX300.jpg\" } ] , \"totalResults\": \"11\", \"Response\": \"True\" }";

        ObjectMapper objectMapper = new ObjectMapper();
        Object json = objectMapper.readValue(jsonResponse, Object.class);
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
    }


}
