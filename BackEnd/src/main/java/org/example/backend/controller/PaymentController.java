package org.example.backend.controller;

import org.example.backend.dto.PaymentDTO;
import org.example.backend.dto.PaymentDetailsDTO;
import org.example.backend.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/payment")
@CrossOrigin
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<PaymentDTO> createPayment(@RequestBody PaymentDTO paymentDTO) {
        try {
            PaymentDTO savedPayment = paymentService.savePayment(paymentDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPayment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/update-status/{paymentId}")
    public ResponseEntity<String> updatePaymentStatus(
            @PathVariable int paymentId,
            @RequestParam String status) {
        boolean updated = paymentService.updatePaymentStatus(paymentId, status);
        return ResponseEntity.ok(updated ?
                "Payment status updated successfully" :
                "Failed to update payment status");
    }

    @GetMapping("/all")
    public ResponseEntity<List<PaymentDTO>> getAllPayments() {
        List<PaymentDTO> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentDTO> getPaymentById(@PathVariable int paymentId) {
        try {
            PaymentDTO payment = paymentService.getPaymentById(paymentId);
            return ResponseEntity.ok(payment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<PaymentDTO>> getPaymentsByBookingId(@PathVariable int bookingId) {
        List<PaymentDTO> payments = paymentService.getPaymentsByBookingId(bookingId);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/details/{paymentId}")
    public ResponseEntity<PaymentDetailsDTO> getPaymentDetails(@PathVariable int paymentId) {
        try {
            PaymentDetailsDTO details = paymentService.getPaymentDetails(paymentId);
            return ResponseEntity.ok(details);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/statistics/monthly-revenue")
    public ResponseEntity<Map<String, BigDecimal>> getMonthlyRevenue() {
        Map<String, BigDecimal> monthlyRevenue = paymentService.getMonthlyRevenue();
        return ResponseEntity.ok(monthlyRevenue);
    }

    @GetMapping("/statistics/payment-methods")
    public ResponseEntity<Map<String, Long>> getPaymentMethodStats() {
        Map<String, Long> stats = paymentService.getPaymentMethodStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/statistics/total-revenue")
    public ResponseEntity<BigDecimal> getTotalRevenue() {
        BigDecimal totalRevenue = paymentService.getTotalRevenue();
        return ResponseEntity.ok(totalRevenue);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<PaymentDTO>> getRecentPayments() {
        List<PaymentDTO> recentPayments = paymentService.getRecentPayments();
        return ResponseEntity.ok(recentPayments);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<PaymentDTO>> getPendingPayments() {
        List<PaymentDTO> pendingPayments = paymentService.getPendingPayments();
        return ResponseEntity.ok(pendingPayments);
    }
}