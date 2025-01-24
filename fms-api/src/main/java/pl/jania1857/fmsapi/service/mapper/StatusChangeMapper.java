package pl.jania1857.fmsapi.service.mapper;

import org.springframework.stereotype.Service;
import pl.jania1857.fmsapi.dto.StatusChangeDto;
import pl.jania1857.fmsapi.model.StatusChange;

@Service
public class StatusChangeMapper {
    public StatusChangeDto toDto(StatusChange statusChange) {
        return new StatusChangeDto(
                statusChange.getId(),
                statusChange.getNewStatus(),
                statusChange.getTimestamp(),
                statusChange.getVehicle().getId()
        );
    }
}
