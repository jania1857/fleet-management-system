package pl.jania1857.fmsapi.dto.shell;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Transaction(
        String transactionId,
        String cardNumber,
        String vehicleId,
        LocalDateTime transactionDate,
        String fuelType,
        BigDecimal quantity,
        BigDecimal unitPrice,
        BigDecimal totalAmount,
        String currency
) {
}
