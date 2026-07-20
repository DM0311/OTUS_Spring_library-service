package com.otus.library.library_service.controllers;

import com.otus.library.library_service.dto.request.UserReqDto;
import com.otus.library.library_service.dto.response.UserRespDto;
import com.otus.library.library_service.events.AuditEvent;
import com.otus.library.library_service.model.entity.User;
import com.otus.library.library_service.model.enums.ActionType;
import com.otus.library.library_service.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    private final ApplicationEventPublisher eventPublisher;

    @GetMapping("api/user")
    public List<UserRespDto> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("api/user/{id}")
    public UserRespDto getUserById(@PathVariable("id") long id) {
        return userService.getById(id);
    }

    @PostMapping("api/user/register")
    public UserRespDto createUser(@Valid @RequestBody UserReqDto request, HttpServletRequest servletRequest) {
        UserRespDto userRespDto = userService.create(request);
        eventPublisher.publishEvent(AuditEvent.builder()
                .user(null)
                .actionType(ActionType.USER_CREATE)
                .entityType(User.class.getSimpleName())
                .entityId(userRespDto.getId())
                .details("Создан пользователь " + userRespDto.getFullName() + " с id: " + userRespDto.getId())
                .endpoint(servletRequest.getRequestURI())
                .httpMethod(servletRequest.getMethod())
                .build());
        return userRespDto;
    }

    @DeleteMapping("api/user/{id}")
    public void deleteUser(@PathVariable("id") long id, HttpServletRequest servletRequest) {
        userService.deleteById(id);
        eventPublisher.publishEvent(AuditEvent.builder()
                .user(null)
                .actionType(ActionType.USER_DELETE)
                .entityType(User.class.getSimpleName())
                .entityId(id)
                .details("Удалён пользователь с id: " + id)
                .endpoint(servletRequest.getRequestURI())
                .httpMethod(servletRequest.getMethod())
                .build());
    }
}
