package com.alaa.moviesapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableAsync
@EnableCaching
public class MoviesAppApplication {
	private static final Logger logger = LoggerFactory.getLogger(MoviesAppApplication.class);

	public static void main(String[] args) {

		ApplicationContext context = SpringApplication.run(MoviesAppApplication.class, args);
		Environment environment = context.getEnvironment();
		String serverPort = environment.getProperty("server.port");
		logger.info("Application started successfully on port: {} ", serverPort);
	}
}
