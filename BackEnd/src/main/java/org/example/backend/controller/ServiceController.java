//package org.example.backend.controller;
//
//import org.example.backend.dto.CustomerDTO;
//import org.example.backend.dto.ServiceDTO;
//import org.example.backend.service.ServiceService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("api/v1/service")
//@CrossOrigin
//public class ServiceController {
//
//    @Autowired
//    private ServiceService serviceService;
//
//    @PostMapping(value = "save", consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<String> saveService(@RequestBody ServiceDTO serviceDTO) {
//        boolean res = serviceService.addService(serviceDTO);
//        return ResponseEntity.ok(res ? "Service saved successfully" : "Failed to save service");
//    }
//
//    @PutMapping(value = "update", consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<String> updateService(@RequestBody ServiceDTO serviceDTO) {
//        boolean res = serviceService.updateService(serviceDTO);
//        return ResponseEntity.ok(res ? "Service updated successfully" : "Failed to update service");
//    }
//
//    @DeleteMapping("delete/{id}")
//    public ResponseEntity<String> deleteService(@PathVariable Integer id) {
//        boolean res = serviceService.deleteService(id);
//        return ResponseEntity.ok(res ? "Service deleted successfully" : "Failed to delete service");
//    }
//
//    @GetMapping("getAll")
//    public ResponseEntity<List<ServiceDTO>> getAllServices() {
//        List<ServiceDTO> services = serviceService.getAllServices();
//        return ResponseEntity.ok(services);
//    }
//
//
//
//    @GetMapping("getByserviceName/{serviceName}")
//    public ResponseEntity<ServiceDTO> getServiceByServiceName(@PathVariable String serviceName) {
//        ServiceDTO serviceDTO = serviceService.getServiceByServiceName(serviceName);
//        return ResponseEntity.ok(serviceDTO);
//    }
//
//    @GetMapping("/get/{id}")
//    public ResponseEntity<ServiceDTO> getServiceById(@PathVariable Integer id) {
//        ServiceDTO serviceDTO = serviceService.getServiceById(id);
//        if (serviceDTO != null) {
//            return ResponseEntity.ok(serviceDTO);
//        }
//        return ResponseEntity.notFound().build();
//    }
//}
package org.example.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.backend.dto.ServiceDTO;
import org.example.backend.dto.ServiceHistoryDTO;
import org.example.backend.dto.ValidationDTO;
import org.example.backend.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/service")
@CrossOrigin
public class ServiceController {

    @Autowired
    private ServiceService serviceService;

    @PostMapping(value = "/save", consumes = { "application/json", "multipart/form-data" })
    public ResponseEntity<ValidationDTO> saveService(
            @RequestParam("service") String serviceJson,
            @RequestParam(value = "photo", required = false) MultipartFile photo) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        ServiceDTO serviceDTO = objectMapper.readValue(serviceJson, ServiceDTO.class);

        // Manual Validation
        Map<String, String> errors = validateServiceDTO(serviceDTO);
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(new ValidationDTO("Validation failed", 400, errors));
        }

        boolean isSaved = serviceService.addService(serviceDTO, photo);

        if (isSaved) {
            return ResponseEntity.ok(new ValidationDTO("Service saved successfully!", 200, null));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ValidationDTO("Error saving service", 400, null));
        }
    }

    private Map<String, String> validateServiceDTO(ServiceDTO serviceDTO) {
        Map<String, String> errors = new HashMap<>();

        if (serviceDTO.getServiceName() == null || serviceDTO.getServiceName().trim().isEmpty()) {
            errors.put("serviceName", "Service name cannot be empty");
        } else if (serviceDTO.getServiceName().length() > 100) {
            errors.put("serviceName", "Service name must not exceed 100 characters");
        }

        if (serviceDTO.getDescription() == null || serviceDTO.getDescription().trim().isEmpty()) {
            errors.put("description", "Description cannot be empty");
        }

        if (serviceDTO.getPrice() <= 0) {
            errors.put("price", "Price must be a positive value");
        }

        return errors;
    }

    @PutMapping(value = "/update", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> updateService(
            @RequestParam("service") String serviceJson,
            @RequestParam(value = "photo", required = false) MultipartFile photo) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        ServiceDTO serviceDTO = objectMapper.readValue(serviceJson, ServiceDTO.class);

        boolean res = serviceService.updateService(serviceDTO, photo);
        return ResponseEntity.ok(res ? "Service updated successfully" : "Failed to update service");
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteService(@PathVariable Integer id) {
        boolean res = serviceService.deleteService(id);
        return ResponseEntity.ok(res ? "Service deleted successfully" : "Failed to delete service");
    }

    @GetMapping("/getAll")
    public List<ServiceDTO> getAllServices() {
        return serviceService.getAllServices();
    }

    @GetMapping("get/{id}")
    public ResponseEntity<ServiceDTO> getServiceById(@PathVariable Integer id) {
        ServiceDTO service = serviceService.getServiceById(id);
        return service != null ? ResponseEntity.ok(service) : ResponseEntity.notFound().build();
    }

    @GetMapping("getByserviceName/{serviceName}")
    public ResponseEntity<ServiceDTO> getServiceByServiceName(@PathVariable String serviceName) {
        ServiceDTO serviceDTO = serviceService.getServiceByServiceName(serviceName);
        return ResponseEntity.ok(serviceDTO);
    }



    /**
     * Get service history with booking statistics for a specific service
     * @param id The ID of the service
     * @return ServiceHistoryDTO with service details and booking statistics
     */
    @GetMapping("history/{id}")
    public ResponseEntity<ServiceHistoryDTO> getServiceHistory(@PathVariable Integer id) {
        try {
            ServiceHistoryDTO history = serviceService.getServiceHistory(id);
            return ResponseEntity.ok(history);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get service booking analytics for all services
     * @return Map of service names to booking counts
     */
    @GetMapping("booking-analytics")
    public ResponseEntity<Map<String, Long>> getServiceBookingAnalytics() {
        Map<String, Long> analytics = serviceService.getServiceBookingAnalytics();
        return ResponseEntity.ok(analytics);
    }
}