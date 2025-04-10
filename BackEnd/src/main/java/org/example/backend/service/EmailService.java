package org.example.backend.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendEmail(String to, String subject, String body) {
        try {
            // Create a MIME message
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            // Use helper to easily set email properties
            MimeMessageHelper helper = new MimeMessageHelper(
                    mimeMessage,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );

            // Detailed logging for debugging
            logger.info("Sending email from: {}", fromEmail);
            logger.info("Sending email to: {}", to);
            logger.info("Email subject: {}", subject);

            // Set email details
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true); // Enable HTML content

            // Send the email
            mailSender.send(mimeMessage);

            logger.info("Email sent successfully to: {}", to);
        } catch (MessagingException e) {
            logger.error("Comprehensive email sending error", e);
            throw new RuntimeException("Detailed email sending failure", e);
        } catch (Exception e) {
            logger.error("Unexpected error during email sending", e);
            throw new RuntimeException("Unexpected email sending error", e);
        }
    }
}
