/**
 * DAO class for integrating with the OMDB API.
 * <p>
 * This class provides methods for searching movies by title and retrieving movie details
 * using the OMDB API. It uses a provided API key for authentication.
 * </p>
 */

package com.fawry.MoviesApp.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fawry.MoviesApp.dto.MovieSearchResponse;
import com.fawry.MoviesApp.mapper.MovieSearchResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class OMDBDao {

    private final RestTemplate restTemplate;
    private final MovieSearchResponseMapper movieSearchResponseMapper;
    @Value("${app.ombd.api.integration.key}")
    private String API_KEY;
    private static final String BASE_URL = "https://www.omdbapi.com/?type=movie&apikey=";

    public MovieSearchResponse searchMovies(String title,int pageNumber) throws JsonProcessingException {
        String url = BASE_URL+API_KEY+"&s="+title+"&page="+pageNumber;
        String response = restTemplate.getForObject(url, String.class);
        return movieSearchResponseMapper.mapToMovieSearchResponse(response);

    }

    public String getMovieByImdbId(String imdbId) {
        String url = BASE_URL+API_KEY+"&i="+imdbId;
        return restTemplate.getForObject(url, String.class);
    }


}
