package pl.jania1857.fmsapi.dto;

public record MileageChangeDto(
        Integer id,
        Integer newMileage,
        Integer vehicleId
) {
}
