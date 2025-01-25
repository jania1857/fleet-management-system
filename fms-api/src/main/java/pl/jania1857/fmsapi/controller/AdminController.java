package pl.jania1857.fmsapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.jania1857.fmsapi.dto.*;
import pl.jania1857.fmsapi.service.CostService;
import pl.jania1857.fmsapi.service.InspectionService;
import pl.jania1857.fmsapi.service.RefuelingService;
import pl.jania1857.fmsapi.service.UserService;

import java.util.List;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final UserService userService;
    private final CostService costService;
    private final RefuelingService refuelingService;
    private final InspectionService inspectionService;

    @PostMapping("/createUser")
    public ResponseEntity<GeneratedUserCredentialsResponse> createUser(
            @RequestBody CreateUserRequest request
    ) {
        return ok(userService.createUser(request));
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ok(userService.getAllUsers());
    }

    @GetMapping("/getAllManagers")
    public ResponseEntity<List<UserDto>> getAllManagers() {
        return ok(userService.getAllUsers());
    }

    @PatchMapping("/resetPasswordForUser/{userId}")
    public ResponseEntity<GeneratedUserCredentialsResponse> resetPasswordForUser(
            @PathVariable Integer userId
    ) {
        return ok(userService.resetPasswordForUser(userId));
    }

    @PutMapping("/changeUserData/{userId}")
    public ResponseEntity<UserDto> changeUserData(
            @RequestBody ChangeUserDataRequest request,
            @PathVariable Integer userId
    ) {
        return ok(userService.changeUserData(userId, request));
    }

    @PatchMapping("/changeUserRole/{userId}")
    public ResponseEntity<UserDto> changeUserRole(
            @PathVariable Integer userId,
            @RequestBody ChangeUserRoleRequest request
    ) {
        return ok(userService.changeUserRole(userId, request));
    }

    @GetMapping("/getManagers")
    public ResponseEntity<List<UserDto>> getManagers() {
        return ok(userService.getManagers());
    }

    @DeleteMapping("/deleteUser/{userId}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Integer userId
    ) {
        userService.deleteUser(userId);
        return noContent().build();
    }

    @DeleteMapping("/deleteRefueling/{refuelingId}")
    public ResponseEntity<Void> deleteRefueling(
            @PathVariable Integer refuelingId
    ) {
        refuelingService.deleteRefueling(refuelingId);
        return noContent().build();
    }

    @DeleteMapping("/deleteInspection/{inspectionId}")
    public ResponseEntity<Void> deleteInspection(
            @PathVariable Integer inspectionId
    ) {
        inspectionService.deleteInspection(inspectionId);
        return noContent().build();
    }
}
