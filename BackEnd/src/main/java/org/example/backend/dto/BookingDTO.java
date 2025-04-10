package org.example.backend.dto;

import lombok.*;

import java.sql.Time;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BookingDTO {
    private int bookingId;
    private int customerId;
    private int technicianId;
    private int serviceId;
    private Date bookingDate;
    private Time bookingTime;
    private Long mobileNumber;
}

