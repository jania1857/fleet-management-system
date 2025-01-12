package pl.jania1857.fmsapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.jania1857.fmsapi.dto.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
