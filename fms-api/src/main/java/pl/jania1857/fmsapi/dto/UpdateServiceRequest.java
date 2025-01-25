package pl.jania1857.fmsapi.dto;

import java.math.BigDecimal;

public record UpdateServiceRequest(
        String name,
        String description,
        Integer mileageAtTheTime,
        BigDecimal cost
) {
}
