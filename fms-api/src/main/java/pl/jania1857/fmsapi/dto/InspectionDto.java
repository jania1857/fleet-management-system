package pl.jania1857.fmsapi.dto;

import java.time.LocalDateTime;

public record InspectionDto(
        Integer id,
        LocalDateTime inspectionDate,
        LocalDateTime nextInspectionDate,
        String description,
        Boolean passed,
        Integer vehicleId,
        CostDto cost
) {
}
