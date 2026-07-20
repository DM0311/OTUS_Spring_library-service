package com.otus.library.library_service.dto.response;

import com.otus.library.library_service.model.enums.ActionType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuditLogRespDto {

    private Long id;

    private Long userId;

    private String userName;

    private String userFullName;

    private ActionType actionType;

    private String entityType;

    private Long entityId;

    private String details;

    private String endpoint;

    private String httpMethod;

    private LocalDateTime createdAt;
}
