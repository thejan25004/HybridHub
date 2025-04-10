//package org.example.backend.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//@Entity
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@ToString
//public class Service {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int serviceId;
//
//    private String serviceName;
//
//    @Lob
//    @Column(columnDefinition = "TEXT")
//    private String description;
//
//    private double price;
//
//    private String photoUrl;  // New field for service image URL
//}
package org.example.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int serviceId;

    @NotBlank(message = "Service name cannot be empty")
    @Size(max = 100, message = "Service name must not exceed 100 characters")
    private String serviceName;

    @Lob
    @Column(columnDefinition = "TEXT")
    @NotBlank(message = "Description cannot be empty")
    private String description;

    @Positive(message = "Price must be a positive value")
    private double price;

    @NotBlank(message = "Photo URL cannot be empty")
    @Pattern(
            regexp = "^/uploads/.+\\.(jpg|jpeg|png|gif|bmp|webp)$",
            message = "Photo URL must be in the /uploads/ directory and have a valid image extension"
    )
    private String photoUrl;
}
