package pl.jania1857.fmsapi.dto;

import java.time.LocalDateTime;

public record UserAssignmentResponse(
        VehicleDto vehicle,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}
