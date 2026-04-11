/**
 * DAO class for integrating with the OMDB API.
 * <p>
 * This class provides methods for searching movies by title and retrieving movie details
 * using the OMDB API. It uses a provided API key for authentication.
 * </p>
 */

package com.alaa.MoviesApp.service;

import com.alaa.MoviesApp.dto.AppResponse;
import com.alaa.MoviesApp.mapper.OmdbMovieMapper;
import com.alaa.MoviesApp.utils.AppResponseBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.alaa.MoviesApp.dto.IntegrationSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class OmdbIntegrationService {

    private final SystemPropertyService systemPropertyService;
    private final RestTemplate restTemplate;
    private final OmdbMovieMapper omdbMovieMapper;

    public AppResponse<IntegrationSearch> searchMovies(String title, int pageNumber) throws JsonProcessingException {
        String url = systemPropertyService.getProperty("app.omdb.base-url")
                + systemPropertyService.getProperty("app.omdb.api.integration.key")
                + "&s=" + title
                + "&page=" + pageNumber;
        String response = restTemplate.getForObject(url, String.class);
        IntegrationSearch integrationSearch = omdbMovieMapper.mapSearchResponse(response);
        return AppResponseBuilder.buildResponse(true,integrationSearch,
                "Movies fetched successfully", HttpStatus.OK,null,null);

    }

    public String getMovieByImdbId(String imdbId) {
        String url = systemPropertyService.getProperty("app.omdb.base-url") +
                systemPropertyService.getProperty("app.omdb.api.integration.key") + "&i="+imdbId;
        return restTemplate.getForObject(url, String.class);
    }

}
