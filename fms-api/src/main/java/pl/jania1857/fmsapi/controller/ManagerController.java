package pl.jania1857.fmsapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.jania1857.fmsapi.dto.*;
import pl.jania1857.fmsapi.service.*;
import pl.jania1857.fmsapi.dto.UpdateCostRequest;

import java.util.List;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/manager")
public class ManagerController {
    private final VehicleService vehicleService;
    private final UserService userService;
    private final CostService costService;
    private final RefuelingService refuelingService;
    private final InspectionService inspectionService;

    @PostMapping("/createVehicle")
    public ResponseEntity<VehicleDto> createVehicle(
            @RequestBody CreateVehicleRequest request
    ) {
        return ok(vehicleService.createVehicle(request));
    }

    @PutMapping("/updateVehicle/{vehicleId}")
    public ResponseEntity<VehicleDto> updateVehicle(
            @RequestBody UpdateVehicleRequest request,
            @PathVariable Integer vehicleId
    ) {
        return ok(vehicleService.updateVehicle(vehicleId, request));
    }

    @GetMapping("/getAllVehicles")
    public ResponseEntity<List<VehicleDto>> getAllVehicles() {
        return ok(vehicleService.getAllVehicles());
    }

    @DeleteMapping("/deleteVehicle/{vehicleId}")
    public ResponseEntity<Void> deleteVehicle(
            @PathVariable Integer vehicleId
    ) {
        vehicleService.deleteVehicleById(vehicleId);
        return noContent().build();
    }

    @PatchMapping("/changeStatusForVehicle/{vehicleId}")
    public ResponseEntity<ChangeStatusResponse> changeStatusForVehicle(
            @PathVariable Integer vehicleId,
            @RequestBody ChangeStatusRequest request
    ) {
        return ok(vehicleService.changeStatusForVehicle(vehicleId, request));
    }

    @PostMapping("/newInspection/{vehicleId}")
    public ResponseEntity<InspectionDto> newInspection(
            @PathVariable Integer vehicleId,
            @RequestBody NewInspectionRequest request
    ) {
        return ok(vehicleService.newInspection(vehicleId, request));
    }

    @PostMapping("/newService/{vehicleId}")
    public ResponseEntity<ServiceDto> newService(
            @PathVariable Integer vehicleId,
            @RequestBody NewServiceRequest request
    ) {
        return ok(vehicleService.newService(vehicleId, request));
    }

    @PostMapping("/newInsurance/{vehicleId}")
    public ResponseEntity<InsuranceDto> newInsurance(
            @PathVariable Integer vehicleId,
            @RequestBody NewInsuranceRequest request
    ) {
        return ok(vehicleService.newInsurance(vehicleId, request));
    }

    @PostMapping("/newMileage/{vehicleId}")
    public ResponseEntity<MileageChangeDto> newMileage(
            @PathVariable Integer vehicleId,
            @RequestBody NewMileageChangeRequest request
    ) {
        return ok(vehicleService.newMileageChange(vehicleId, request));
    }

    @PostMapping("/newAssignment/{userId}/{vehicleId}")
    public ResponseEntity<AssignmentDto> newAssignment(
            @PathVariable Integer userId,
            @PathVariable Integer vehicleId
    ) {
        return ok(vehicleService.newAssignment(vehicleId, userId));
    }

    @PatchMapping("/endAssignment/{assignmentId}")
    public ResponseEntity<AssignmentDto> endAssignment(
            @PathVariable Integer assignmentId
    ) {
        return ok(vehicleService.endAssignment(assignmentId));
    }

    @GetMapping("/getUserAssignments/{userId}")
    public ResponseEntity<List<UserAssignmentResponse>> getUserAssignments(
            @PathVariable Integer userId
    ) {
        return ok(userService.getUserAssignments(userId));
    }

    @GetMapping("/getDrivers")
    public ResponseEntity<List<UserDto>> getDrivers() {
        return ok(userService.getDrivers());
    }

    @GetMapping("/getAllCosts")
    public ResponseEntity<List<CostDto>> getCosts() {
        return ok(costService.getAllCosts());
    }

    @GetMapping("/getCostById/{costId}")
    ResponseEntity<CostDto> getCostById(@PathVariable Integer costId) {
        return ok(costService.getCostById(costId));
    }

    @GetMapping("/getAllCostsForVehicle")
    public ResponseEntity<List<CostDto>> getAllCostsForVehicle(@RequestParam Integer vehicleId) {
        return ok(costService.getAllCostsForVehicle(vehicleId));
    }

    @PatchMapping("/updateCost/{costId}")
    public ResponseEntity<CostDto> updateCost(@PathVariable Integer costId, @RequestBody UpdateCostRequest request) {
        return ok(costService.updateCost(costId, request));
    }

    @GetMapping("/getAllRefuelings")
    public ResponseEntity<List<RefuelingDto>> getAllRefuelings() {
        return ok(refuelingService.getAllRefuelings());
    }

    @GetMapping("/getRefuelingById/{refuelingId}")
    public ResponseEntity<RefuelingDto> getRefuelingById(@PathVariable Integer refuelingId) {
        return ok(refuelingService.getRefuelingById(refuelingId));
    }

    @GetMapping("/getRefuelingsForVehicle/{vehicleId}")
    public ResponseEntity<List<RefuelingDto>> getRefuelingsForVehicle(@PathVariable Integer vehicleId) {
        return ok(refuelingService.getRefuelingsForVehicle(vehicleId));
    }

    @GetMapping("/getAllInspections")
    public ResponseEntity<List<InspectionDto>> getAllInspections() {
        return ok(inspectionService.getAllInspections());
    }

    @GetMapping("/getInspectionById/{inspectionId}")
    public ResponseEntity<InspectionDto> getInspectionById(@PathVariable Integer inspectionId) {
        return ok(inspectionService.getInspectionById(inspectionId));
    }

    @GetMapping("/getInspectionsForVehicle/{vehicleId}")
    public ResponseEntity<List<InspectionDto>> getInspectionsForVehicle(@PathVariable Integer vehicleId) {
        return ok(inspectionService.getInspectionsForVehicle(vehicleId));
    }

    @PatchMapping("/updateInspection/{inspectionId}")
    public ResponseEntity<InspectionDto> updateInspection(@PathVariable Integer inspectionId, @RequestBody UpdateInspectionRequest request) {
        return ok(inspectionService.updateInspection(inspectionId, request));
    }
}
