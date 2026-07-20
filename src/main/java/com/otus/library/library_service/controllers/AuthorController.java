package com.otus.library.library_service.controllers;

import com.otus.library.library_service.dto.request.AuthorReqDto;
import com.otus.library.library_service.dto.response.AuthorRespDto;
import com.otus.library.library_service.events.AuditEvent;
import com.otus.library.library_service.model.entity.Author;
import com.otus.library.library_service.model.enums.ActionType;
import com.otus.library.library_service.services.AuthorService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class AuthorController {

    private final AuthorService authorService;

    private final ApplicationEventPublisher eventPublisher;

    @GetMapping("api/author")
    List<AuthorRespDto> getAllAuthors() {
        return authorService.findAll();
    }

    @PostMapping("/api/author")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorRespDto createAuthor(@Valid @RequestBody AuthorReqDto request, HttpServletRequest servletRequest) {
        AuthorRespDto authorRespDto = authorService.create(request);
        eventPublisher.publishEvent(AuditEvent.builder()
                .user(null)
                .actionType(ActionType.AUTHOR_CREATE)
                .entityType(Author.class.getSimpleName())
                .entityId(authorRespDto.getId())
                .details("Создан автор " + authorRespDto.getFullName() + " с id: " + authorRespDto.getId())
                .endpoint(servletRequest.getRequestURI())
                .httpMethod(servletRequest.getMethod())
                .build());
        return authorRespDto;
    }

    @DeleteMapping("/api/author/{id}")
    public void deleteAuthor(@PathVariable("id") long id, HttpServletRequest servletRequest) {
        authorService.deleteById(id);
        eventPublisher.publishEvent(AuditEvent.builder()
                .user(null)
                .actionType(ActionType.AUTHOR_DELETE)
                .entityType(Author.class.getSimpleName())
                .entityId(id)
                .details("Удалён автор с id: " + id)
                .endpoint(servletRequest.getRequestURI())
                .httpMethod(servletRequest.getMethod())
                .build());
    }
}
