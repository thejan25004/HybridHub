package org.example.backend.service;

import org.example.backend.dto.PaymentDTO;
import org.example.backend.entity.Booking;
import org.example.backend.entity.Payment;
import org.example.backend.repo.BookingRepo;
import org.example.backend.repo.PaymentRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepo paymentRepo;

    @Autowired
    private BookingRepo bookingRepo;

    @Autowired
    private ModelMapper modelMapper;

    public boolean savePayment(PaymentDTO paymentDTO) {
        try {
            Payment payment = new Payment();

            // Set payment details
            payment.setAmount(paymentDTO.getAmount());
            payment.setPaymentDate(paymentDTO.getPaymentDate() != null ?
                    paymentDTO.getPaymentDate() : new Date());

            // Set payment method
            payment.setPaymentMethod(Payment.PaymentMethod.valueOf(
                    paymentDTO.getPaymentMethod().replace(" ", "_").toUpperCase()
            ));

            // Set payment status
            payment.setPaymentStatus(Payment.PaymentStatus.valueOf(
                    paymentDTO.getPaymentStatus().replace(" ", "_").toUpperCase()
            ));

            // Find and set booking
            Booking booking = bookingRepo.findById(paymentDTO.getBookingId())
                    .orElseThrow(() -> new RuntimeException("Booking not found"));
            payment.setBooking(booking);

            // Save payment
            paymentRepo.save(payment);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePayment(PaymentDTO paymentDTO) {
        if (paymentRepo.existsById(paymentDTO.getPaymentId())) {
            try {
                Payment payment = paymentRepo.findById(paymentDTO.getPaymentId()).get();

                // Update payment details
                payment.setAmount(paymentDTO.getAmount());
                if (paymentDTO.getPaymentDate() != null) {
                    payment.setPaymentDate(paymentDTO.getPaymentDate());
                }

                // Update payment method if provided
                if (paymentDTO.getPaymentMethod() != null) {
                    payment.setPaymentMethod(Payment.PaymentMethod.valueOf(
                            paymentDTO.getPaymentMethod().replace(" ", "_").toUpperCase()
                    ));
                }

                // Update payment status if provided
                if (paymentDTO.getPaymentStatus() != null) {
                    payment.setPaymentStatus(Payment.PaymentStatus.valueOf(
                            paymentDTO.getPaymentStatus().replace(" ", "_").toUpperCase()
                    ));
                }

                // Update booking if provided
                if (paymentDTO.getBookingId() > 0) {
                    Booking booking = bookingRepo.findById(paymentDTO.getBookingId())
                            .orElseThrow(() -> new RuntimeException("Booking not found"));
                    payment.setBooking(booking);
                }

                // Save updated payment
                paymentRepo.save(payment);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean deletePayment(Integer id) {
        if (paymentRepo.existsById(id)) {
            paymentRepo.deleteById(id);
            return true;
        }
        return false;
    }

    public List<PaymentDTO> getAllPayments() {
        return paymentRepo.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public PaymentDTO getPaymentById(Integer id) {
        Payment payment = paymentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        return convertToDTO(payment);
    }

    public List<PaymentDTO> getPaymentsByBookingId(Integer bookingId) {
        return paymentRepo.findByBookingBookingId(bookingId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Map<String, Long> getPaymentMethodStats() {
        Map<String, Long> stats = new HashMap<>();
        for (Payment.PaymentMethod method : Payment.PaymentMethod.values()) {
            stats.put(method.getDisplayName(), paymentRepo.countByPaymentMethod(method));
        }
        return stats;
    }

    public Map<String, Object> getPaymentSummary() {
        Map<String, Object> summary = new HashMap<>();

        // Total payments count
        summary.put("totalPayments", paymentRepo.count());

        // Completed payments count
        Long completedCount = paymentRepo.countByPaymentStatus(Payment.PaymentStatus.COMPLETED);
        summary.put("completedPayments", completedCount);

        // Pending payments count
        Long pendingCount = paymentRepo.countByPaymentStatus(Payment.PaymentStatus.PENDING);
        summary.put("pendingPayments", pendingCount);

        // Failed payments count
        Long failedCount = paymentRepo.countByPaymentStatus(Payment.PaymentStatus.FAILED);
        summary.put("failedPayments", failedCount);

        return summary;
    }


    private PaymentDTO convertToDTO(Payment payment) {
        PaymentDTO dto = new PaymentDTO();
        dto.setPaymentId(payment.getPaymentId());
        dto.setBookingId(payment.getBooking().getBookingId());
        dto.setPaymentDate(payment.getPaymentDate());
        dto.setAmount(payment.getAmount());
        dto.setPaymentMethod(payment.getPaymentMethod().getDisplayName());
        dto.setPaymentStatus(payment.getPaymentStatus().getDisplayName());
        return dto;
    }
}