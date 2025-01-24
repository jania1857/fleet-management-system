package pl.jania1857.fmsapi.service.mapper;

import org.springframework.stereotype.Service;
import pl.jania1857.fmsapi.dto.MileageChangeDto;
import pl.jania1857.fmsapi.model.MileageChange;

@Service
public class MileageChangeMapper {
    public MileageChangeDto toDto(MileageChange mileageChange) {
        return new MileageChangeDto(
                mileageChange.getId(),
                mileageChange.getNewMileage(),
                mileageChange.getVehicle().getId()
        );
    }
}
