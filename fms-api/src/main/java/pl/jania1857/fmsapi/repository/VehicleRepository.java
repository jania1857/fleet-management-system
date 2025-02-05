package pl.jania1857.fmsapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.jania1857.fmsapi.model.Vehicle;

import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {
    Optional<Vehicle> findByFuelCardNumber(String fuelCardNumber);
}
