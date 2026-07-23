package com.otus.library.library_service.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentReqDto {

    @NotNull(message = "ID пользователя обязательно")
    private Long userId;

    @NotNull(message = "ID книги обязательно")
    private Long bookId;

    @Min(value = 1, message = "Рейтинг должен быть от 1 до 5")
    @Max(value = 5, message = "Рейтинг должен быть от 1 до 5")
    private int rating;

    private String commentText;
}
