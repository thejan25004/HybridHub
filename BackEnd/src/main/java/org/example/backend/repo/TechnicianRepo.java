package org.example.backend.repo;

import org.example.backend.entity.Service;
import org.example.backend.entity.Technician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TechnicianRepo extends JpaRepository<Technician, Integer> {
    Technician findByname(String name);

}
