package com.alaa.moviesapp;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitialization;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Component
@DependsOnDatabaseInitialization
@RequiredArgsConstructor
public class StartupConnectionChecker implements CommandLineRunner {

    private final DataSource dataSource;
    private final CacheManager cacheManager;
    private final RedisTemplate<String,Object> redisTemplate;
    private final Logger logger = LoggerFactory.getLogger(StartupConnectionChecker.class);

    @Override
    public void run(String @NonNull ... args) {
        checkDatabaseConnection();
        checkRedisConnection();
    }

    private void checkDatabaseConnection(){
        try (Connection connection = dataSource.getConnection()){
            if (connection.isValid(2)){
                logger.info("Database Connection Successful: {}",
                        connection.getMetaData().getURL());
            }
        } catch (Exception e) {
            logger.error("Database Connection Failed: {}", e.getMessage());
        }
    }

    private void checkRedisConnection() {
        logger.info("CacheManager in use: {}", cacheManager.getClass().getName());
        try {
            String pingKey = "connection-check";
            redisTemplate.opsForValue().set(pingKey, "ok");
            Object result = redisTemplate.opsForValue().get(pingKey);
            redisTemplate.delete(pingKey);

            if ("ok".equals(result)) {
                logger.info("Redis connection successful");
            } else {
                logger.warn("Redis responded but value mismatch: {}", result);
            }
        } catch (Exception e) {
            logger.error("Redis connection failed: {}", e.getMessage(), e);
        }
    }
}
