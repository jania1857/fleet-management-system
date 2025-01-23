package pl.jania1857.fmsapi.dto;

import pl.jania1857.fmsapi.utils.FuelType;

public record CreateVehicleRequest(
        String manufacturer,
        String model,
        String year,
        String registrationNumber,
        String vin,
        FuelType fuelType,
        Integer displacement
) {
}
