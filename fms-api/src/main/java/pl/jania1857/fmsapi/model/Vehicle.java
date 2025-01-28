package pl.jania1857.fmsapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import pl.jania1857.fmsapi.utils.FuelType;

import java.util.ArrayList;
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "iot_device_id")
    private User iotDevice;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StatusChange> statusChanges = new ArrayList<>();

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Refueling> refuelings = new ArrayList<>();

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Inspection> inspections = new ArrayList<>();

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Service> services = new ArrayList<>();

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Insurance> insurances = new ArrayList<>();

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MileageChange> mileageChanges = new ArrayList<>();

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Assignment> assignments = new ArrayList<>();

    public void addStatusChange(StatusChange statusChange) {
        if (statusChanges == null) {
            statusChanges = new ArrayList<>();
        }
        statusChanges.add(statusChange);
    }

    public void addRefueling(Refueling refueling) {
        if (refuelings == null) {
            refuelings = new ArrayList<>();
        }
        refuelings.add(refueling);
    }
    public void addInspection(Inspection inspection) {
        if (inspections == null) {
            inspections = new ArrayList<>();
        }
        inspections.add(inspection);
    }
    public void addService(Service service) {
        if (services == null) {
            services = new ArrayList<>();
        }
        services.add(service);
    }
    public void addInsurance(Insurance insurance) {
        if (insurances == null) {
            insurances = new ArrayList<>();
        }
        insurances.add(insurance);
    }
    public void addMileageChange(MileageChange mileageChange) {
        if (mileageChanges == null) {
            mileageChanges = new ArrayList<>();
        }
        mileageChanges.add(mileageChange);
    }
    public void addAssignment(Assignment assignment) {
        if (assignments == null) {
            assignments = new ArrayList<>();
        }
        assignments.add(assignment);
    }

}
