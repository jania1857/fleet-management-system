package pl.jania1857.fmsapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.jania1857.fmsapi.model.Inspection;

@Repository
public interface InspectionRepository extends JpaRepository<Inspection, Integer> {
}
