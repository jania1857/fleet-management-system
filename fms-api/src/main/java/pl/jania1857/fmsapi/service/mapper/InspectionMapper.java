package pl.jania1857.fmsapi.service.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.jania1857.fmsapi.dto.InspectionDto;
import pl.jania1857.fmsapi.model.Inspection;

@Service
@RequiredArgsConstructor
public class InspectionMapper {
    private final CostMapper costMapper;

    public InspectionDto toDto(Inspection inspection) {
        return new InspectionDto(
                inspection.getId(),
                inspection.getInspectionDate(),
                inspection.getNextInspectionDate(),
                inspection.getDescription(),
                inspection.getPassed(),
                inspection.getVehicle().getId(),
                costMapper.toDto(inspection.getCost())
        );
    }
}
