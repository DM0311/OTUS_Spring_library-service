package com.otus.library.library_service.dto.response;

import com.otus.library.library_service.model.enums.Role;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class UserRespDto {

    private long id;

    private String email;

    private String userName;

    private String fullName;

    private boolean isBlocked;

    private LocalDateTime blockedUntil;

    private int penaltyPoints;

    private Set<Role> roles;

    private LocalDateTime createdAt;
}
