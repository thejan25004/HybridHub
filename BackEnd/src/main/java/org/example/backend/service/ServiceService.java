//package org.example.backend.service;
//
//import org.example.backend.dto.CustomerDTO;
//import org.example.backend.dto.ServiceDTO;
//import org.example.backend.entity.Customer;
//import org.example.backend.entity.Service;
//import org.example.backend.repo.ServiceRepo;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//@org.springframework.stereotype.Service
//public class ServiceService {
//
//    @Autowired
//    private ServiceRepo serviceRepo;
//
//    @Autowired
//    private ModelMapper modelMapper ;
//
//
//    public boolean addService(ServiceDTO serviceDTO) {
//        Service service = modelMapper.map(serviceDTO, Service.class);
//        serviceRepo.save(service);
//        return true;
//    }
//
//    public boolean updateService(ServiceDTO serviceDTO) {
//        if (serviceRepo.existsById(serviceDTO.getServiceId())) {
//            Service service = serviceRepo.findById(serviceDTO.getServiceId()).orElse(null);
//            if (service == null) return false;
//            service.setServiceName(serviceDTO.getServiceName());
//            service.setDescription(serviceDTO.getDescription());
//            service.setPrice(serviceDTO.getPrice());
//            serviceRepo.save(service);
//            return true;
//        }
//        return false;
//    }
//
//    public boolean deleteService(Integer id) {
//        if (serviceRepo.existsById(id)) {
//            serviceRepo.deleteById(id);
//            return true;
//        }
//        return false;
//    }
//
//    public List<ServiceDTO> getAllServices() {
//        return serviceRepo.findAll().stream()
//                .map(service -> {
//                    ServiceDTO dto = modelMapper.map(service, ServiceDTO.class);
//                    dto.setPhotoUrl(service.getPhotoUrl()); // Ensure the photoUrl is set
//                    return dto;
//                })
//                .collect(Collectors.toList());
//    }
//
//
//
//    public ServiceDTO getServiceByServiceName(String serviceName) {
//        Service service = serviceRepo.findByserviceName(serviceName);
//        return modelMapper.map(service,ServiceDTO.class);
//    }
//
//    public ServiceDTO getServiceById(Integer id) {
//        Service service = serviceRepo.findById(id).orElse(null);
//        return service != null ? modelMapper.map(service, ServiceDTO.class) : null;
//    }
//
//
//
//}
package org.example.backend.service;

