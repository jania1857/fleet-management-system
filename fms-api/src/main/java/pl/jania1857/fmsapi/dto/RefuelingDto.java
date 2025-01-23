package pl.jania1857.fmsapi.dto;

import pl.jania1857.fmsapi.utils.Fuel;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RefuelingDto(
    Integer id,
    Fuel fuel,
    BigDecimal price,
    BigDecimal quantity,
    BigDecimal amount,
    LocalDateTime timestamp,
    Integer vehicleId,
    Integer costId
) {
}
