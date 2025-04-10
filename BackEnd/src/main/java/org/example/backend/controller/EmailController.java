//package org.example.backend.controller;
//
//import org.example.backend.dto.EmailDTO;
//import org.example.backend.service.EmailService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("api/v1/email")
//@CrossOrigin
//public class EmailController {
//
//    @Autowired
//    private EmailService emailService;
//
//    @PostMapping("/send")
//    public ResponseEntity<String> sendEmail(@RequestBody EmailDTO emailDTO) {
//        try {
//            emailService.sendEmail(emailDTO.getTo(), emailDTO.getSubject(), emailDTO.getBody());
//            return ResponseEntity.ok("Email sent successfully");
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Failed to send email: " + e.getMessage());
//        }
//    }
//}

package org.example.backend.controller;

import org.example.backend.dto.EmailDTO;
import org.example.backend.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/email")
@CrossOrigin(origins = "*")
public class EmailController {

    @Autowired
    private EmailService emailService;



    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody EmailDTO emailDTO) {
        try {
            emailService.sendEmail(
                    emailDTO.getTo(),
                    emailDTO.getSubject(),
                    emailDTO.getBody()
            );
            return ResponseEntity.ok("Email sent successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Email sending failed: " + e.getMessage());
        }
    }


}