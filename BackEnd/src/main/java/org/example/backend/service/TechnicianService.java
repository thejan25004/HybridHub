package org.example.backend.service;

import org.example.backend.dto.CustomerDTO;
import org.example.backend.dto.TechnicianDTO;
import org.example.backend.entity.Customer;
import org.example.backend.entity.Technician;
import org.example.backend.repo.TechnicianRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TechnicianService {

    @Autowired
    private TechnicianRepo technicianRepo;

    @Autowired
    private ModelMapper modelMapper;
    private static final String UPLOAD_DIR = "C:/uploads/";


//    public boolean addTechnician(TechnicianDTO technicianDTO, MultipartFile photo) {
//        Technician technician = modelMapper.map(technicianDTO, Technician.class);
//        technicianRepo.save(technician);
//        return true;
//    }

    public boolean addTechnician(TechnicianDTO technicianDTO, MultipartFile photo) {
        Technician technician = modelMapper.map(technicianDTO, Technician.class);

        // Save the photo if present
        if (photo != null && !photo.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "_" + photo.getOriginalFilename();
                File destFile = new File(UPLOAD_DIR + fileName);

                // Ensure the directory exists
                destFile.getParentFile().mkdirs();
                photo.transferTo(destFile);

                // Save the relative URL in the database
                technician.setPhotoUrl("/uploads/" + fileName);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        technicianRepo.save(technician);
        return true;
    }

    public boolean updateTechnician(TechnicianDTO technicianDTO, MultipartFile photo) {
        if (technicianRepo.existsById(technicianDTO.getTechnicianId())) {
            Technician technician = modelMapper.map(technicianDTO, Technician.class);

            // Update the photo if present
            if (photo != null && !photo.isEmpty()) {
                try {
                    String fileName = System.currentTimeMillis() + "_" + photo.getOriginalFilename();
                    File destFile = new File(UPLOAD_DIR + fileName);

                    // Ensure the directory exists
                    destFile.getParentFile().mkdirs();
                    photo.transferTo(destFile);

                    // Set the new photo URL
                    technician.setPhotoUrl("/uploads/" + fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            } else {
                // Preserve the existing photo URL if no new photo is uploaded
                Technician existingTechnician = technicianRepo.findById(technicianDTO.getTechnicianId()).orElse(null);
                if (existingTechnician != null) {
                    technician.setPhotoUrl(existingTechnician.getPhotoUrl());
                }
            }

            technicianRepo.save(technician);
            return true;
        }
        return false;
    }

    public boolean deleteTechnician(Integer id) {
        if (technicianRepo.existsById(id)) {
            technicianRepo.deleteById(id);
            return true;
        }
        return false;
    }

    public List<TechnicianDTO> getAllTechnicians() {
        return technicianRepo.findAll().stream()
                .map(technician -> modelMapper.map(technician, TechnicianDTO.class))
                .collect(Collectors.toList());
    }

    public TechnicianDTO getTechnicianById(Integer id) {
        return technicianRepo.findById(id)
                .map(technician -> modelMapper.map(technician, TechnicianDTO.class))
                .orElse(null);
    }

    public TechnicianDTO getTechnicianByName(String name) {
        Technician technician = technicianRepo.findByname(name);
        return modelMapper.map(technician,TechnicianDTO.class);
    }
}

//package org.example.carservice.service;
//
//import org.example.carservice.dto.TechnicianDTO;
//import org.example.carservice.entity.Technician;
//import org.example.carservice.repo.TechnicianRepo;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class TechnicianService {
//
//    @Autowired
//    private TechnicianRepo technicianRepo;
//
//    @Autowired
//    private ModelMapper modelMapper;
//
//    public boolean addTechnician(TechnicianDTO technicianDTO) {
//        Technician technician = modelMapper.map(technicianDTO, Technician.class);
//        technicianRepo.save(technician);
//        return true;
//    }
//
//    public boolean updateTechnician(TechnicianDTO technicianDTO) {
//        if (technicianRepo.existsById(technicianDTO.getTechnicianId())) {
//            Technician technician = modelMapper.map(technicianDTO, Technician.class);
//            technicianRepo.save(technician);
//            return true;
//        }
//        return false;
//    }
//
//    public boolean deleteTechnician(Integer id) {
//        if (technicianRepo.existsById(id)) {
//            technicianRepo.deleteById(id);
//            return true;
//        }
//        return false;
//    }
//
//    public List<TechnicianDTO> getAllTechnicians() {
//        return technicianRepo.findAll().stream()
//                .map(technician -> modelMapper.map(technician, TechnicianDTO.class))
//                .collect(Collectors.toList());
//    }
//
//    public TechnicianDTO getTechnicianById(Integer id) {
//        return technicianRepo.findById(id)
//                .map(technician -> modelMapper.map(technician, TechnicianDTO.class))
//                .orElse(null);
//    }
//}












//new

//
//package org.example.backend.service;
//
//import org.example.backend.dto.TechnicianDTO;
//import org.example.backend.entity.Technician;
//import org.example.backend.repo.TechnicianRepo;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class TechnicianService {
//
//    @Autowired
//    private TechnicianRepo technicianRepo;
//
//    @Autowired
//    private ModelMapper modelMapper;
//
//    private static final String UPLOAD_DIR = "C:/uploads/";
//
//    public boolean addTechnician(TechnicianDTO technicianDTO, MultipartFile photo) {
//        Technician technician = modelMapper.map(technicianDTO, Technician.class);
//
//        // Save the photo if present
//        if (photo != null && !photo.isEmpty()) {
//            try {
//                String fileName = System.currentTimeMillis() + "_" + photo.getOriginalFilename();
//                File destFile = new File(UPLOAD_DIR + fileName);
//                photo.transferTo(destFile);
//                technician.setPhotoUrl("/uploads/" + fileName);
//            } catch (IOException e) {
//                e.printStackTrace();
//                return false;
//            }
//        }
//
//        technicianRepo.save(technician);
//        return true;
//    }
//
//    public boolean updateTechnician(TechnicianDTO technicianDTO, MultipartFile photo) {
//        if (technicianRepo.existsById(technicianDTO.getTechnicianId())) {
//            Technician technician = modelMapper.map(technicianDTO, Technician.class);
//
//            // Update the photo if present
//            if (photo != null && !photo.isEmpty()) {
//                try {
//                    String fileName = System.currentTimeMillis() + "_" + photo.getOriginalFilename();
//                    File destFile = new File(UPLOAD_DIR + fileName);
//                    photo.transferTo(destFile);
//                    technician.setPhotoUrl("/uploads/" + fileName);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    return false;
//                }
//            }
//
//            technicianRepo.save(technician);
//            return true;
//        }
//        return false;
//    }
//
//    public boolean deleteTechnician(Integer id) {
//        if (technicianRepo.existsById(id)) {
//            technicianRepo.deleteById(id);
//            return true;
//        }
//        return false;
//    }
//
//    public List<TechnicianDTO> getAllTechnicians() {
//        return technicianRepo.findAll().stream()
//                .map(technician -> modelMapper.map(technician, TechnicianDTO.class))
//                .collect(Collectors.toList());
//    }
//
//    public TechnicianDTO getTechnicianById(Integer id) {
//        return technicianRepo.findById(id)
//                .map(technician -> modelMapper.map(technician, TechnicianDTO.class))
//                .orElse(null);
//    }
//
//    public TechnicianDTO getTechnicianByName(String name) {
//        Technician technician = technicianRepo.findByname(name);
//        return modelMapper.map(technician, TechnicianDTO.class);
//    }
//}
