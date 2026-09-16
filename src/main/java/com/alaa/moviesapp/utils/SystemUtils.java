package com.alaa.MoviesApp.utils;

import com.alaa.MoviesApp.service.SystemPropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SystemUtils {

    private final SystemPropertyService systemPropertyService;

    public static String generateUUIDCode(){
        return UUID.randomUUID().toString();
    }

    public static String generateCorrelationId() {
        String dateStr = LocalDateTime.now(ZoneOffset.UTC).toString();
        return UUID.randomUUID() + "_" + dateStr;
    }

    public Pageable buildPageableObj(Integer pageNumber) {
        int pageSize = Integer.parseInt(systemPropertyService.getProperty("app.page.size"));
        int pageIndex = Math.max(pageNumber - 1, 0);
        return PageRequest.of(pageIndex,pageSize);
    }
}
