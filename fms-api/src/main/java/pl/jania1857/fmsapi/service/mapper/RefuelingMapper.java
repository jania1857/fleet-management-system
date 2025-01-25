package pl.jania1857.fmsapi.service.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.jania1857.fmsapi.dto.RefuelingDto;
import pl.jania1857.fmsapi.model.Refueling;

@Service
@RequiredArgsConstructor
public class RefuelingMapper {
    private final CostMapper costMapper;

    public RefuelingDto toDto(Refueling refueling) {
        return new RefuelingDto(
                refueling.getId(),
                refueling.getFuel(),
                refueling.getPrice(),
                refueling.getQuantity(),
                refueling.getAmount(),
                refueling.getTimestamp(),
                refueling.getVehicle().getId(),
                refueling.getCost() == null
                        ? null
                        : costMapper.toDto(refueling.getCost())
        );
    }
}
