package pl.jania1857.fmsapi.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.jania1857.fmsapi.utils.Role;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, max = 50)
    private String firstname;

    @NotNull
    @Size(min = 3, max = 50)
    private String lastname;

    @NotNull
    private Role role;

    @OneToMany(mappedBy = "assignedTo", cascade = CascadeType.ALL)
    private List<Vehicle> vehicles;
}
