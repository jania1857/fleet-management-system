package pl.jania1857.fmsapi.dto;

import pl.jania1857.fmsapi.utils.FuelType;

import java.util.List;

public record VehicleDto(
        Integer id,
        String manufacturer,
        String model,
        Integer year,
        String registrationNumber,
        String vin,
        FuelType fuelType,
        Integer displacement,
        String fuelCardNumber,
        List<StatusChangeDto> statusChanges,
        List<RefuelingDto> refuelings,
        List<InspectionDto> inspections,
        List<ServiceDto> services,
        List<InsuranceDto> insurances,
        List<MileageChangeDto> mileageChanges,
        List<AssignmentDto> assignments
) {
}
