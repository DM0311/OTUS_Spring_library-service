package com.otus.library.library_service.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class BookReqDto {

    @NotBlank(message = "Название обязательно")
    private String title;

    private String year;

    private String description;

    @NotNull(message = "Укажите общее количество экземпляров")
    @Min(value = 1, message = "Количество должно быть больше 0")
    private Integer totalCopies;

    @NotNull(message = "Укажите количество доступных экземпляров")
    @Min(value = 0, message = "Количество не может быть отрицательным")
    private Integer availableCopies;

    private Set<Long> authorIds;

    private Set<Long> genreIds;
}
