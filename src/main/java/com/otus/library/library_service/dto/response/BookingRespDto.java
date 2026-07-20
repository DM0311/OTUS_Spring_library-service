package com.otus.library.library_service.dto.response;

import com.otus.library.library_service.model.enums.BookingStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingRespDto {

    private Long id;

    private Long userId;

    private String userName;

    private Long bookId;

    private String bookTitle;

    private LocalDateTime bookingDate;

    private LocalDateTime dueDate;

    private LocalDateTime actualReturnDate;

    private BookingStatus status;

    private boolean extended;

    private int extensionCount;

    private LocalDateTime createdAt;

}
