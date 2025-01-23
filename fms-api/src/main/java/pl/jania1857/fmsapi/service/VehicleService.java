package pl.jania1857.fmsapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import pl.jania1857.fmsapi.dto.CreateVehicleRequest;
import pl.jania1857.fmsapi.dto.VehicleDto;
import pl.jania1857.fmsapi.repository.VehicleRepository;

@Service
@RequiredArgsConstructor
public class VehicleService {
    private final VehicleRepository vehicleRepository;

    public VehicleDto createVehicle(
            @RequestBody CreateVehicleRequest request
    ) {
        return null;
    }
}
