package com.alaa.MoviesApp.mapper;

import com.alaa.MoviesApp.dto.MetaData;
import com.alaa.MoviesApp.dto.Pagination;
import org.springframework.data.domain.Page;

public class PaginationMetaDataMapper {

    private PaginationMetaDataMapper(){
        /*
          equal to @NoArgsConstructor(access = AccessLevel.PRIVATE)
         */
    }

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
