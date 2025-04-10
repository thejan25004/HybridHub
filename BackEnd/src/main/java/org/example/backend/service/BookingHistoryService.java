package org.example.backend.service;

import org.example.backend.dto.BookingHistoryDTO;
import org.example.backend.entity.BookingHistory;
import org.example.backend.repo.BookingHistoryRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingHistoryService {

    @Autowired
    private BookingHistoryRepo bookingHistoryRepo;

    @Autowired
    private ModelMapper modelMapper;

    public boolean saveBookingHistory(BookingHistoryDTO bookingHistoryDTO) {
        try {
            BookingHistory bookingHistory = modelMapper.map(bookingHistoryDTO, BookingHistory.class);
            bookingHistoryRepo.save(bookingHistory);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<BookingHistoryDTO> getAllBookingHistories() {
        return bookingHistoryRepo.findAll().stream()
                .map(history -> modelMapper.map(history, BookingHistoryDTO.class))
                .collect(Collectors.toList());
    }

    public List<BookingHistoryDTO> getBookingHistoryByBookingId(int bookingId) {
        return bookingHistoryRepo.findByBookingId(bookingId).stream()
                .map(history -> modelMapper.map(history, BookingHistoryDTO.class))
                .collect(Collectors.toList());
    }

    public BookingHistoryDTO getLatestStatusByBookingId(int bookingId) {
        BookingHistory history = bookingHistoryRepo.findTopByBookingIdOrderByUpdateDateDesc(bookingId);
        return history != null ? modelMapper.map(history, BookingHistoryDTO.class) : null;
    }
}