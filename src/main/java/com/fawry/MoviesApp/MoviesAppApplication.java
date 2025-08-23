package com.fawry.MoviesApp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
@EnableCaching
public class MoviesAppApplication {
	private static final Logger logger = LogManager.getLogger(MoviesAppApplication.class);

	public static void main(String[] args) {
		for (String arg : args) {//print argument passed
			System.out.println("Command Line Argument");
			System.out.println("Argument Passed: " + arg);
		}

		ApplicationContext context = SpringApplication.run(MoviesAppApplication.class, args);
		Environment environment = context.getEnvironment();
		System.out.println("Application started successfully on port: " + environment.getProperty("server.port"));
		String [] activeProfiles = environment.getActiveProfiles();
		for (String profile : activeProfiles) {
			System.out.println("Application Running with Configuration based on profile: " + profile);
		}

		DataSource dataSource = context.getBean(DataSource.class);
		try (Connection connection = dataSource.getConnection()){
			DatabaseMetaData metaData = connection.getMetaData();
			System.out.println("Database connection established");
			System.out.println("Connected to database: " + metaData.getDatabaseProductName());
			System.out.println("Database version: " + metaData.getDatabaseProductVersion());
			System.out.println("Database driver: " + metaData.getDriverName());
			System.out.println("Database driver version: " + metaData.getDriverVersion());
			System.out.println("Database URL: " + metaData.getURL());
			System.out.println("Database user: " + metaData.getUserName());
		}catch (Exception e){
			logger.info("error", e);
		}
	}
}
