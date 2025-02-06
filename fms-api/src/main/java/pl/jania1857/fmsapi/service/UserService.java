package pl.jania1857.fmsapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.jania1857.fmsapi.dto.*;
import pl.jania1857.fmsapi.dto.all.ChangePasswordRequest;
import pl.jania1857.fmsapi.model.Assignment;
import pl.jania1857.fmsapi.model.User;
import pl.jania1857.fmsapi.model.Vehicle;
import pl.jania1857.fmsapi.repository.UserRepository;
import pl.jania1857.fmsapi.service.mapper.UserMapper;
import pl.jania1857.fmsapi.service.mapper.VehicleMapper;
import pl.jania1857.fmsapi.utils.Role;

import java.security.Principal;
import java.security.SecureRandom;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VehicleMapper vehicleMapper;
    private final UserMapper userMapper;

    public GeneratedUserCredentialsResponse createUser(CreateUserRequest request) {

        String password = generateSecurePassword();
        String username = generateUsername(request.firstname(), request.lastname());

        User user = new User();

        user.setFirstname(request.firstname());
        user.setLastname(request.lastname());
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(request.role());

        userRepository.save(user);

        return new GeneratedUserCredentialsResponse(username, password);
    }

    public CreateIotAccountResponse newIotAccount(Vehicle vehicle) {
        String password = generateSecurePassword();
        String firstname = vehicle.getManufacturer();
        String lastname = vehicle.getModel();
        String username = generateUsername(firstname, lastname);

        User iotUser = new User();

        iotUser.setFirstname(firstname);
        iotUser.setLastname(lastname);
        iotUser.setUsername(username);
        iotUser.setPassword(passwordEncoder.encode(password));
        iotUser.setRole(Role.IOT);

        return new CreateIotAccountResponse(iotUser, password);
    }

    public void changePassword(ChangePasswordRequest request, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username: " + username + " not found"));

        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
            throw new BadCredentialsException("Old password does not match");
        }

        if (!request.newPassword().equals(request.confirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    public GeneratedUserCredentialsResponse resetPasswordForUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException(String.valueOf(userId)));

        String generatedPassword = generateSecurePassword();

        user.setPassword(passwordEncoder.encode(generatedPassword));
        User savedUser = userRepository.save(user);
        return new GeneratedUserCredentialsResponse(user.getUsername(), generatedPassword);
    }

    public List<UserAssignmentResponse> getUserAssignments(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException(String.valueOf(userId)));

        List<Assignment> assignments = user.getAssignments();

        return assignments.stream().map(
                assignment -> new UserAssignmentResponse(
                        vehicleMapper.toDto(assignment.getVehicle()),
                        assignment.getStartTime(),
                        assignment.getEndTime()
                )
        ).toList();
    }

    public List<UserAssignmentResponse> getMyAssignments(Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username: " + username + " not found"));

        List<Assignment> assignments = user.getAssignments();

        return assignments.stream().map(
                assignment -> new UserAssignmentResponse(
                        vehicleMapper.toDto(assignment.getVehicle()),
                        assignment.getStartTime(),
                        assignment.getEndTime()
                )
        ).toList();
    }

    public UserDto changeUserData(Integer userId, ChangeUserDataRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException(String.valueOf(userId)));

        user.setFirstname(request.firstname());
        user.setLastname(request.lastname());

        User savedUser = userRepository.save(user);

        return userMapper.toDto(savedUser);
    }

    public UserDto changeUserRole(Integer userId, ChangeUserRoleRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException(String.valueOf(userId)));

        user.setRole(request.newRole());

        User savedUser = userRepository.save(user);

        return userMapper.toDto(savedUser);
    }

    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(userMapper::toDto).toList();
    }

    public List<UserDto> getDrivers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .filter(user -> user.getRole() == Role.DRIVER)
                .map(userMapper::toDto).toList();
    }

    public List<UserDto> getManagers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .filter(user -> user.getRole() == Role.MANAGER)
                .map(userMapper::toDto).toList();
    }

    public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
    }


    /// PRIVATE METHODS
    private String generateUsername(String firstname, String lastname) {
        String baseUsername = (firstname.length() > 3 ? firstname.substring(0, 3).toLowerCase() : firstname.toLowerCase()) +
                (lastname.length() > 3 ? lastname.substring(0, 3).toLowerCase() : lastname.toLowerCase());

        String uniqueUsername = baseUsername;
        int counter = 1;
        while (userRepository.existsByUsername(uniqueUsername)) {
            uniqueUsername = baseUsername + counter;
            counter++;
        }

        return uniqueUsername;
    }

    public String generateSecurePassword() {
        String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String specialCharacters = "!@#$%^&*()-_=+[]{}|;:,.<>?";
        String allcharacters = upperCaseLetters + lowerCaseLetters + digits + specialCharacters;

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        password.append(upperCaseLetters.charAt(random.nextInt(upperCaseLetters.length())));
        password.append(lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length())));
        password.append(digits.charAt(random.nextInt(digits.length())));
        password.append(specialCharacters.charAt(random.nextInt(specialCharacters.length())));

        for (int i = 4; i < 8; i++) {
            password.append(allcharacters.charAt(random.nextInt(allcharacters.length())));
        }

        return shuffleString(password.toString());
    }

    private String shuffleString(String input) {
        SecureRandom random = new SecureRandom();
        char[] array = input.toCharArray();
        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            char temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
        return new String(array);
    }
}

