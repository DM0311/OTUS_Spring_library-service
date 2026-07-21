package com.otus.library.library_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

public record ErrorResponseDto (
     int status,
     String message,
     LocalDateTime timestamp
){
    
}
