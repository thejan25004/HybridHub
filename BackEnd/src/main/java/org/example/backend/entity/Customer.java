package org.example.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int customerId;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId", nullable = false, unique = true)
    @NotNull(message = "User cannot be null")
    private User user;

    @NotBlank(message = "Address cannot be blank")
    @Size(max = 255, message = "Address cannot exceed 255 characters")
    private String address;

    @NotBlank(message = "Vehicle name cannot be blank")
    @Size(max = 100, message = "Vehicle name cannot exceed 100 characters")
    private String vehicleName;

    @NotBlank(message = "Number plate cannot be blank")
    @Pattern(
            regexp = "^[A-Z]{2,3}-\\d{4}$",
            message = "Number plate must follow the pattern (e.g., ABC-1234 or AB-1234)"
    )
    private String numberPlate;
}
