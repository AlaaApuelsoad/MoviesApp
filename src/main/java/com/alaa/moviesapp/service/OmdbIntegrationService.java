package com.alaa.moviesapp.service;

import com.alaa.moviesapp.dto.AppResponse;
import com.alaa.moviesapp.dto.IntegrationSearch;
import com.alaa.moviesapp.mapper.OmdbMovieMapper;
import com.alaa.moviesapp.utils.AppResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tools.jackson.core.JacksonException;

@Service
@RequiredArgsConstructor
public class OmdbIntegrationService {

    private final SystemPropertyService systemPropertyService;
    private final RestTemplate restTemplate;
    private final OmdbMovieMapper omdbMovieMapper;

    public AppResponse<IntegrationSearch> searchMovies(String title, int pageNumber) throws JacksonException {
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
