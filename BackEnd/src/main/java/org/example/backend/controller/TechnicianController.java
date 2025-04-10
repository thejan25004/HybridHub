package org.example.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.example.backend.dto.CustomerDTO;
import org.example.backend.dto.TechnicianDTO;
import org.example.backend.service.TechnicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/v1/technician")
@CrossOrigin
public class TechnicianController {

    @Autowired
    private TechnicianService technicianService;


@PostMapping(value = "/save", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
public ResponseEntity<String> saveTechnician(
        @Valid @RequestParam("technician") String technicianJson,
        @RequestParam(value = "photo", required = false) MultipartFile photo) throws JsonProcessingException {

    ObjectMapper objectMapper = new ObjectMapper();
    TechnicianDTO technicianDTO = objectMapper.readValue(technicianJson, TechnicianDTO.class);

    boolean isSaved = technicianService.addTechnician(technicianDTO, photo);

    if (isSaved) {
        return ResponseEntity.ok("Technician saved successfully!");
    } else {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error saving technician");
    }
}

    @PutMapping(value = "update", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> updateTechnician(
            @RequestParam("technician") String technicianJson,
            @RequestPart(value = "photo", required = false) MultipartFile photo) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        TechnicianDTO technicianDTO = objectMapper.readValue(technicianJson, TechnicianDTO.class);

        boolean res = technicianService.updateTechnician(technicianDTO, photo);
        return ResponseEntity.ok(res ? "Technician updated successfully" : "Failed to update technician");
    }


    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteTechnician(@PathVariable Integer id) {
        boolean res = technicianService.deleteTechnician(id);
        return ResponseEntity.ok(res ? "Technician deleted successfully" : "Failed to delete technician");
    }

    @GetMapping("/getAll")
    public List<TechnicianDTO> getAllTechnicians() {
        return technicianService.getAllTechnicians();
    }

    @GetMapping("get/{id}")
    public ResponseEntity<TechnicianDTO> getTechnicianById(@PathVariable Integer id) {
        TechnicianDTO technician = technicianService.getTechnicianById(id);
        return technician != null ? ResponseEntity.ok(technician) : ResponseEntity.notFound().build();
    }

    @GetMapping("getByname/{name}")
    public ResponseEntity<TechnicianDTO> getTechnicianByName(@PathVariable String name) {
        TechnicianDTO technicianDTO = technicianService.getTechnicianByName(name);
        return ResponseEntity.ok(technicianDTO);
    }
}
