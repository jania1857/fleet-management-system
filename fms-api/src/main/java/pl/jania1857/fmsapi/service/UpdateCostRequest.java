package pl.jania1857.fmsapi.service;

import java.math.BigDecimal;

public record UpdateCostRequest(
        BigDecimal amount
) {
}
