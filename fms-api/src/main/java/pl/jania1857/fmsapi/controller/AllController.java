package pl.jania1857.fmsapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.jania1857.fmsapi.dto.NewRefuelingRequest;
import pl.jania1857.fmsapi.dto.RefuelingDto;
import pl.jania1857.fmsapi.dto.UserAssignmentResponse;
import pl.jania1857.fmsapi.dto.VehicleDto;
import pl.jania1857.fmsapi.dto.all.ChangePasswordRequest;
import pl.jania1857.fmsapi.service.UserService;
import pl.jania1857.fmsapi.service.VehicleService;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/all")
public class AllController {
    private final UserService userService;
    private final VehicleService vehicleService;

    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal principal
    ) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("User not authenticated!");
        }

        userService.changePassword(request, principal);
        return ResponseEntity.ok("Password changed");
    }

    @GetMapping("/getVehicle/{vehicleId}")
    public ResponseEntity<VehicleDto> getVehicle(
            @PathVariable Integer vehicleId
    ) {
        return ResponseEntity.ok(vehicleService.getVehicleById(vehicleId));
    }

    @GetMapping("/newRefueling/{vehicleId}")
    public ResponseEntity<RefuelingDto> newRefueling(
            @PathVariable Integer vehicleId,
            @RequestBody NewRefuelingRequest request
    ) {
        return ResponseEntity.ok(vehicleService.newRefueling(vehicleId, request));
    }

    @GetMapping("/myAssignments")
    public ResponseEntity<List<UserAssignmentResponse>> getMyAssignments(Principal principal) {
        return ResponseEntity.ok(userService.getMyAssignments(principal));
    }
}
