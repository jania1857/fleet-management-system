package pl.jania1857.fmsapi.dto;

import pl.jania1857.fmsapi.utils.FuelType;
import pl.jania1857.fmsapi.utils.InsuranceType;

import java.time.LocalDateTime;

public record CreateVehicleRequest(
        String manufacturer,
        String model,
        Integer year,
        String registrationNumber,
        String vin,
        FuelType fuelType,
        Integer displacement,
        String fuelCardNumber,

        Integer mileage,

        LocalDateTime insuranceStart,
        LocalDateTime insuranceEnd,
        InsuranceType insuranceType,
        String insuranceNumber,
        String insurer,

        LocalDateTime inspectionDate,
        LocalDateTime nextInspectionDate
) {
}
