package com.alaa.MoviesApp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomPageDto<T> {

    private List<T> content;
    private long totalElements;
    private int totalPages;
    private boolean isFirst;
    private boolean isLast;


}
