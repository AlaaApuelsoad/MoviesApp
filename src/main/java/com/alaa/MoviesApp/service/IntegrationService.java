package com.alaa.MoviesApp.service;

import com.alaa.MoviesApp.dto.AppResponse;
import com.alaa.MoviesApp.dto.IntegrationSearch;
import com.alaa.MoviesApp.mapper.IntegrationMapper;
import com.alaa.MoviesApp.utils.AppResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tools.jackson.core.JacksonException;

@Service
@RequiredArgsConstructor
public class IntegrationService {

    private final RestTemplate restTemplate;
    private final IntegrationMapper integrationMapper;
    private final SystemPropertyService systemPropertyService;

    public AppResponse<IntegrationSearch> searchMovies(String title, int pageNumber) throws JacksonException {
        String url = systemPropertyService.getProperty("app.omdb.base-url")
                + systemPropertyService.getProperty("app.omdb.api.integration.key")
                + "&s=" + title
                + "&page=" + pageNumber;
        String response = restTemplate.getForObject(url, String.class);
        IntegrationSearch integrationSearch = integrationMapper.mapSearchResponse(response);

        return AppResponseBuilder.success(integrationSearch, "generic.get.success");
    }

    public String getMovieByImdbId(String imdbId) {
        String url = systemPropertyService.getProperty("app.omdb.base-url") +
                systemPropertyService.getProperty("app.omdb.api.integration.key") + "&i="+imdbId;
        return restTemplate.getForObject(url, String.class);
    }

}
