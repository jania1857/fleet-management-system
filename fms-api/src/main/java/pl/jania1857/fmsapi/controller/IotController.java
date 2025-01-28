package pl.jania1857.fmsapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.jania1857.fmsapi.dto.MileageChangeDto;
import pl.jania1857.fmsapi.dto.NewMileageChangeRequest;
import pl.jania1857.fmsapi.dto.NewRefuelingRequest;
import pl.jania1857.fmsapi.dto.RefuelingDto;
import pl.jania1857.fmsapi.model.User;
import pl.jania1857.fmsapi.repository.UserRepository;
import pl.jania1857.fmsapi.service.VehicleService;

import java.security.Principal;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/v1/iot")
public class IotController {

    private final VehicleService vehicleService;
    private final UserRepository userRepository;

    @PostMapping("/newRefuelingIOT")
    public ResponseEntity<RefuelingDto> newRefuelingIOT(Principal principal, NewRefuelingRequest request) {
        String deviceUsername = principal.getName();
        User iotUser = userRepository.findByUsername(deviceUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Integer vehicleId = iotUser.getBindedVehicle().getId();
        return ok(vehicleService.newRefueling(vehicleId, request));
    }

    @PostMapping("/newMileageChangeIOT")
    public ResponseEntity<MileageChangeDto> newMileageChangeIOT(Principal principal, NewMileageChangeRequest request) {
        String deviceUsername = principal.getName();
        User iotUser = userRepository.findByUsername(deviceUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Integer vehicleId = iotUser.getBindedVehicle().getId();
        return ok(vehicleService.newMileageChange(vehicleId, request));
    }
}
