package pl.jania1857.fmsapi.dto;

import pl.jania1857.fmsapi.utils.Status;

import java.time.LocalDateTime;

public record StatusChangeDto(
        Integer id,
        Status newStatus,
        LocalDateTime timestamp,
        Integer vehicleId
) {
}
