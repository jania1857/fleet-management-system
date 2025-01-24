package pl.jania1857.fmsapi.dto;

import java.time.LocalDateTime;

public record NewInspectionRequest(
        LocalDateTime inspectionDate,
        String description,
        Boolean passed
) {
}
