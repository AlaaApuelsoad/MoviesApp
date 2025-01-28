package com.fawry.MoviesApp.controller;

import com.fawry.MoviesApp.dto.CustomPageDto;
import com.fawry.MoviesApp.dto.MovieInfoDetails;
import com.fawry.MoviesApp.dto.MovieListInfo;
import com.fawry.MoviesApp.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/fawry/home/",produces = "application/json")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class HomeController {

    private final MovieService movieService;
    @Value("${app.fawry.page.size}")
    private int PAGE_SIZE;


    @GetMapping("/page")
    @Operation(summary = "Fawry Movie app home page",description = "Home page displays movies paginated")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Successfully retrieved Movies pages"),
            @ApiResponse(responseCode = "404",description = "No data found"),
            @ApiResponse(responseCode = "500",description = "Internal Server Error")
    })
    @PreAuthorize("hasAuthority('MEMBER')")
    public ResponseEntity<CustomPageDto<MovieListInfo>> getAllMoviesFromDB(@RequestParam(defaultValue = "0") int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber,PAGE_SIZE);
        return new ResponseEntity<>(movieService.getMoviePaginatedFromDB(pageable),HttpStatus.OK);

    }

    @GetMapping("/movies/get/{imdbID}")
    @Operation(summary = "Get Movie Details by IMDB ID",description = "Fetches detailed information about a movie using its IMDb ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Successfully retrieved movie details"),
            @ApiResponse(responseCode = "404",description = "Movie Not Found"),
            @ApiResponse(responseCode = "500",description = "Internal Server Error")
    })
    @PreAuthorize("hasAuthority('MEMBER')")
    public ResponseEntity<MovieInfoDetails> getMovieByImdbID(@PathVariable("imdbID") String imdbID){
        return new ResponseEntity<>(movieService.getMovieByImdbId(imdbID),HttpStatus.OK);
    }

    @GetMapping("/movies/search")
    @Operation(summary = "Search for movies by keyword",description = "Search for movies by 'title,actors and directors'.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "A paginated list of movies matching the search keyword",
            content = @Content(schema = @Schema(implementation = CustomPageDto.class))),
            @ApiResponse(responseCode = "404",description = "No Data Found")
    })
    public ResponseEntity<CustomPageDto<MovieListInfo>> searchForMovies(
            @RequestParam("keyword") String keyword, @RequestParam(defaultValue = "0") int pageNumber){
        Pageable pageable = PageRequest.of(pageNumber,PAGE_SIZE);
        return new ResponseEntity<>(movieService.searchMovies(keyword,pageable),HttpStatus.OK);
    }


}
