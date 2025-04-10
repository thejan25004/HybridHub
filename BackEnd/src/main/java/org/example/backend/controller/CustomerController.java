package org.example.backend.controller;

import jakarta.validation.Valid;
import org.example.backend.dto.CustomerDTO;
import org.example.backend.dto.ValidationDTO;
import org.example.backend.entity.Customer;
import org.example.backend.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;


@RestController
@RequestMapping("api/v1/customer")
@CrossOrigin
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping(value = "save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ValidationDTO> saveCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        if (customerDTO.getUserId() == 0) {
            return new ResponseEntity<>(new ValidationDTO("User ID is missing.", 400, null), HttpStatus.BAD_REQUEST);
        }

        boolean res = customerService.addCustomer(customerDTO);
        ValidationDTO responseDTO = res
                ? new ValidationDTO("Customer saved successfully", 201, customerDTO)
                : new ValidationDTO("Failed to save customer", 500, null);

        return new ResponseEntity<>(responseDTO, res ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR);
    }



    @PutMapping(value = "update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateCustomer(@RequestBody CustomerDTO customerDTO) {
        boolean res = customerService.updateCustomer(customerDTO);
        return ResponseEntity.ok(res ? "Customer updated successfully" : "Failed to update customer");
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Integer id) {
        boolean res = customerService.deleteCustomer(id);
        return ResponseEntity.ok(res ? "Customer deleted successfully" : "Failed to delete customer");
    }

    @GetMapping("getAll")
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        List<CustomerDTO> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }
    @GetMapping("getByNumberPlate/{numberPlate}")
    public ResponseEntity<CustomerDTO> getCustomerByNumberPlate(@PathVariable String numberPlate) {
        CustomerDTO customerDTO = customerService.getCustomerByNumberPlate(numberPlate);
        return ResponseEntity.ok(customerDTO);
    }

    @GetMapping("analytics")
    public ResponseEntity<Map<String, Long>> getCustomerAnalytics() {
        Map<String, Long> customerAnalytics = customerService.getCustomerAnalytics();
        return ResponseEntity.ok(customerAnalytics);
    }


    @GetMapping("getTotalCount")
    public ResponseEntity<Long> getTotalCustomerCount() {
        long totalCount = customerService.getTotalCustomerCount();
        return ResponseEntity.ok(totalCount);
    }


    @GetMapping("getById/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Integer id) {
        try {
            CustomerDTO customerDTO = customerService.getCustomerById(id);
            return ResponseEntity.ok(customerDTO);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }



}
