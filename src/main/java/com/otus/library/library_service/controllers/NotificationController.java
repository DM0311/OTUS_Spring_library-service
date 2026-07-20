package com.otus.library.library_service.controllers;

import com.otus.library.library_service.dto.response.NotificationRespDto;
import com.otus.library.library_service.mappers.NotificationMapper;
import com.otus.library.library_service.services.impl.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class NotificationController {

    private final NotificationService notificationService;

    private final NotificationMapper notificationMapper;

    @GetMapping("/api/notification/user/{userId}")
    public List<NotificationRespDto> getAllByUser(@PathVariable Long userId) {
        return notificationService.getAllNotificationsByUser(userId).stream()
                .map(notificationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/notification/user/{userId}/unread")
    public List<NotificationRespDto> getUnread(@PathVariable Long userId) {
        return notificationService.getUnreadNotifications(userId).stream()
                .map(notificationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @PutMapping("/api/notification/{id}/read")
    public void markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
    }

    @PutMapping("/api/notification/user/{userId}/read-all")
    public void markAllAsRead(@PathVariable Long userId) {
        notificationService.markAllAsRead(userId);
    }
}
