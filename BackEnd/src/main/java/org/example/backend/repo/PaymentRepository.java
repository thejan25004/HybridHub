package org.example.backend.repo;

import org.example.backend.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    List<Payment> findByBookingBookingId(int bookingId);

    List<Payment> findByPaymentStatus(Payment.PaymentStatus status);

    List<Payment> findByPaymentDateBetween(Date startDate, Date endDate);

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.paymentStatus = 'COMPLETED'")
    BigDecimal getTotalCompletedPayments();

    @Query("SELECT p.paymentMethod, COUNT(p) FROM Payment p GROUP BY p.paymentMethod")
    List<Object[]> countByPaymentMethod();

    @Query("SELECT FUNCTION('DATE_FORMAT', p.paymentDate, '%Y-%m') as month, SUM(p.amount) " +
            "FROM Payment p WHERE p.paymentStatus = 'COMPLETED' " +
            "GROUP BY FUNCTION('DATE_FORMAT', p.paymentDate, '%Y-%m')")
    List<Object[]> getMonthlyRevenue();
}