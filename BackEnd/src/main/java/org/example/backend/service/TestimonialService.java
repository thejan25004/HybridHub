//package org.example.backend.service;
//
//import org.example.backend.dto.TestimonialDTO;
//import org.example.backend.entity.Testimonial;
//import org.example.backend.repo.TestimonialRepository;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class TestimonialService {
//
//    @Autowired
//    private TestimonialRepository testimonialRepository;
//
//    @Autowired
//    private ModelMapper modelMapper;
//
//    public boolean addTestimonial(TestimonialDTO testimonialDTO) {
//        try {
//            Testimonial testimonial = new Testimonial();
//            testimonial.setName(testimonialDTO.getName());
//            testimonial.setProfession(testimonialDTO.getProfession());
//            testimonial.setMessage(testimonialDTO.getMessage());
//            testimonial.setPhoto(testimonialDTO.getPhoto() != null ?
//                    java.util.Base64.getDecoder().decode(testimonialDTO.getPhoto()) : null);
//            testimonialRepository.save(testimonial);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    public List<TestimonialDTO> getAllTestimonials() {
//        return testimonialRepository.findAll().stream()
//                .map(t -> new TestimonialDTO(t.getName(), t.getProfession(), t.getMessage(), t.getPhoto()))
//                .collect(Collectors.toList());
//    }
//}
package org.example.backend.service;

import org.example.backend.dto.TestimonialDTO;
import org.example.backend.entity.Testimonial;
import org.example.backend.repo.TestimonialRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TestimonialService {

    @Autowired
    private TestimonialRepository testimonialRepository;

    @Autowired
    private ModelMapper modelMapper;

    public boolean addTestimonial(TestimonialDTO testimonialDTO) {
        try {
            Testimonial testimonial = modelMapper.map(testimonialDTO, Testimonial.class);
            testimonialRepository.save(testimonial);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<TestimonialDTO> getAllTestimonials() {
        return testimonialRepository.findAll().stream()
                .map(t -> modelMapper.map(t, TestimonialDTO.class))
                .collect(Collectors.toList());
    }
}
