package pl.jania1857.fms.vehicle;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import pl.jania1857.fms.assignment.Assignment;
import pl.jania1857.fms.inspection.Inspection;
import pl.jania1857.fms.insurance.Insurance;
import pl.jania1857.fms.refueling.Refueling;
import pl.jania1857.fms.service.Service;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {
    @Id
    @GeneratedValue
    private long id;

    private String brand;                   // marka
    private String model;                   // model
    private int year;                       // rok produkcji
    private String color;                   // kolor
    @Embedded
    private EngineInfo engine;              // silnik
    private int mileage;                    // przebieg
    private String registrationNumber;      // numer rejestracyjny
    private String VIN;                     // VIN/FIN
    @OneToMany(mappedBy = "vehicle_id")
    private List<Service> services;         // serwisy
    @OneToMany(mappedBy = "vehicle_id")
    private List<Insurance> insurances;     // ubezpieczenia
    @OneToMany(mappedBy = "vehicle_id")
    private List<Refueling> refuelings;     // tankowania
    @OneToMany(mappedBy = "vehicle_id")
    private List<Inspection> inspections;   // przeglądy techniczne
    @OneToMany(mappedBy = "vehicle_id")
    private List<Assignment> assignments;   // przypisania do kierowców

    @CreatedDate
    private Date createdAt;
    @LastModifiedDate
    private Date updatedAt;
}
