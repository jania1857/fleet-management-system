package pl.jania1857.fms.service;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import pl.jania1857.fms.cost.Cost;
import pl.jania1857.fms.vehicle.Vehicle;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Service {
    @Id
    @GeneratedValue
    private long id;

    private String title;
    private String description;
    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;
    @OneToOne
    @JoinColumn(name = "cost_id")
    private Cost cost;
    private int mileageAtTheTime;
    private int nextServiceAt;

    @CreatedDate
    private Date createdAt;
    @LastModifiedDate
    private Date updatedAt;
}
