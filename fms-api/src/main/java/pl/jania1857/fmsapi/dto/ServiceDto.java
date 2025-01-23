package pl.jania1857.fmsapi.dto;

public record ServiceDto(
        Integer id,
        String name,
        String description,
        Integer mileageAtTheTime,
        Integer vehicleId,
        Integer costId
) {
}
