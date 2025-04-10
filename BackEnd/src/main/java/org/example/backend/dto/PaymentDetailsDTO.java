package org.example.backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentDetailsDTO {
    private PaymentDTO payment;
    private BookingDTO booking;
    private CustomerDTO customer;
    private ServiceDTO service;
    private TechnicianDTO technician;
}