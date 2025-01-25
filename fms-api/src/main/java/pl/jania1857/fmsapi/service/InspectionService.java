package pl.jania1857.fmsapi.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.jania1857.fmsapi.dto.InspectionDto;
import pl.jania1857.fmsapi.dto.UpdateInspectionRequest;
import pl.jania1857.fmsapi.model.Inspection;
import pl.jania1857.fmsapi.model.Vehicle;
import pl.jania1857.fmsapi.repository.InspectionRepository;
import pl.jania1857.fmsapi.repository.VehicleRepository;
import pl.jania1857.fmsapi.service.mapper.InspectionMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InspectionService {
    private final InspectionRepository inspectionRepository;
    private final InspectionMapper inspectionMapper;
    private final VehicleRepository vehicleRepository;

    public List<InspectionDto> getAllInspections() {
        return inspectionRepository.findAll().stream().map(inspectionMapper::toDto).toList();
    }

    public InspectionDto getInspectionById(int id) {
        Inspection inspection = inspectionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Inspection with id " + id + " not found"));
        return inspectionMapper.toDto(inspection);
    }

    public List<InspectionDto> getInspectionsForVehicle(int vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle with id " + vehicleId + " not found"));

        List<Inspection> vehicleInspections = vehicle.getInspections();

        return vehicleInspections.stream().map(inspectionMapper::toDto).toList();
    }

    public void deleteInspection(int id) {
        inspectionRepository.deleteById(id);
    }

    public InspectionDto updateInspection(int inspectionId, UpdateInspectionRequest request) {
        Inspection inspection = inspectionRepository.findById(inspectionId)
                .orElseThrow(() -> new EntityNotFoundException("Inspection with id " + inspectionId + " not found"));

        inspection.setInspectionDate(request.inspectionDate());
        inspection.setNextInspectionDate(request.nextInspectionDate());
        inspection.setDescription(request.description());
        inspection.setPassed(request.passed());
        inspection.getCost().setAmount(request.cost());

        Inspection updatedInspection = inspectionRepository.save(inspection);

        return inspectionMapper.toDto(updatedInspection);
    }
}
