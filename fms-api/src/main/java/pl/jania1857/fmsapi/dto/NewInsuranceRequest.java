package pl.jania1857.fmsapi.dto;

import pl.jania1857.fmsapi.utils.InsuranceType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record NewInsuranceRequest(
        InsuranceType insuranceType,
        String number,
        String description,
        String insurer,
        LocalDateTime startDate,
        BigDecimal cost
) {
}
