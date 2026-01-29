/**
 * DAO class for integrating with the OMDB API.
 * <p>
 * This class provides methods for searching movies by title and retrieving movie details
 * using the OMDB API. It uses a provided API key for authentication.
 * </p>
 */

package com.alaa.MoviesApp.service;

import com.alaa.MoviesApp.mapper.OmdbMovieMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.alaa.MoviesApp.dto.MovieSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class OmdbIntegrationService {

    private final SystemPropertyService systemPropertyService;
    private final RestTemplate restTemplate;
    private final OmdbMovieMapper omdbMovieMapper;

    @Cacheable(value = "movieDetails", key = "#pageNumber")
    public MovieSearchResponse searchMovies(String title,int pageNumber) throws JsonProcessingException {
        String url = systemPropertyService.getProperty("app.omdb.base-url") +
                systemPropertyService.getProperty("app.ombd.api.integration.key") +
                "&s=" + title + "&page=" + pageNumber;
        String response = restTemplate.getForObject(url, String.class);
        return omdbMovieMapper.mapSearchResponse(response);
    }

    public String getMovieByImdbId(String imdbId) {
        String url = systemPropertyService.getProperty("app.omdb.base-url") +
                systemPropertyService.getProperty("app.ombd.api.integration.key") + "&i="+imdbId;
        return restTemplate.getForObject(url, String.class);
    }

}
