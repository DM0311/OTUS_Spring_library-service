package com.otus.library.library_service.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentRespDto {

    private Long id;

    private Long userId;

    private String userName;

    private Long bookId;

    private String bookTitle;

    private int rating;

    private String commentText;

    private LocalDateTime createdAt;


}
