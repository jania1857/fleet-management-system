package pl.jania1857.fms.domain.insurance;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pl.jania1857.fms.domain.cost.Cost;
import pl.jania1857.fms.domain.vehicle.Vehicle;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Insurance {
    @Id
    @GeneratedValue
    private long id;

    private InsuranceType insuranceType;
    private String number;
    private String description;
    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;
    private String insurer;
    @OneToOne
    @JoinColumn(name = "cost_id")
    private Cost cost;
    private Date startDate;
    private Date endDate;

    @CreatedDate
    private Date createdAt;
    @LastModifiedDate
    private Date updatedAt;
}