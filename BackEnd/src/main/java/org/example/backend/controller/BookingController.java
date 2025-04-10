package org.example.backend.controller;

import org.example.backend.dto.BookingDTO;
import org.example.backend.dto.BookingDetailsDTO;
import org.example.backend.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/booking")
@CrossOrigin
public class BookingController {

    @Autowired
    private BookingService bookingService;



    @PostMapping("save")
    public ResponseEntity<String> saveBooking(@RequestBody BookingDTO bookingDTO) {
        boolean res = bookingService.saveBooking(bookingDTO);
        return ResponseEntity.status(res ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST)
                .body(res ? "Booking saved successfully" : "Failed to save booking");
    }

    @PutMapping("update")
    public ResponseEntity<String> updateBooking(@RequestBody BookingDTO bookingDTO) {
        boolean res = bookingService.updateBooking(bookingDTO);
        return ResponseEntity.ok(res ? "Booking updated successfully" : "Failed to update booking");
    }


    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteBooking(@PathVariable Integer id) {
        boolean res = bookingService.deleteBooking(id);
        return ResponseEntity.ok(res ? "Booking deleted successfully" : "Failed to delete booking");
    }

    @GetMapping("getAll")
    public ResponseEntity<List<BookingDTO>> getAllBookings() {
        List<BookingDTO> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("monthly-stats")
    public ResponseEntity<Map<String, Long>> getMonthlyBookingStats() {
        Map<String, Long> monthlyStats = bookingService.getBookingCountByMonth();
        return ResponseEntity.ok(monthlyStats);
    }

    @GetMapping("total-bookings")
    public ResponseEntity<Long> getTotalBookings() {
        long count = bookingService.getTotalBookingsCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("new-bookings")
    public ResponseEntity<List<BookingDTO>> getNewBookings() {
        List<BookingDTO> newBookings = bookingService.getNewBookings();
        return ResponseEntity.ok(newBookings);
    }

    @GetMapping("details/{id}")
    public ResponseEntity<BookingDetailsDTO> getBookingDetails(@PathVariable Integer id) {
        try {
            BookingDetailsDTO details = bookingService.getBookingDetailsById(id);
            return ResponseEntity.ok(details);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

