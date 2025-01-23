package pl.jania1857.fmsapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.jania1857.fmsapi.model.Insurance;

@Repository
public interface InsuranceRepository extends JpaRepository<Insurance, Integer> {
}
