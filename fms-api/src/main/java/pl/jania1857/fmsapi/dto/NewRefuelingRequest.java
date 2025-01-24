package pl.jania1857.fmsapi.dto;

import pl.jania1857.fmsapi.utils.Fuel;

import java.math.BigDecimal;

public record NewRefuelingRequest(
        Fuel fuel,
        BigDecimal price,
        BigDecimal quantity,
        BigDecimal amount
) {
}
