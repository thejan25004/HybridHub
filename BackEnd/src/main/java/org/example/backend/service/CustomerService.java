package org.example.backend.service;

import org.example.backend.dto.CustomerDTO;
import org.example.backend.dto.UserDTO;
import org.example.backend.entity.Customer;
import org.example.backend.entity.User;
import org.example.backend.repo.CustomerRepo;
import org.example.backend.repo.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;


    public boolean addCustomer(CustomerDTO customerDTO) {
        Optional<User> user = userRepo.findById(customerDTO.getUserId());
        if (user.isPresent()) {
            Customer customer = new Customer();
            customer.setUser(user.get());
            customer.setAddress(customerDTO.getAddress());
            customer.setVehicleName(customerDTO.getVehicleName());
            customer.setNumberPlate(customerDTO.getNumberPlate());

            customerRepo.save(customer);
            return true;
        }
        return false; // Return false if user is not found
    }

    public boolean updateCustomer(CustomerDTO customerDTO) {
        if (customerRepo.existsById(customerDTO.getCustomerId())) {
            Customer customer = customerRepo.findById(customerDTO.getCustomerId()).orElse(null);
            if (customer == null) return false;

            // Get the user from the userId in the DTO
            User user = userRepo.findById(customerDTO.getUserId()).orElse(null);
            if (user == null) return false;

            // Update the customer fields
            customer.setUser(user);
            customer.setAddress(customerDTO.getAddress());
            customer.setVehicleName(customerDTO.getVehicleName());
            customer.setNumberPlate(customerDTO.getNumberPlate());

            customerRepo.save(customer);
            return true;
        }
        return false;
    }

    public boolean deleteCustomer(Integer id) {
        if (customerRepo.existsById(id)) {
            customerRepo.deleteById(id);
            return true;
        }
        return false;
    }

    public List<CustomerDTO> getAllCustomers() {
        return customerRepo.findAll().stream()
                .map(customer -> modelMapper.map(customer, CustomerDTO.class))
                .collect(Collectors.toList());
    }

    public CustomerDTO getCustomerByNumberPlate(String numberPlate) {
        try {
            Customer customer = customerRepo.findByNumberPlate(numberPlate)
                    .orElseThrow(() -> new NoSuchElementException("Customer not found"));

            return modelMapper.map(customer, CustomerDTO.class);

        } catch (IncorrectResultSizeDataAccessException e) {
            throw new RuntimeException("Multiple customers found with the same number plate. Data inconsistency!", e);
        }
    }

    public Map<String, Long> getCustomerAnalytics() {
        return customerRepo.findAll().stream()
                .collect(Collectors.groupingBy(
                        Customer::getVehicleName,
                        Collectors.counting()
                ));
    }

    public long getTotalCustomerCount() {
        return customerRepo.count();
    }

    public CustomerDTO getCustomerById(Integer id) {
        Optional<Customer> customerOpt = customerRepo.findById(id);
        if (!customerOpt.isPresent()) {
            throw new NoSuchElementException("Customer not found");
        }

        Customer customer = customerOpt.get();
        return new CustomerDTO(
                customer.getCustomerId(),
                customer.getUser().getUserId(),
                customer.getAddress(),
                customer.getVehicleName(),
                customer.getNumberPlate()
        );
    }
}