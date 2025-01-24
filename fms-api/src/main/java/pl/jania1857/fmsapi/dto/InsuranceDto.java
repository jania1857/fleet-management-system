package pl.jania1857.fmsapi.dto;

import pl.jania1857.fmsapi.utils.InsuranceType;

import java.time.LocalDateTime;

public record InsuranceDto(
        Integer id,
        InsuranceType type,
        String number,
        String description,
        String insurer,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Integer vehicleId,
        CostDto cost
) {
}
