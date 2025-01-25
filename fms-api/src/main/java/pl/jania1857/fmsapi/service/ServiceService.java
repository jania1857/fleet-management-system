package pl.jania1857.fmsapi.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.jania1857.fmsapi.dto.ServiceDto;
import pl.jania1857.fmsapi.dto.UpdateServiceRequest;
import pl.jania1857.fmsapi.model.Vehicle;
import pl.jania1857.fmsapi.repository.ServiceRepository;
import pl.jania1857.fmsapi.repository.VehicleRepository;
import pl.jania1857.fmsapi.service.mapper.ServiceMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceService {
    private final ServiceRepository serviceRepository;
    private final ServiceMapper serviceMapper;
    private final VehicleRepository vehicleRepository;

    public List<ServiceDto> getAllServices() {
        return serviceRepository.findAll().stream().map(serviceMapper::toDto).toList();
    }

    public ServiceDto getServiceById(int id) {
        return serviceRepository.findById(id).map(serviceMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Service with id " + id + " not found"));
    }

    public List<ServiceDto> getServicesForVehicle(int vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle " + vehicleId + " not found"));

        List<pl.jania1857.fmsapi.model.Service> vehicleServices = vehicle.getServices();

        return vehicleServices.stream().map(serviceMapper::toDto).toList();
    }

    public ServiceDto updateService(int serviceId, UpdateServiceRequest request) {
        pl.jania1857.fmsapi.model.Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new EntityNotFoundException("Service with id " + serviceId + " not found"));

        service.setName(request.name());
        service.setDescription(request.description());
        service.setMileageAtTheTime(request.mileageAtTheTime());
        service.getCost().setAmount(request.cost());

        return serviceMapper.toDto(serviceRepository.save(service));
    }

    public void deleteService(int serviceId) {
        serviceRepository.deleteById(serviceId);
    }
}
