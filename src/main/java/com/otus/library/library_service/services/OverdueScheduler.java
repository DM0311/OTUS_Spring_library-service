package com.otus.library.library_service.services;

import com.otus.library.library_service.model.entity.Booking;
import com.otus.library.library_service.model.entity.User;
import com.otus.library.library_service.model.enums.BookingStatus;
import com.otus.library.library_service.repositories.BookingRepository;
import com.otus.library.library_service.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
public class OverdueScheduler {

    private final BookingRepository bookingRepository;

    private final UserRepository userRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void checkOverdueBookings() {

        log.info("Запуск проверки просроченных бронирований");

        LocalDateTime now = LocalDateTime.now();
        List<Booking> overdueBookings = bookingRepository.findByStatusAndDueDateBefore(BookingStatus.ACTIVE, now);

        if (overdueBookings.isEmpty()) {
            log.info("Просроченных бронирований нет");
            return;
        }

        for (Booking booking : overdueBookings) {
            processOverdue(booking);
        }

        log.info("Проверка завершена, обработано {} просроченных бронирований", overdueBookings.size());
    }

    private void processOverdue(Booking booking) {

        User user = booking.getUser();
        int penalty = user.getPenaltyPoints() + 1;
        user.setPenaltyPoints(penalty);

        if (penalty >= 10) {
            user.setBlocked(true);
            user.setBlockedUntil(LocalDateTime.now().plusDays(30));

            log.warn("Пользователь {} (ID: {}) заблокирован за превышение штрафных баллов ({} шт.)",
                    user.getUserName(), user.getId(), penalty);
            // TODO: создать уведомление о блокировке
        } else {
            log.info("Пользователю {} начислен штрафной балл, всего {}", user.getUserName(), penalty);
            // TODO: создать уведомление о начислении штрафа
        }

        userRepository.save(user);

        booking.setStatus(BookingStatus.OVERDUE);
        bookingRepository.save(booking);

        // TODO: создать уведомление о просрочке
    }

    @Scheduled(cron = "0 10 0 * * ?")
    @Transactional
    public void unblockUsers() {

        log.info("Запуск разблокировки пользователей");

        List<User> blockedUsers = userRepository.findByIsBlockedTrueAndBlockedUntilBefore(LocalDateTime.now());

        if (blockedUsers.isEmpty()) {
            log.info("Пользователей для разблокировки нет");
            return;
        }

        for (User user : blockedUsers) {
            user.setBlocked(false);
            user.setBlockedUntil(null);
            userRepository.save(user);
            log.info("Пользователь {} (ID: {}) разблокирован", user.getUserName(), user.getId());
            // TODO: создать уведомление о разблокировке
        }
    }
}
