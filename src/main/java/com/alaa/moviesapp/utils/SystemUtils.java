package com.alaa.moviesapp.utils;

import com.alaa.moviesapp.service.SystemPropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SystemUtils {

    private final SystemPropertyService systemPropertyService;

    public static String generateUUIDCode(){
        return UUID.randomUUID().toString();
    }

    public Pageable buildPageableObj(Integer pageNumber) {
        int pageSize = Integer.parseInt(systemPropertyService.getProperty("app.page.size"));
        int pageIndex = Math.max(pageNumber - 1, 0);
        return PageRequest.of(pageIndex,pageSize);
    }
}
