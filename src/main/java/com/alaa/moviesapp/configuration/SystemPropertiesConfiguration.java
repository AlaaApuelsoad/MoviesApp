package com.alaa.moviesapp.configuration;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;


@Component
@RequiredArgsConstructor
public class SystemPropertiesConfiguration {

    private final Logger logger = org.slf4j.LoggerFactory.getLogger(SystemPropertiesConfiguration.class);

    /*
    Environment interface represents the current environment in which the spring application is running, provides access
    to application properties, system properties, and environment variables.
     */
    private final Environment environment;

    @PostConstruct
    public void configReader() {
        String filePath = System.getProperty("user.home") + File.separator + ".application-properties";

        logger.info("===== Loaded Properties =====");

        createExternalPropertyFile(filePath);

        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {

            Properties properties = loadApplicationProperties();

            properties.store(outputStream, "System Properties");

            logger.info("You will find the application.properties file at: {}", filePath);

        } catch (IOException e) {
            throw new RuntimeException("Failed Writing Properties", e);
        } catch (Exception e) {
            logger.error("Unexpected Exception: {}", e.getMessage(), e);
        }
    }

    public void createExternalPropertyFile(String filePath) {
        logger.info("Creating External Properties File: {}", filePath);
        try {
            File propertyFile = new File(filePath);

            File parentDir = propertyFile.getParentFile();
            if (!parentDir.exists()) {
                boolean dirCreated = parentDir.mkdirs();
                if (dirCreated) {
                    logger.info("Directory created {}", parentDir.getAbsolutePath());
                } else {
                    logger.info("Directory not created {}", parentDir.getAbsolutePath());
                    return;
                }
            }

            if (!propertyFile.exists()) {
                logger.info("Property file does not exist");
                boolean created = propertyFile.createNewFile();
                if (created) {
                    logger.info("Property file created {}", propertyFile.getAbsolutePath());
                    logger.info("Property file path: {}", propertyFile.getAbsolutePath());
                } else {
                    logger.info("Property file already exists {}", propertyFile.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            logger.error("Error Creating Property File {}", e.getMessage());
        }
    }

    private Properties loadApplicationProperties() {
        Properties properties = new Properties();
        if (!(environment instanceof ConfigurableEnvironment configurableEnvironment)) {
            return properties;
        }
        MutablePropertySources propertySources = configurableEnvironment.getPropertySources();
        for (PropertySource<?> propertySource : propertySources) {

            if (!propertySource.getName().contains("Config resource")) {
                continue;
            }
            logger.info("Property Source: {}", propertySource.getName());
            addProperties(propertySource, properties);
        }
        return properties;
    }

    private void addProperties(PropertySource<?> propertySource, Properties properties) {
        Object source = propertySource.getSource();
        if (!(source instanceof Map<?, ?> map)) {
            return;
        }
        map.forEach((key, value) ->
                properties.setProperty(
                        key.toString(),
                        value != null ? value.toString() : ""
                ));
    }
}
