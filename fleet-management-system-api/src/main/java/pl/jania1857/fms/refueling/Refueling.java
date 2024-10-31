package pl.jania1857.fms.refueling;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import pl.jania1857.fms.cost.Cost;
import pl.jania1857.fms.vehicle.FuelType;
import pl.jania1857.fms.vehicle.Vehicle;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Refueling {
    @Id
    @GeneratedValue
    private long id;

    private FuelType fuelType;
    private BigDecimal price;
    private float quantity;
    @OneToOne
    @JoinColumn(name = "cost_id")
    private Cost cost;
    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @CreatedDate
    private Date createdAt;
    @LastModifiedDate
    private Date updatedAt;
}
