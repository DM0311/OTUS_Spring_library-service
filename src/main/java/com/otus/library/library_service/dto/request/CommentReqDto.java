package com.otus.library.library_service.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentReqDto {

    @NotNull(message = "ID пользователя обязательно")
    private Long userId;

    @NotNull(message = "ID книги обязательно")
    private Long bookId;

    @Size(min = 1, max = 100, message = "Оценка от 1 до 5")
    private int rating;

    private String commentText;
}
