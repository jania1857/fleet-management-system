package pl.jania1857.fmsapi.service.mapper;

import org.springframework.stereotype.Service;
import pl.jania1857.fmsapi.dto.CostDto;
import pl.jania1857.fmsapi.model.Cost;

@Service
public class CostMapper {
    public CostDto toDto(Cost cost) {
        if (cost.getRefueling() != null) {
            return new CostDto(
                    cost.getId(),
                    cost.getAmount(),
                    cost.getRefueling().getId(),
                    null,
                    null,
                    null,
                    cost.getTimestamp()
            );
        } else if (cost.getInspection() != null) {
            return new CostDto(
                    cost.getId(),
                    cost.getAmount(),
                    null,
                    cost.getInspection().getId(),
                    null,
                    null,
                    cost.getTimestamp()
            );
        } else if (cost.getService() != null) {
            return new CostDto(
                    cost.getId(),
                    cost.getAmount(),
                    null,
                    null,
                    cost.getService().getId(),
                    null,
                    cost.getTimestamp()
            );
        } else if (cost.getInsurance() != null) {
            return new CostDto(
                    cost.getId(),
                    cost.getAmount(),
                    null,
                    null,
                    null,
                    cost.getInsurance().getId(),
                    cost.getTimestamp()
            );
        }
        return new CostDto(
                cost.getId(),
                cost.getAmount(),
                null,
                null,
                null,
                null,
                cost.getTimestamp()
        );
    }
}
