package org.example.backend.service;

import org.example.backend.dto.*;
import org.example.backend.entity.Booking;
import org.example.backend.entity.Payment;
import org.example.backend.entity.User;
import org.example.backend.repo.BookingRepo;
import org.example.backend.repo.PaymentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BookingRepo bookingRepo;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ModelMapper modelMapper;

    public PaymentDTO savePayment(PaymentDTO paymentDTO) {
        try {
            // Find the booking
            Booking booking = bookingRepo.findById(paymentDTO.getBookingId())
                    .orElseThrow(() -> new RuntimeException("Booking not found"));

            // Create payment entity
            Payment payment = new Payment();
            payment.setBooking(booking);
            payment.setAmount(paymentDTO.getAmount());
            payment.setPaymentMethod(Payment.PaymentMethod.valueOf(paymentDTO.getPaymentMethod()));
            payment.setPaymentStatus(Payment.PaymentStatus.valueOf(paymentDTO.getPaymentStatus()));
            payment.setPaymentDate(new Date());

            // Save and return
            Payment savedPayment = paymentRepository.save(payment);
            return modelMapper.map(savedPayment, PaymentDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save payment: " + e.getMessage());
        }
    }

    public boolean updatePaymentStatus(int paymentId, String newStatus) {
        try {
            Payment payment = paymentRepository.findById(paymentId)
                    .orElseThrow(() -> new RuntimeException("Payment not found"));

            payment.setPaymentStatus(Payment.PaymentStatus.valueOf(newStatus));
            paymentRepository.save(payment);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<PaymentDTO> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public PaymentDTO getPaymentById(int paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        return convertToDTO(payment);
    }

    public List<PaymentDTO> getPaymentsByBookingId(int bookingId) {
        return paymentRepository.findByBookingBookingId(bookingId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public PaymentDetailsDTO getPaymentDetails(int paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        // Get booking details
        Booking booking = payment.getBooking();
        BookingDetailsDTO bookingDetails = bookingService.getBookingDetailsById(booking.getBookingId());

        // Create payment details DTO
        PaymentDetailsDTO detailsDTO = new PaymentDetailsDTO();
        detailsDTO.setPayment(convertToDTO(payment));
        detailsDTO.setBooking(bookingDetails.getBooking());
        detailsDTO.setCustomer(bookingDetails.getCustomer());
        detailsDTO.setService(bookingDetails.getService());
        detailsDTO.setTechnician(bookingDetails.getTechnician());

        return detailsDTO;
    }

    public Map<String, BigDecimal> getMonthlyRevenue() {
        Map<String, BigDecimal> revenueMap = new HashMap<>();
        List<Object[]> results = paymentRepository.getMonthlyRevenue();

        for (Object[] result : results) {
            String month = (String) result[0];
            BigDecimal amount = (BigDecimal) result[1];
            revenueMap.put(month, amount);
        }

        return revenueMap;
    }

    public Map<String, Long> getPaymentMethodStats() {
        Map<String, Long> statsMap = new HashMap<>();
        List<Object[]> results = paymentRepository.countByPaymentMethod();

        for (Object[] result : results) {
            Payment.PaymentMethod method = (Payment.PaymentMethod) result[0];
            Long count = (Long) result[1];
            statsMap.put(method.getDisplayName(), count);
        }

        return statsMap;
    }

    private PaymentDTO convertToDTO(Payment payment) {
        PaymentDTO dto = modelMapper.map(payment, PaymentDTO.class);

        if (payment.getBooking() != null) {
            dto.setBookingId(payment.getBooking().getBookingId());
            dto.setBookingDate(payment.getBooking().getBookingDate());

            if (payment.getBooking().getCustomer() != null) {
                User user = payment.getBooking().getCustomer().getUser();
                if (user != null) {
                    dto.setCustomerName(user.getName());
                }
            }

            if (payment.getBooking().getService() != null) {
                dto.setServiceName(payment.getBooking().getService().getServiceName());
            }
        }

        if (payment.getPaymentMethod() != null) {
            dto.setPaymentMethod(payment.getPaymentMethod().getDisplayName());
        }

        if (payment.getPaymentStatus() != null) {
            dto.setPaymentStatus(payment.getPaymentStatus().getDisplayName());
        }

        return dto;
    }


    public BigDecimal getTotalRevenue() {
        return paymentRepository.getTotalCompletedPayments();
    }

    public List<PaymentDTO> getRecentPayments() {
        // Get payments from the last 7 days
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -7);
        Date oneWeekAgo = cal.getTime();

        return paymentRepository.findByPaymentDateBetween(oneWeekAgo, new Date()).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<PaymentDTO> getPendingPayments() {
        return paymentRepository.findByPaymentStatus(Payment.PaymentStatus.PENDING).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}