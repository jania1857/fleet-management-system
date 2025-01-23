package pl.jania1857.fmsapi.dto;

import java.time.LocalDateTime;

public record AssignmentDto(
        Integer id,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Integer vehicleId,
        Integer userId
) {
}
