package pl.jania1857.fmsapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import pl.jania1857.fmsapi.utils.FuelType;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private String manufacturer;

    @NotNull
    private String model;

    @NotNull
    private Integer year;

    @NotNull
    private String registrationNumber;

    @NotNull
    private String vin;

    @NotNull
    @Enumerated(EnumType.STRING)
    private FuelType fuelType;
    @NotNull
    private Integer displacement;

    @OneToMany(mappedBy = "id")
    private List<StatusChange> statusChanges;

    @OneToMany(mappedBy = "id")
    private List<Refueling> refuelings;

    @OneToMany(mappedBy = "id")
    private List<Inspection> inspections;

    @OneToMany(mappedBy = "id")
    private List<Service> services;

    @OneToMany(mappedBy = "id")
    private List<Insurance> insurances;

    @OneToMany(mappedBy = "id")
    private List<MileageChange> mileageChanges;

    @OneToMany(mappedBy = "id")
    private List<Assignment> assignments;

}
