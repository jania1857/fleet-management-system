package pl.jania1857.fmsapi.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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

    @NotNull
    @Size(min = 1, max = 50)
    private String manufacturer;

    @NotNull
    @Size(min = 1, max = 50)
    private String model;

    @NotNull
    @Min(1900)
    @Max(2100)
    private int year;

    @NotNull
    @Pattern(regexp = "^[A-Z0-9-]+$")
    private String registrationNumber;

    @NotNull
    @Pattern(regexp = "^[A-HJ-NPR-Z0-9]{17}$")
    private String vin;

    @NotNull
    @Size(min = 1, max = 50)
    private String status;

    @NotNull
    @Size(min = 1, max = 50)
    private String fuelType;

    @Positive
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
