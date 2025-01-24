package pl.jania1857.fmsapi.service.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.jania1857.fmsapi.dto.InsuranceDto;
import pl.jania1857.fmsapi.model.Insurance;

@Service
@RequiredArgsConstructor
public class InsuranceMapper {
    private final CostMapper costMapper;
    public InsuranceDto toDto(Insurance insurance) {
        return new InsuranceDto(
                insurance.getId(),
                insurance.getType(),
                insurance.getNumber(),
                insurance.getDescription(),
                insurance.getInsurer(),
                insurance.getStartDate(),
                insurance.getEndDate(),
                insurance.getVehicle().getId(),
                costMapper.toDto(insurance.getCost())
        );
    }
}
