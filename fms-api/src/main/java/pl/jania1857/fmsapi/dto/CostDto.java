package pl.jania1857.fmsapi.dto;

import java.math.BigDecimal;

public record CostDto(
        Integer id,
        BigDecimal amount,
        Integer refuelingId,
        Integer inspectionId,
        Integer serviceId,
        Integer insuranceId
) {
}
