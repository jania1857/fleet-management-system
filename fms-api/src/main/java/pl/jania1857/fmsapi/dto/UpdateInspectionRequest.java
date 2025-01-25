package pl.jania1857.fmsapi.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UpdateInspectionRequest(
        LocalDateTime inspectionDate,
        LocalDateTime nextInspectionDate,
        String description,
        Boolean passed,
        BigDecimal cost
) {
}
