package pl.jania1857.fmsapi.dto;

import java.math.BigDecimal;

public record UpdateCostRequest(
        BigDecimal amount
) {
}
