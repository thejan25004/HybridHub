package org.example.backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ServiceDTO {
    private int serviceId;
    private String serviceName;
    private String description;
    private double price;
    private String photoUrl;
}
