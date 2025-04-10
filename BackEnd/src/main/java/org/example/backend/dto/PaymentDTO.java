package org.example.backend.dto;

import lombok.*;
import org.example.backend.entity.Payment;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentDTO {
    private int paymentId;
    private int bookingId;
    private Date paymentDate;
    private BigDecimal amount;
    private String paymentMethod;
    private String paymentStatus;

    // Additional details for frontend display
    private String customerName;
    private String serviceName;
    private Date bookingDate;
}