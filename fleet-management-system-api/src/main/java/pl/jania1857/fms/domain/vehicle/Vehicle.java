package pl.jania1857.fms.domain.vehicle;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pl.jania1857.fms.domain.assignment.Assignment;
import pl.jania1857.fms.domain.inspection.Inspection;
import pl.jania1857.fms.domain.insurance.Insurance;
import pl.jania1857.fms.domain.refueling.Refueling;
import pl.jania1857.fms.domain.service.Service;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
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
    @OneToMany(mappedBy = "vehicle")
    private List<Service> services;         // serwisy
    @OneToMany(mappedBy = "vehicle")
    private List<Insurance> insurances;     // ubezpieczenia
    @OneToMany(mappedBy = "vehicle")
    private List<Refueling> refuelings;     // tankowania
    @OneToMany(mappedBy = "vehicle")
    private List<Inspection> inspections;   // przeglądy techniczne
    @OneToMany(mappedBy = "vehicle")
    private List<Assignment> assignments;   // przypisania do kierowców

    @CreatedDate
    private Date createdAt;
    @LastModifiedDate
    private Date updatedAt;
}
