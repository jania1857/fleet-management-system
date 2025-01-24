package pl.jania1857.fmsapi.service.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.jania1857.fmsapi.dto.ServiceDto;

@Service
@RequiredArgsConstructor
public class ServiceMapper {
    private final CostMapper costMapper;
    public ServiceDto toDto(pl.jania1857.fmsapi.model.Service service) {
        return new ServiceDto(
                service.getId(),
                service.getName(),
                service.getDescription(),
                service.getMileageAtTheTime(),
                service.getVehicle().getId(),
                costMapper.toDto(service.getCost())
        );
    }
}
