//package org.example.backend.controller;
//
//import org.example.backend.dto.TestimonialDTO;
//import org.example.backend.service.TestimonialService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.List;
//
//@RestController
//@RequestMapping("api/v1/testimonial")
//@CrossOrigin
//public class TestimonialController {
//
//    @Autowired
//    private TestimonialService testimonialService;
//
//    @PostMapping("save")
//    public ResponseEntity<String> saveTestimonial(@RequestParam("name") String name,
//                                                  @RequestParam("profession") String profession,
//                                                  @RequestParam("message") String message,
//                                                  @RequestParam(value = "photo", required = false) MultipartFile photo) {
//        try {
//            byte[] photoBytes = (photo != null && !photo.isEmpty()) ? photo.getBytes() : null;
//            TestimonialDTO testimonialDTO = new TestimonialDTO(name, profession, message, photoBytes);
//            boolean res = testimonialService.addTestimonial(testimonialDTO);
//            return ResponseEntity.ok(res ? "Testimonial saved successfully" : "Failed to save testimonial");
//        } catch (IOException e) {
//            return ResponseEntity.badRequest().body("Error processing image");
//        }
//    }
//
//
//    @GetMapping("getAll")
//    public ResponseEntity<List<TestimonialDTO>> getAllTestimonials() {
//        List<TestimonialDTO> testimonials = testimonialService.getAllTestimonials();
//        return ResponseEntity.ok(testimonials);
//    }
//}



package org.example.backend.controller;

import org.example.backend.dto.TestimonialDTO;
import org.example.backend.entity.Testimonial;
import org.example.backend.service.TestimonialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/testimonial")
@CrossOrigin(origins = "*")
public class TestimonialController {
    @Autowired
    private TestimonialService testimonialService;

    private static final String UPLOAD_DIR = "C:/uploads"; // Directory to save uploaded files
    private static final String UPLOAD_URL_PREFIX = "/uploads/";

    @Value("${upload.path:C:/uploads}")
    private String uploadPath;

    @PostMapping("/save")
    public ResponseEntity<String> saveTestimonial(
            @RequestParam("name") String name,
            @RequestParam("profession") String profession,
            @RequestParam("message") String message,
            @RequestParam(value = "photo", required = false) MultipartFile photo) {

        try {
            Path uploadDir = Paths.get(uploadPath).toAbsolutePath().normalize();
            Files.createDirectories(uploadDir);

            String photoPath = null;
            if (photo != null && !photo.isEmpty()) {
                // Generate a unique filename
                String originalFilename = photo.getOriginalFilename();
                String fileExtension = "";
                if (originalFilename != null && originalFilename.contains(".")) {
                    fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                }
                String newFilename = UUID.randomUUID() + fileExtension;

                // Save the file
                Path targetLocation = uploadDir.resolve(newFilename);
                Files.copy(photo.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

                // Store relative path for frontend
                photoPath = "/uploads/" + newFilename;
            }

            TestimonialDTO testimonialDTO = new TestimonialDTO(name, profession, message, photoPath);
            boolean res = testimonialService.addTestimonial(testimonialDTO);

            return ResponseEntity.ok(res ? "Testimonial saved successfully" : "Failed to save testimonial");

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error processing image: " + e.getMessage());
        }
    }
    @GetMapping("/getAll")
    public ResponseEntity<List<TestimonialDTO>> getAllTestimonials() {
        List<TestimonialDTO> testimonials = testimonialService.getAllTestimonials();
        return ResponseEntity.ok(testimonials);
    }
}
