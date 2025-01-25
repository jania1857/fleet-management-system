package pl.jania1857.fmsapi.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.jania1857.fmsapi.dto.CostDto;
import pl.jania1857.fmsapi.dto.UpdateCostRequest;
import pl.jania1857.fmsapi.model.Cost;
import pl.jania1857.fmsapi.model.Vehicle;
import pl.jania1857.fmsapi.repository.CostRepository;
import pl.jania1857.fmsapi.repository.VehicleRepository;
import pl.jania1857.fmsapi.service.mapper.CostMapper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CostService {
    private final CostRepository costRepository;
    private final CostMapper costMapper;
    private final VehicleRepository vehicleRepository;

    public List<CostDto> getAllCosts() {
        List<Cost> costs = costRepository.findAll();

        return costs.stream().map(costMapper::toDto).toList();
    }

    public CostDto getCostById(int costId) {
        Cost cost = costRepository.findById(costId)
                .orElseThrow(() -> new EntityNotFoundException("Cost with ID: " + costId + " not found"));

        return costMapper.toDto(cost);
    }

    public List<CostDto> getAllCostsForVehicle(int vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle with ID: " + vehicleId + " not found"));

        List<Cost> vehicleCosts = new ArrayList<>();

        vehicle.getServices().forEach(service -> {
            if (service.getCost() != null) {
                vehicleCosts.add(service.getCost());
            }
        });

        vehicle.getRefuelings().forEach(refuel -> {
            if (refuel.getCost() != null) {
                vehicleCosts.add(refuel.getCost());
            }
        });

        vehicle.getInspections().forEach(inspection -> {
            if (inspection.getCost() != null) {
                vehicleCosts.add(inspection.getCost());
            }
        });

        vehicle.getInsurances().forEach(insurance -> {
            if (insurance.getCost() != null) {
                vehicleCosts.add(insurance.getCost());
            }
        });

        vehicleCosts.sort(Comparator.comparing(Cost::getTimestamp).reversed());

        return vehicleCosts.stream().map(costMapper::toDto).toList();
    }

    public CostDto updateCost(int costId, UpdateCostRequest request) {
        Cost cost = costRepository.findById(costId)
                .orElseThrow(() -> new EntityNotFoundException("Cost with ID: " + costId + " not found"));

        cost.setAmount(request.amount());
        Cost savedCost = costRepository.save(cost);

        return costMapper.toDto(savedCost);
    }
}
