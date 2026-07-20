package com.otus.library.library_service.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class BookRespDto {
    private Long id;

    private String title;

    private String year;

    private String description;

    private Integer totalCopies;

    private Integer availableCopies;

    private Double rating;

    private LocalDateTime createdAt;

    private Set<AuthorRespDto> authors;

    private Set<GenreRespDto> genres;
}
