package pl.jania1857.fms.vehicle;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class EngineInfo {
    private int displacement;
    private int power;
    private FuelType fuelType;
}
