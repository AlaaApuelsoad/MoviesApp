package com.alaa.MoviesApp;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@RequiredArgsConstructor
public class StartupConnectionChecker implements CommandLineRunner {

    private final DataSource dataSource;
    private final RedisTemplate<String,Object> redisTemplate;

    @Override
    public void run(String... args) {
        checkDatabaseConnection();
        checkRedisConnection();
    }

    private void checkDatabaseConnection(){
        try {
            dataSource.getConnection().close();
            System.out.println("Database Connection Successful");
        } catch (Exception e) {
            System.out.println("Database Connection Failed: " + e.getMessage());
        }
    }

    private void checkRedisConnection(){
        try {
            redisTemplate.opsForValue().get("test");
            System.out.println("Redis Connection Successful");
        }catch (Exception e){
            System.out.println("Redis Connection Failed: " + e.getMessage());
        }
    }
}
