package pl.jania1857.fmsapi.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Insurance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String type;

    @NotNull
    @Size(min = 1, max = 50)
    private String number;

    @NotNull
    @Size(min = 1, max = 50)
    private String insurer;

    @NotNull
    private LocalDate startingDate;

    @NotNull
    private LocalDate endingDate;
    
    @Size(max = 500)
    private String description;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @OneToOne
    @JoinColumn(name = "cost_id")
    private Cost cost;
}
