package pl.jania1857.fms.assignment;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import pl.jania1857.fms.user.User;
import pl.jania1857.fms.vehicle.Vehicle;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Assignment {
    @Id
    @GeneratedValue
    private long id;

    private boolean active;
    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;
    @ManyToOne
    @JoinColumn(name = "driver_id")
    private User driver;
    private Date startDate;
    private Date endDate;

    @CreatedDate
    private Date createdAt;
    @LastModifiedDate
    private Date updatedAt;
}
