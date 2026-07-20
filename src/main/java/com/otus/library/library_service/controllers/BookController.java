package com.otus.library.library_service.controllers;

import com.otus.library.library_service.dto.request.BookReqDto;
import com.otus.library.library_service.dto.response.BookRespDto;
import com.otus.library.library_service.events.AuditEvent;
import com.otus.library.library_service.model.entity.Book;
import com.otus.library.library_service.model.enums.ActionType;
import com.otus.library.library_service.services.BookService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class BookController {

    private final BookService bookService;

    private final ApplicationEventPublisher eventPublisher;

    @GetMapping("api/book")
    public List<BookRespDto> getAllBooks() {
        return bookService.findAll();
    }

    @GetMapping("api/book/{id}")
    public BookRespDto getById(@PathVariable("id") long id) {
        return bookService.findById(id);
    }

    @GetMapping("api/book/find")
    public List<BookRespDto> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) Long genreId,
            @RequestParam(required = false) String year) {

        if(title==null & author==null & genreId==null & year==null){
            return bookService.findAll();
        }
        return bookService.searchBooks(title, author, genreId, year);
    }

    @PostMapping("/api/book")
    @ResponseStatus(HttpStatus.CREATED)
    public BookRespDto createBook(@Valid @RequestBody BookReqDto request, HttpServletRequest servletRequest) {
        BookRespDto bookRespDto = bookService.create(request);
        //TODO: добавить пользователя
        eventPublisher.publishEvent(AuditEvent.builder()
                .user(null)
                .actionType(ActionType.BOOK_CREATE)
                .entityType(Book.class.getSimpleName())
                .entityId(bookRespDto.getId())
                .details("Создана книга " + bookRespDto.getTitle() + " с id: " + bookRespDto.getId())
                .endpoint(servletRequest.getRequestURI())
                .httpMethod(servletRequest.getMethod())
                .build());
        return bookRespDto;
    }

    @PutMapping("api/book/{id}")
    public BookRespDto updateBook(@PathVariable Long id,
                                  @Valid @RequestBody BookReqDto request,
                                  HttpServletRequest httpRequest) {
        BookRespDto response = bookService.updateBook(id, request);

        eventPublisher.publishEvent(AuditEvent.builder()
                //TODO: добавить пользователя
                .user(null)
                .actionType(ActionType.BOOK_UPDATE)
                .entityType(Book.class.getSimpleName())
                .entityId(id)
                .details("Книга обновлена: " + response.getTitle())
                .endpoint(httpRequest.getRequestURI())
                .httpMethod(httpRequest.getMethod())
                .build()
        );

        return response;
    }

    @DeleteMapping("/api/book/{id}")
    public void deleteBook(@PathVariable("id") long id, HttpServletRequest servletRequest) {
        bookService.deleById(id);
        eventPublisher.publishEvent(AuditEvent.builder()
                .user(null)
                .actionType(ActionType.BOOK_DELETE)
                .entityType(Book.class.getSimpleName())
                .entityId(id)
                .details("Удалена книга с id: " + id)
                .endpoint(servletRequest.getRequestURI())
                .httpMethod(servletRequest.getMethod())
                .build());
    }
}
