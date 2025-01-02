package com.fawry.MoviesApp.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomOpenApi {


    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MoviesApp")
                        .version("1.0 SNAPSHOT")
                        .description("This page lists all the REST APIs for Fawry Movies App"));
    }
}
