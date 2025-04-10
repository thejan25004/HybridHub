package org.example.backend.controller;

import org.example.backend.dto.BookingHistoryDTO;
import org.example.backend.service.BookingHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/booking-history")
@CrossOrigin
public class BookingHistoryController {

    @Autowired
    private BookingHistoryService bookingHistoryService;

    @PostMapping("save")
    public ResponseEntity<String> saveBookingHistory(@RequestBody BookingHistoryDTO bookingHistoryDTO) {
        boolean res = bookingHistoryService.saveBookingHistory(bookingHistoryDTO);
        return ResponseEntity.status(res ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST)
                .body(res ? "Booking history saved successfully" : "Failed to save booking history");
    }

    @GetMapping("getAll")
    public ResponseEntity<List<BookingHistoryDTO>> getAllBookingHistories() {
        List<BookingHistoryDTO> histories = bookingHistoryService.getAllBookingHistories();
        return ResponseEntity.ok(histories);
    }

    @GetMapping("getByBookingId/{bookingId}")
    public ResponseEntity<List<BookingHistoryDTO>> getBookingHistoryByBookingId(@PathVariable Integer bookingId) {
        List<BookingHistoryDTO> histories = bookingHistoryService.getBookingHistoryByBookingId(bookingId);
        return ResponseEntity.ok(histories);
    }

    @GetMapping("getLatestStatus/{bookingId}")
    public ResponseEntity<BookingHistoryDTO> getLatestStatusByBookingId(@PathVariable Integer bookingId) {
        BookingHistoryDTO history = bookingHistoryService.getLatestStatusByBookingId(bookingId);
        return history != null ?
                ResponseEntity.ok(history) :
                ResponseEntity.notFound().build();
    }
}