package pl.jania.mockfuelcardapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import pl.jania.mockfuelcardapi.model.Transaction;
import pl.jania.mockfuelcardapi.repository.TransactionRepository;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transaction")
public class TransactionController {

    private final TransactionRepository transactionRepository;

    @GetMapping
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @PostMapping
    public Transaction createTransaction(
            @RequestBody Transaction transaction
    ) {
        return transactionRepository.save(transaction);
    }

    @GetMapping("/byDate")
    public List<Transaction> getTransactionsByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        return transactionRepository.findByTransactionDateBetween(startDate, endDate);
    }
}
