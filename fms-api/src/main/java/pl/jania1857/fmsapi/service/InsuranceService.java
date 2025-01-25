package pl.jania1857.fmsapi.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.jania1857.fmsapi.dto.InsuranceDto;
import pl.jania1857.fmsapi.dto.UpdateInsuranceRequest;
import pl.jania1857.fmsapi.model.Insurance;
import pl.jania1857.fmsapi.model.Vehicle;
import pl.jania1857.fmsapi.repository.InsuranceRepository;
import pl.jania1857.fmsapi.repository.VehicleRepository;
import pl.jania1857.fmsapi.service.mapper.InsuranceMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InsuranceService {
    private final InsuranceRepository insuranceRepository;
    private final InsuranceMapper insuranceMapper;
    private final VehicleRepository vehicleRepository;

    public List<InsuranceDto> getAllInsurances() {
        return insuranceRepository.findAll().stream().map(insuranceMapper::toDto).toList();
    }

    public InsuranceDto getInsuranceById(int id) {
        Insurance insurance = insuranceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Insurance with ID: " + id + " not found"));

        return insuranceMapper.toDto(insurance);
    }

    public List<InsuranceDto> getInsurancesForVehicle(int vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle with ID: " + vehicleId + " not found"));

        return vehicle.getInsurances().stream().map(insuranceMapper::toDto).toList();
    }

    public InsuranceDto updateInsurance(int insuranceId, UpdateInsuranceRequest request) {
        Insurance insurance = insuranceRepository.findById(insuranceId)
                .orElseThrow(() -> new EntityNotFoundException("Insurance with ID: " + insuranceId + " not found"));

        insurance.setType(request.insuranceType());
        insurance.setNumber(request.insurer());
        insurance.setDescription(request.description());
        insurance.setInsurer(request.insurer());
        insurance.setStartDate(request.startDate());
        insurance.setEndDate(request.endDate());
        insurance.getCost().setAmount(request.cost());

        return insuranceMapper.toDto(insuranceRepository.save(insurance));
    }

    public void deleteInsurance(int insuranceId) {
        insuranceRepository.deleteById(insuranceId);
    }
}
