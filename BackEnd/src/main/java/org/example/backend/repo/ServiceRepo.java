package org.example.backend.repo;

import org.example.backend.entity.Customer;
import org.example.backend.entity.Service;
import org.example.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepo extends JpaRepository<Service, Integer> {
    Service findByserviceName(String serviceName);
}
