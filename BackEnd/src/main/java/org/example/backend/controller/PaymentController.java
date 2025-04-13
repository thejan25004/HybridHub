package org.example.backend.controller;

import org.example.backend.dto.PaymentDTO;
import org.example.backend.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/payment")
@CrossOrigin
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("save")
    public ResponseEntity<String> savePayment(@RequestBody PaymentDTO paymentDTO) {
        boolean res = paymentService.savePayment(paymentDTO);
        return ResponseEntity.status(res ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST)
                .body(res ? "Payment saved successfully" : "Failed to save payment");
    }

    @PutMapping("update")
    public ResponseEntity<String> updatePayment(@RequestBody PaymentDTO paymentDTO) {
        boolean res = paymentService.updatePayment(paymentDTO);
        return ResponseEntity.ok(res ? "Payment updated successfully" : "Failed to update payment");
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deletePayment(@PathVariable Integer id) {
        boolean res = paymentService.deletePayment(id);
        return ResponseEntity.ok(res ? "Payment deleted successfully" : "Failed to delete payment");
    }

    @GetMapping("getAll")
    public ResponseEntity<List<PaymentDTO>> getAllPayments() {
        List<PaymentDTO> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    @GetMapping("getById/{id}")
    public ResponseEntity<PaymentDTO> getPaymentById(@PathVariable Integer id) {
        try {
            PaymentDTO payment = paymentService.getPaymentById(id);
            return ResponseEntity.ok(payment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("getByBookingId/{bookingId}")
    public ResponseEntity<List<PaymentDTO>> getPaymentsByBookingId(@PathVariable Integer bookingId) {
        List<PaymentDTO> payments = paymentService.getPaymentsByBookingId(bookingId);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("method-stats")
    public ResponseEntity<Map<String, Long>> getPaymentMethodStats() {
        Map<String, Long> stats = paymentService.getPaymentMethodStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("summary")
    public ResponseEntity<Map<String, Object>> getPaymentSummary() {
        Map<String, Object> summary = paymentService.getPaymentSummary();
        return ResponseEntity.ok(summary);
    }
}