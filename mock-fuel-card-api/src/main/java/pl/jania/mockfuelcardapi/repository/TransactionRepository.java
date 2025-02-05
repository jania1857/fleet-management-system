package pl.jania.mockfuelcardapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.jania.mockfuelcardapi.model.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByTransactionDateBetween(LocalDateTime from, LocalDateTime to);
}
