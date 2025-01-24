package pl.jania1857.fmsapi.dto;

import pl.jania1857.fmsapi.utils.Status;

public record ChangeStatusResponse(
        Integer vehicleId,
        Status newStatus
) {
}
