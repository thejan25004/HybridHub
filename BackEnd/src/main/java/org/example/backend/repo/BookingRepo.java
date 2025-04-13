package org.example.backend.repo;

import org.example.backend.entity.Booking;
import org.example.backend.entity.Customer;
import org.example.backend.entity.Service;
import org.example.backend.entity.Technician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepo extends JpaRepository<Booking, Integer> {
    List<Booking> findByBookingDateAfter(Date date);

    List<Booking> findByServiceServiceId(Integer serviceId);

    Optional<Booking> findById(Integer bookingId);


}
