package org.example.backend.repo;

import org.example.backend.entity.BookingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingHistoryRepo extends JpaRepository<BookingHistory, Integer> {
    List<BookingHistory> findByBookingId(int bookingId);
    BookingHistory findTopByBookingIdOrderByUpdateDateDesc(int bookingId);
}