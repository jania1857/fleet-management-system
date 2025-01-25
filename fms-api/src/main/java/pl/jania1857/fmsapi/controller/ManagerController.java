package pl.jania1857.fmsapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.jania1857.fmsapi.dto.*;
import pl.jania1857.fmsapi.service.UserService;
import pl.jania1857.fmsapi.service.VehicleService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/manager")
public class ManagerController {
    private final VehicleService vehicleService;
    private final UserService userService;

    @PostMapping("/createVehicle")
    public ResponseEntity<VehicleDto> createVehicle(
            @RequestBody CreateVehicleRequest request
    ) {
        return ResponseEntity.ok(vehicleService.createVehicle(request));
    }

    @PutMapping("/updateVehicle/{vehicleId}")
    public ResponseEntity<VehicleDto> updateVehicle(
            @RequestBody UpdateVehicleRequest request,
            @PathVariable Integer vehicleId
    ) {
        return ResponseEntity.ok(vehicleService.updateVehicle(vehicleId, request));
    }

    @GetMapping("/getAllVehicles")
    public ResponseEntity<List<VehicleDto>> getAllVehicles() {
        return ResponseEntity.ok(vehicleService.getAllVehicles());
    }

    @DeleteMapping("/deleteVehicle/{vehicleId}")
    public ResponseEntity<Void> deleteVehicle(
            @PathVariable Integer vehicleId
    ) {
        vehicleService.deleteVehicleById(vehicleId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/changeStatusForVehicle/{vehicleId}")
    public ResponseEntity<ChangeStatusResponse> changeStatusForVehicle(
            @PathVariable Integer vehicleId,
            @RequestBody ChangeStatusRequest request
    ) {
        return ResponseEntity.ok(vehicleService.changeStatusForVehicle(vehicleId, request));
    }

    @PostMapping("/newInspection/{vehicleId}")
    public ResponseEntity<InspectionDto> newInspection(
            @PathVariable Integer vehicleId,
            @RequestBody NewInspectionRequest request
    ) {
        return ResponseEntity.ok(vehicleService.newInspection(vehicleId, request));
    }

    @PostMapping("/newService/{vehicleId}")
    public ResponseEntity<ServiceDto> newService(
            @PathVariable Integer vehicleId,
            @RequestBody NewServiceRequest request
    ) {
        return ResponseEntity.ok(vehicleService.newService(vehicleId, request));
    }

    @PostMapping("/newInsurance/{vehicleId}")
    public ResponseEntity<InsuranceDto> newInsurance(
            @PathVariable Integer vehicleId,
            @RequestBody NewInsuranceRequest request
    ) {
        return ResponseEntity.ok(vehicleService.newInsurance(vehicleId, request));
    }

    @PostMapping("/newMileage/{vehicleId}")
    public ResponseEntity<MileageChangeDto> newMileage(
            @PathVariable Integer vehicleId,
            @RequestBody NewMileageChangeRequest request
    ) {
        return ResponseEntity.ok(vehicleService.newMileageChange(vehicleId, request));
    }

    @PostMapping("/newAssignment/{userId}/{vehicleId}")
    public ResponseEntity<AssignmentDto> newAssignment(
            @PathVariable Integer userId,
            @PathVariable Integer vehicleId
    ) {
        return ResponseEntity.ok(vehicleService.newAssignment(vehicleId, userId));
    }

    @GetMapping("/getUserAssignments/{userId}")
    public ResponseEntity<List<UserAssignmentResponse>> getUserAssignments(
            @PathVariable Integer userId
    ) {
        return ResponseEntity.ok(userService.getUserAssignments(userId));
    }

    @GetMapping("/getDrivers")
    public ResponseEntity<List<UserDto>> getDrivers() {
        return ResponseEntity.ok(userService.getDrivers());
    }

}
