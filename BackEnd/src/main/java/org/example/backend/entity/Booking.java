package org.example.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookingId;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "technician_id", nullable = true)
    private Technician technician;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private org.example.backend.entity.Service service;

    @Temporal(TemporalType.DATE)
    private Date bookingDate;

    @Temporal(TemporalType.TIME)
    private Time bookingTime;

    private Long mobileNumber;
}
