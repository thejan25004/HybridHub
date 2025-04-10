package org.example.backend.dto;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ServiceHistoryDTO {
    private ServiceDTO service;
    private int totalBookings;
    private List<BookingDTO> bookings;
    private Map<Integer, Long> bookingsByCustomer; // Customer ID to booking count
}