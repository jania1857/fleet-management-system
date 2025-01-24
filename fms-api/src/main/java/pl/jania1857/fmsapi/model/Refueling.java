package pl.jania1857.fmsapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import pl.jania1857.fmsapi.utils.Fuel;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Refueling {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private Fuel fuel;

    @NotNull
    private BigDecimal price;

    @NotNull
    private BigDecimal quantity;

    @NotNull
    private BigDecimal amount;

    @CreatedDate
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @OneToOne
    @JoinColumn(name = "cost_id")
    private Cost cost;

}
