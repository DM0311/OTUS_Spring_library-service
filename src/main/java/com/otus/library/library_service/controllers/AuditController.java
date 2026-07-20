package com.otus.library.library_service.controllers;

import com.otus.library.library_service.dto.response.AuditLogRespDto;
import com.otus.library.library_service.services.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class AuditController {

    private final AuditService auditService;


    @GetMapping("/api/admin/auditLog")
    public List<AuditLogRespDto> getAllLogs() {
        return auditService.findAllLogsDesc();
    }
}
