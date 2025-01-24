package pl.jania1857.fmsapi.dto;

import pl.jania1857.fmsapi.utils.FuelType;

import java.time.LocalDateTime;

public record CreateVehicleRequest(
        String manufacturer,
        String model,
        Integer year,
        String registrationNumber,
        String vin,
        FuelType fuelType,
        Integer displacement,

        Integer mileage,

        LocalDateTime insuranceStart,
        LocalDateTime insuranceEnd,
        String insurer,

        LocalDateTime inspectionDate,
        LocalDateTime nextInspectionDate
) {
}
