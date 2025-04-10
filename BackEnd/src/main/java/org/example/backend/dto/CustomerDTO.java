package org.example.backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomerDTO {
    private int customerId;
    private int userId;
    private String address;
    private String vehicleName;
    private String numberPlate;
}
