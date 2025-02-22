package com.fawry.MoviesApp.configuration;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * The {@code SystemPropertiesConfiguration} class is responsible for extracting application properties
 * from the Spring Environment and saving them to an external file.
 *
 * <p>
 * This component runs automatically after bean initialization (using {@code @PostConstruct}).
 * It writes all loaded properties from the application context into a file at a specific location.
 * </p>
 */
@Component
@RequiredArgsConstructor
public class SystemPropertiesConfiguration {

    /**
     * Spring {@link Environment} instance to access application properties.
     */
    private final Environment environment;

    /**
     * Reads configuration properties from the application context and writes them to an external file.
     * This method is executed automatically after bean initialization due to the {@code @PostConstruct} annotation.
     */
    @PostConstruct
    public void configReader() {
        // Define the file path where properties will be saved
        String filePath = "C:/Users/apuel/Desktop/txt/application-properties.properties";

        try {
            System.out.println("===== Loaded Properties =====");

            FileWriter writer = new FileWriter(filePath);

            if (environment instanceof ConfigurableEnvironment configurableEnvironment) {
                MutablePropertySources propertySources = configurableEnvironment.getPropertySources();
                for (PropertySource<?> propertySource : propertySources) {
                    if (propertySource.getName().contains("Config resource")) {
                        Object source = propertySource.getSource();
                        if (source instanceof Map<?, ?> map) {
                            for (Map.Entry<?, ?> entry : map.entrySet()) {
                                writer.write(entry.getKey() + "=" + entry.getValue() + "\n");
                            }
                        }
                        System.out.println(propertySource.getName());
                    }
                }

                System.out.println("-------------------------------");
                System.out.println("You will find the application.properties file at: " + filePath);
            }

            writer.close();

        } catch (IOException e) {
            throw new RuntimeException("Failed Writing Properties", e);
        }catch (Exception e){
            System.out.println("Unexpected Exception: " + e);
        }
    }

    /**
     * Environment in Spring:
     * <p>
     * The {@link Environment} interface represents the current environment
     * in which the Spring application is running. It provides access to
     * application properties, system properties, and environment variables.
     * </p>
     */
}
