package pl.jania1857.fmsapi.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.jania1857.fmsapi.dto.RefuelingDto;
import pl.jania1857.fmsapi.model.Vehicle;
import pl.jania1857.fmsapi.repository.RefuelingRepository;
import pl.jania1857.fmsapi.repository.VehicleRepository;
import pl.jania1857.fmsapi.service.mapper.RefuelingMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RefuelingService {

    private final RefuelingRepository refuelingRepository;
    private final RefuelingMapper refuelingMapper;
    private final VehicleRepository vehicleRepository;

    public List<RefuelingDto> getAllRefuelings() {
        return refuelingRepository.findAll().stream().map(refuelingMapper::toDto).toList();
    }

    public RefuelingDto getRefuelingById(int id) {
        return refuelingRepository.findById(id).map(refuelingMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Refueling with ID: " + id + " not found"));
    }

    public List<RefuelingDto> getRefuelingsForVehicle(int vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle with ID: " + vehicleId + " not found"));

        return vehicle.getRefuelings().stream().map(refuelingMapper::toDto).toList();
    }

    public void deleteRefueling(int id) {
        refuelingRepository.deleteById(id);
    }
}
