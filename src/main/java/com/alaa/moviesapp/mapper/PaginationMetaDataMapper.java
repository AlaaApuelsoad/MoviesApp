package com.alaa.moviesapp.mapper;

import com.alaa.moviesapp.dto.MetaData;
import com.alaa.moviesapp.dto.Pagination;
import org.springframework.data.domain.Page;

public class PaginationMetaDataMapper {

    public static <T> MetaData fromPage(Page<T> page) {
        Pagination pagination = new Pagination(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext(),
                page.hasPrevious()
        );

        return new MetaData(pagination);
    }
}
