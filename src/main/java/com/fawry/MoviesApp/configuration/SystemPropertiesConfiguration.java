package com.fawry.MoviesApp.configuration;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Map;
import java.util.Properties;

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
     * Environment in Spring:
     * <p>
     * The {@link Environment} interface represents the current environment
     * in which the Spring application is running. It provides access to
     * application properties, system properties, and environment variables.
     * </p>
     */

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
        //dir file inside the container will be mapping to dir file in the host
        String filePath = System.getProperty("user.home")+"/systemProperties.properties";
        System.out.println("userhome-->"+System.getProperty("user.home"));
        System.out.println(filePath);
        System.out.println("===== Loaded Properties =====");
        createExternalPropertyFile(filePath);

        try(FileOutputStream outputStream = new FileOutputStream(filePath)) {
            Properties properties = new Properties();

            if (environment instanceof ConfigurableEnvironment configurableEnvironment) {
                MutablePropertySources propertySources = configurableEnvironment.getPropertySources();
                for (PropertySource<?> propertySource : propertySources) {
                    if (propertySource.getName().contains("Config resource")) {
                        Object source = propertySource.getSource();
                        if (source instanceof Map<?, ?> map) {
                            for (Map.Entry<?, ?> entry : map.entrySet()) {
                                properties.setProperty(entry.getKey().toString(), entry.getValue().toString());
                            }
                        }
                        System.out.println(propertySource.getName());
                    }
                }

                properties.store(outputStream, "System Properties");
                System.out.println("-------------------------------");
                System.out.println("You will find the application.properties file at: " + filePath);
                System.out.println("-------------------------------");
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed Writing Properties", e);
        }catch (Exception e){
            System.out.println("Unexpected Exception: " + e);
        }
    }

    public void createExternalPropertyFile(String filePath){
        try {
            File propertyFile = new File(filePath);

            File parentDir = propertyFile.getParentFile();
            System.out.println("parentDir: " + parentDir);
            if (!parentDir.exists()) {
                boolean dirCreated = parentDir.mkdirs();
                if (dirCreated) {
                    System.out.println("Directory created"+parentDir.getAbsolutePath());
                }else {
                    System.out.println("Directory not created"+parentDir.getAbsolutePath());
                    return;
                }
            }

            if (!propertyFile.exists()) {
                System.out.println("Property file does not exist");
                boolean created = propertyFile.createNewFile();
                if (created){
                    System.out.println("Property file created"+propertyFile.getAbsolutePath());
                    System.out.println("Property file path: " + propertyFile.getAbsolutePath());
                }else {
                    System.out.println("Property file already exists"+propertyFile.getAbsolutePath());
                }
            }
        }catch (Exception e){
            System.out.println("Error Creating Property File " + e);
        }
    }

}
