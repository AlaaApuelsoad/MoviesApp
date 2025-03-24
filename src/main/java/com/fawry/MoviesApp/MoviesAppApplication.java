package com.fawry.MoviesApp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
public class MoviesAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoviesAppApplication.class, args);
		ExecutorService executorService = Executors.newFixedThreadPool(3);
		for (int i = 0; i < 6; i++) {
			final int finalI = i;
			executorService.submit(()->{
				System.out.println("Task " + finalI + " is running on thread " + Thread.currentThread().getName());
				try {
					Thread.sleep(6000);
				}catch (InterruptedException e){
					e.printStackTrace();
				}
			});
		}
		executorService.shutdown();
	}

	@Bean
	public CommandLineRunner init(){

		return args -> {
			System.out.println("Welcome to Fawry Movies App.");
		};
	}

}
