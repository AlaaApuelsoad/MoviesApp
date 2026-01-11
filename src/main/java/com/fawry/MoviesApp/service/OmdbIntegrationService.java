/**
 * DAO class for integrating with the OMDB API.
 * <p>
 * This class provides methods for searching movies by title and retrieving movie details
 * using the OMDB API. It uses a provided API key for authentication.
 * </p>
 */

package com.fawry.MoviesApp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fawry.MoviesApp.dto.MovieSearchResponse;
import com.fawry.MoviesApp.mapper.MovieSearchResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class OmdbIntegrationService {

    private final RestTemplate restTemplate;
    private final MovieSearchResponseMapper movieSearchResponseMapper;

    @Cacheable(value = "movieDetails", key = "#pageNumber")
    public MovieSearchResponse searchMovies(String title,int pageNumber) throws JsonProcessingException {
        String url = SystemPropertyService.getProperty("app.omdb.base-url") +
                SystemPropertyService.getProperty("app.ombd.api.integration.key") +
                "&s=" + title + "&page=" + pageNumber;
        String response = restTemplate.getForObject(url, String.class);
        return movieSearchResponseMapper.mapToMovieSearchResponse(response);
    }

    public String getMovieByImdbId(String imdbId) {
        String url = SystemPropertyService.getProperty("app.omdb.base-url") +
                SystemPropertyService.getProperty("app.ombd.api.integration.key") + "&i="+imdbId;
        return restTemplate.getForObject(url, String.class);
    }

}
