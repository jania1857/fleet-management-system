package pl.jania1857.fmsapi.controller;

import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.jania1857.fmsapi.dto.*;
import pl.jania1857.fmsapi.service.CostService;
import pl.jania1857.fmsapi.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final UserService userService;
    private final CostService costService;

    @PostMapping("/createUser")
    public ResponseEntity<GeneratedUserCredentialsResponse> createUser(
            @RequestBody CreateUserRequest request
    ) {
        return ResponseEntity.ok(userService.createUser(request));
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/getAllManagers")
    public ResponseEntity<List<UserDto>> getAllManagers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PatchMapping("/resetPasswordForUser/{userId}")
    public ResponseEntity<GeneratedUserCredentialsResponse> resetPasswordForUser(
            @PathVariable Integer userId
    ) {
        return ResponseEntity.ok(userService.resetPasswordForUser(userId));
    }

    @PutMapping("/changeUserData/{userId}")
    public ResponseEntity<UserDto> changeUserData(
            @RequestBody ChangeUserDataRequest request,
            @PathVariable Integer userId
    ) {
        return ResponseEntity.ok(userService.changeUserData(userId, request));
    }

    @PatchMapping("/changeUserRole/{userId}")
    public ResponseEntity<UserDto> changeUserRole(
            @PathVariable Integer userId,
            @RequestBody ChangeUserRoleRequest request
    ) {
        return ResponseEntity.ok(userService.changeUserRole(userId, request));
    }

    @GetMapping("/getManagers")
    public ResponseEntity<List<UserDto>> getManagers() {
        return ResponseEntity.ok(userService.getManagers());
    }

    @DeleteMapping("/deleteUser/{userId}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Integer userId
    ) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
