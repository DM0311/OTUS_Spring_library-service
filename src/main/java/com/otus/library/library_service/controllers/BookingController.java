package com.otus.library.library_service.controllers;

import com.otus.library.library_service.dto.request.BookingReqDto;
import com.otus.library.library_service.dto.response.BookingRespDto;
import com.otus.library.library_service.events.AuditEvent;
import com.otus.library.library_service.model.entity.Booking;
import com.otus.library.library_service.model.entity.User;
import com.otus.library.library_service.model.enums.ActionType;
import com.otus.library.library_service.services.BookingService;
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
public class BookingController {

    private final BookingService bookingService;

    private final ApplicationEventPublisher eventPublisher;

    private User getCurrentUser() {
        return null; // TODO: заменить на SecurityContext
    }

    @PostMapping("/api/booking")
    @ResponseStatus(HttpStatus.CREATED)
    public BookingRespDto createBooking(@Valid @RequestBody BookingReqDto request,
                                        HttpServletRequest httpRequest) {
        BookingRespDto bookingResp = bookingService.createBooking(
                request.getUserId(),
                request.getBookId(),
                request.getDaysToAdd()
        );

        eventPublisher.publishEvent(AuditEvent.builder()
                .user(getCurrentUser())
                .actionType(ActionType.BOOKING_CREATE)
                .entityType(Booking.class.getSimpleName())
                .entityId(bookingResp.getId())
                .details(String.format("Пользователь %d взял книгу %d", request.getUserId(), request.getBookId()))
                .endpoint(httpRequest.getRequestURI())
                .httpMethod(httpRequest.getMethod())
                .build()
        );

        return bookingResp;
    }

    @PutMapping("/api/booking/{id}/prolong")
    public BookingRespDto prolongBooking(@PathVariable Long id,
                                         HttpServletRequest httpRequest) {
        BookingRespDto bookingResp = bookingService.extendBooking(id);
        eventPublisher.publishEvent(AuditEvent.builder()
                .user(getCurrentUser())
                .actionType(ActionType.BOOKING_CREATE)
                .entityType(Booking.class.getSimpleName())
                .entityId(id)
                .details("Продлено бронирование " + id)
                .endpoint(httpRequest.getRequestURI())
                .httpMethod(httpRequest.getMethod())
                .build()
        );
        return bookingResp;
    }

    @PutMapping("/api/booking/{id}/return")
    public BookingRespDto returnBook(@PathVariable Long id,
                                     HttpServletRequest httpRequest) {
        BookingRespDto bookingResp = bookingService.returnBook(id);
        eventPublisher.publishEvent(AuditEvent.builder()
                .user(getCurrentUser())
                .actionType(ActionType.BOOKING_RETURN)
                .entityType(Booking.class.getSimpleName())
                .entityId(id)
                .details("Возврат книги по бронированию " + id)
                .endpoint(httpRequest.getRequestURI())
                .httpMethod(httpRequest.getMethod())
                .build());
        return bookingResp;
    }

    @DeleteMapping("/api/booking/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelBooking(@PathVariable Long id,
                              HttpServletRequest httpRequest) {
        bookingService.cancelBooking(id);
        eventPublisher.publishEvent(AuditEvent.builder()
                .user(getCurrentUser())
                .actionType(ActionType.BOOKING_CANCEL)
                .entityType(Booking.class.getSimpleName())
                .entityId(id)
                .details("Отмена бронирования " + id)
                .endpoint(httpRequest.getRequestURI())
                .httpMethod(httpRequest.getMethod())
                .build());
    }

    @GetMapping("/api/booking/my")
    public List<BookingRespDto> getMyBookings(@RequestParam Long userId) {
        return bookingService.getBookingsByUser(userId);
    }

    @GetMapping("/api/booking/admin")
    public List<BookingRespDto> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/api/booking/admin/{bookingId}")
    public BookingRespDto getBookingById(@PathVariable Long
                                                     bookingId) {
        return bookingService.getBookingById(bookingId);
    }
}
