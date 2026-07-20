package com.otus.library.library_service.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookingReqDto {

    @NotNull(message = "ID пользователя обязательно")
    private Long userId;

    @NotNull(message = "ID книги обязательно")
    private Long bookId;

    private Integer daysToAdd;
}
