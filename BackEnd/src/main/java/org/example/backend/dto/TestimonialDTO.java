////package org.example.backend.dto;
////
////import lombok.AllArgsConstructor;
////import lombok.Data;
////import lombok.NoArgsConstructor;
////
////@Data
////@NoArgsConstructor
////@AllArgsConstructor
////public class TestimonialDTO {
////    private String name;
////    private String profession;
////    private String message;
////    private byte[] photo;
////}
//package org.example.backend.dto;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import java.util.Base64;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class TestimonialDTO {
//    private String name;
//    private String profession;
//    private String message;
//    private String photo; // Base64-encoded string
//
//    public TestimonialDTO(String name, String profession, String message, byte[] photoBytes) {
//        this.name = name;
//        this.profession = profession;
//        this.message = message;
//        this.photo = (photoBytes != null && photoBytes.length > 0) ? Base64.getEncoder().encodeToString(photoBytes) : null;
//    }
//}
//
package org.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestimonialDTO {
    private String name;
    private String profession;
    private String message;
    private String photoPath;
}
