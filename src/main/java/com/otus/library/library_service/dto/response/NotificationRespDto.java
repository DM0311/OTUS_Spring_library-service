package com.otus.library.library_service.dto.response;

import com.otus.library.library_service.model.enums.NotificationType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationRespDto {

    private Long id;

    private Long userId;

    private String userName;

    private NotificationType type;

    private String message;

    private boolean isRead;

    private LocalDateTime createdAt;
}
