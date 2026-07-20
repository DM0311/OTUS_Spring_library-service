package com.otus.library.library_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GenreReqDto {

    @NotBlank(message = "Название жанра обязательно")
    private String name;
}
