package pl.jania1857.fms.inspection;

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
public class Inspection {
    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;
    private Date nextInspectionDate;
    @OneToOne
    @JoinColumn(name = "cost_id")
    private Cost cost;

    @CreatedDate
    private Date createdAt;
    @LastModifiedDate
    private Date updatedAt;
}
