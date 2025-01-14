package com.fawry.MoviesApp.mapper;

import com.fawry.MoviesApp.dto.CustomPageDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class PageMapper {


    public <T> CustomPageDto<T> customPageDto(Page<T> page) {

        return new CustomPageDto<>(
                page.getContent(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }
}