import org.example.backend.dto.BookingDTO;
import org.example.backend.dto.ServiceDTO;
import org.example.backend.dto.ServiceHistoryDTO;
import org.example.backend.entity.Booking;
import org.example.backend.entity.Service;
import org.example.backend.repo.BookingRepo;
import org.example.backend.repo.ServiceRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class ServiceService {

    @Autowired
    private ServiceRepo serviceRepo;

    @Autowired
    private BookingRepo bookingRepo;

    @Autowired
    private ModelMapper modelMapper;

    private static final String UPLOAD_DIR = "C:/uploads/";

    public boolean addService(ServiceDTO serviceDTO, MultipartFile photo) {
        Service service = modelMapper.map(serviceDTO, Service.class);

        // Save the photo if present
        if (photo != null && !photo.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "_" + photo.getOriginalFilename();
                File destFile = new File(UPLOAD_DIR + fileName);

                // Ensure the directory exists
                destFile.getParentFile().mkdirs();
                photo.transferTo(destFile);

                // Save the relative URL in the database
                service.setPhotoUrl("/uploads/" + fileName);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        serviceRepo.save(service);
        return true;
    }

    public boolean updateService(ServiceDTO serviceDTO, MultipartFile photo) {
        if (serviceRepo.existsById(serviceDTO.getServiceId())) {
            Service service = modelMapper.map(serviceDTO, Service.class);

            // Update the photo if present
            if (photo != null && !photo.isEmpty()) {
                try {
                    String fileName = System.currentTimeMillis() + "_" + photo.getOriginalFilename();
                    File destFile = new File(UPLOAD_DIR + fileName);

                    // Ensure the directory exists
                    destFile.getParentFile().mkdirs();
                    photo.transferTo(destFile);

                    // Set the new photo URL
                    service.setPhotoUrl("/uploads/" + fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            } else {
                // Preserve the existing photo URL if no new photo is uploaded
                Service existingService = serviceRepo.findById(serviceDTO.getServiceId()).orElse(null);
                if (existingService != null) {
                    service.setPhotoUrl(existingService.getPhotoUrl());
                }
            }

            serviceRepo.save(service);
            return true;
        }
        return false;
    }

    public boolean deleteService(Integer id) {
        if (serviceRepo.existsById(id)) {
            // Consider deleting the associated photo file here
            Service service = serviceRepo.findById(id).orElse(null);
            if (service != null && service.getPhotoUrl() != null && !service.getPhotoUrl().isEmpty()) {
                String fileName = service.getPhotoUrl().substring(service.getPhotoUrl().lastIndexOf("/") + 1);
                File file = new File(UPLOAD_DIR + fileName);
                if (file.exists()) {
                    file.delete();
                }
            }

            serviceRepo.deleteById(id);
            return true;
        }
        return false;
    }

    public List<ServiceDTO> getAllServices() {
        return serviceRepo.findAll().stream()
                .map(service -> modelMapper.map(service, ServiceDTO.class))
                .collect(Collectors.toList());
    }

    public ServiceDTO getServiceById(Integer id) {
        Service service = serviceRepo.findById(id).orElse(null);
        return service != null ? modelMapper.map(service, ServiceDTO.class) : null;
    }

    public ServiceDTO getServiceByServiceName(String serviceName) {
        Service service = serviceRepo.findByserviceName(serviceName);
        return modelMapper.map(service, ServiceDTO.class);
    }



    /**
     * Get service history with booking statistics
     * @param serviceId The ID of the service
     * @return ServiceHistoryDTO containing service details and booking statistics
     */
    public ServiceHistoryDTO getServiceHistory(Integer serviceId) {
        Service service = serviceRepo.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        // Get all bookings for this service
        List<Booking> bookings = bookingRepo.findAll().stream()
                .filter(booking -> booking.getService().getServiceId() == serviceId)
                .collect(Collectors.toList());

        // Convert to DTOs
        List<BookingDTO> bookingDTOs = bookings.stream()
                .map(booking -> {
                    BookingDTO dto = modelMapper.map(booking, BookingDTO.class);
                    dto.setCustomerId(booking.getCustomer().getCustomerId());
                    if (booking.getTechnician() != null) {
                        dto.setTechnicianId(booking.getTechnician().getTechnicianId());
                    }
                    dto.setServiceId(booking.getService().getServiceId());
                    return dto;
                })
                .collect(Collectors.toList());

        // Count bookings by customer
        Map<Integer, Long> bookingsByCustomer = bookings.stream()
                .collect(Collectors.groupingBy(
                        booking -> booking.getCustomer().getCustomerId(),
                        Collectors.counting()
                ));

        // Create and return the ServiceHistoryDTO
        ServiceHistoryDTO historyDTO = new ServiceHistoryDTO();
        historyDTO.setService(modelMapper.map(service, ServiceDTO.class));
        historyDTO.setTotalBookings(bookings.size());
        historyDTO.setBookings(bookingDTOs);
        historyDTO.setBookingsByCustomer(bookingsByCustomer);

        return historyDTO;
    }

    /**
     * Get booking analytics for all services
     * @return Map of service IDs to booking counts
     */
    public Map<String, Long> getServiceBookingAnalytics() {
        List<Booking> allBookings = bookingRepo.findAll();

        return allBookings.stream()
                .collect(Collectors.groupingBy(
                        booking -> booking.getService().getServiceName(),
                        Collectors.counting()
                ));
    }


}