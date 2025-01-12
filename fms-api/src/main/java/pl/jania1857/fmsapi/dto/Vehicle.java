package pl.jania1857.fmsapi.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String manufacturer;
    private String model;
    private int year;
    private String registrationNumber;
    private String vin;
    private String status;
    private String fuelType;
    private double displacement;

    @ManyToOne
    @JoinColumn(name = "assigned_to")
    private User assignedTo;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    private List<Service> services;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    private List<Insurance> insurances;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    private List<Refueling> refuelings;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    private List<Inspection> inspections;
}
