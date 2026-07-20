package com.otus.library.library_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthorReqDto {

    @NotBlank(message = "Полное имя автора обязательно")
    private String fullName;
}
