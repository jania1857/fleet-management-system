package pl.jania1857.fmsapi.repository;

import org.hibernate.sql.ast.tree.update.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {
}
