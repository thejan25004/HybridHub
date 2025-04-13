package org.example.backend.repo;

import org.example.backend.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PaymentRepo extends JpaRepository<Payment, Integer> {
    List<Payment> findByBookingBookingId(Integer bookingId);
    List<Payment> findByPaymentStatus(Payment.PaymentStatus status);
    List<Payment> findByPaymentDateBetween(Date startDate, Date endDate);
    Long countByPaymentMethod(Payment.PaymentMethod method);
    Long countByPaymentStatus(Payment.PaymentStatus status);
}