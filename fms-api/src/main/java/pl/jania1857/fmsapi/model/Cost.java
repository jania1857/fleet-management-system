package pl.jania1857.fmsapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Cost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private BigDecimal amount;

    @OneToOne(mappedBy = "cost")
    private Refueling refueling;

    @OneToOne(mappedBy = "cost")
    private Inspection inspection;

    @OneToOne(mappedBy = "cost")
    private Service service;

    @OneToOne(mappedBy = "cost")
    private Insurance insurance;
}
