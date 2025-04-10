package org.example.backend.service;

import org.example.backend.dto.*;
import org.example.backend.entity.*;
import org.example.backend.repo.BookingRepo;
import org.example.backend.repo.CustomerRepo;
import org.example.backend.repo.TechnicianRepo;
import org.example.backend.repo.ServiceRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingRepo bookingRepo;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private TechnicianRepo technicianRepo;

    @Autowired
    private ServiceRepo serviceRepo;

    @Autowired
    private ModelMapper modelMapper;


    public boolean saveBooking(BookingDTO bookingDTO) {
        try {
            System.out.println("Received BookingDTO: " + bookingDTO);

            Booking booking = modelMapper.map(bookingDTO, Booking.class);


            Customer customer = customerRepo.findById(bookingDTO.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            Technician technician = technicianRepo.findById(bookingDTO.getTechnicianId())
                    .orElse(null);
            org.example.backend.entity.Service service = serviceRepo.findById(bookingDTO.getServiceId())
                    .orElseThrow(() -> new RuntimeException("Service not found"));


            booking.setCustomer(customer);
            booking.setTechnician(technician);
            booking.setService(service);

            bookingRepo.save(booking);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateBooking(BookingDTO bookingDTO) {
        if (bookingRepo.existsById(bookingDTO.getBookingId())) {
            Booking booking = modelMapper.map(bookingDTO, Booking.class);


            Customer customer = customerRepo.findById(bookingDTO.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            Technician technician = technicianRepo.findById(bookingDTO.getTechnicianId())
                    .orElse(null);
            org.example.backend.entity.Service service = serviceRepo.findById(bookingDTO.getServiceId())
                    .orElseThrow(() -> new RuntimeException("Service not found"));


            booking.setCustomer(customer);
            booking.setTechnician(technician);
            booking.setService(service);

            bookingRepo.save(booking);
            return true;
        }
        return false;
    }


    public boolean deleteBooking(Integer id) {
        if (bookingRepo.existsById(id)) {
            bookingRepo.deleteById(id);
            return true;
        }
        return false;
    }

    public List<BookingDTO> getAllBookings() {
        return bookingRepo.findAll().stream()
                .map(booking -> modelMapper.map(booking, BookingDTO.class))
                .collect(Collectors.toList());
    }

    public Map<String, Long> getBookingCountByMonth() {
        return bookingRepo.findAll().stream()
                .collect(Collectors.groupingBy(
                        booking -> new SimpleDateFormat("MMMM").format(booking.getBookingDate()),
                        Collectors.counting()
                ));
    }
    public long getTotalBookingsCount() {
        return bookingRepo.count();
    }

    public List<BookingDTO> getNewBookings() {
        // Calculate the date and time 30 minutes ago
        Date thirtyMinutesAgo = new Date(System.currentTimeMillis() - (30 * 60 * 1000)); // 30 minutes ago

        // Fetch bookings created after this time
        List<Booking> newBookings = bookingRepo.findByBookingDateAfter(thirtyMinutesAgo);

        // Convert the bookings to DTOs
        return newBookings.stream()
                .map(booking -> modelMapper.map(booking, BookingDTO.class))
                .collect(Collectors.toList());
    }

    public BookingDetailsDTO getBookingDetailsById(Integer bookingId) {
        // Find the booking
        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Get related entities
        Customer customer = booking.getCustomer();
        Technician technician = booking.getTechnician();
        org.example.backend.entity.Service service = booking.getService();

        // Create a DTO with all required details
        BookingDetailsDTO detailsDTO = new BookingDetailsDTO();
        detailsDTO.setBooking(modelMapper.map(booking, BookingDTO.class));
        detailsDTO.setCustomer(modelMapper.map(customer, CustomerDTO.class));
        if (technician != null) {
            detailsDTO.setTechnician(modelMapper.map(technician, TechnicianDTO.class));
        }
        detailsDTO.setService(modelMapper.map(service, ServiceDTO.class));

        return detailsDTO;
    }

}
