package pl.jania1857.fmsapi.service.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.jania1857.fmsapi.dto.VehicleDto;
import pl.jania1857.fmsapi.model.Vehicle;

@Service
@RequiredArgsConstructor
public class VehicleMapper {
    private final StatusChangeMapper statusChangeMapper;
    private final RefuelingMapper refuelingMapper;
    private final ServiceMapper serviceMapper;
    private final InsuranceMapper insuranceMapper;
    private final MileageChangeMapper mileageChangeMapper;
    private final AssignmentMapper assignmentMapper;
    private final InspectionMapper inspectionMapper;

    public VehicleDto toDto(Vehicle vehicle) {
        return new VehicleDto(
                vehicle.getId(),
                vehicle.getManufacturer(),
                vehicle.getModel(),
                vehicle.getYear(),
                vehicle.getRegistrationNumber(),
                vehicle.getVin(),
                vehicle.getFuelType(),
                vehicle.getDisplacement(),
                vehicle.getFuelCardNumber(),
                vehicle.getStatusChanges().stream().map(statusChangeMapper::toDto).toList(),
                vehicle.getRefuelings().stream().map(refuelingMapper::toDto).toList(),
                vehicle.getInspections().stream().map(inspectionMapper::toDto).toList(),
                vehicle.getServices().stream().map(serviceMapper::toDto).toList(),
                vehicle.getInsurances().stream().map(insuranceMapper::toDto).toList(),
                vehicle.getMileageChanges().stream().map(mileageChangeMapper::toDto).toList(),
                vehicle.getAssignments().stream().map(assignmentMapper::toDto).toList()
        );
    }
}
