package pl.jania1857.fmsapi.dto;

import pl.jania1857.fmsapi.utils.FuelType;

public record UpdateVehicleRequest(
        String manufacturer,
        String model,
        Integer year,
        String registrationNumber,
        String vin,
        FuelType fuelType,
        Integer displacement
) {
}
