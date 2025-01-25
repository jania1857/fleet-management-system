package pl.jania1857.fmsapi.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.jania1857.fmsapi.dto.AssignmentDto;
import pl.jania1857.fmsapi.model.Assignment;
import pl.jania1857.fmsapi.model.User;
import pl.jania1857.fmsapi.model.Vehicle;
import pl.jania1857.fmsapi.repository.AssignmentRepository;
import pl.jania1857.fmsapi.repository.UserRepository;
import pl.jania1857.fmsapi.repository.VehicleRepository;
import pl.jania1857.fmsapi.service.mapper.AssignmentMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssignmentService {
    private final AssignmentRepository assignmentRepository;
    private final AssignmentMapper assignmentMapper;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;

    public List<AssignmentDto> getAllAssignments() {
        return assignmentRepository.findAll().stream().map(assignmentMapper::toDto).toList();
    }

    public AssignmentDto getAssignmentById(int id) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Assignment with id %d not found", id)));
        return assignmentMapper.toDto(assignment);
    }

    public List<AssignmentDto> getAssignmentsForVehicle(int vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Vehicle with id %d not found", vehicleId)));

        List<Assignment> vehicleAssignments = vehicle.getAssignments();
        return vehicleAssignments.stream().map(assignmentMapper::toDto).toList();
    }

    public AssignmentDto endAssignment(
            Integer assignmentId
    ) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new EntityNotFoundException("Assignment with id " + assignmentId + " not found"));

        if (assignment.getEndTime() != null) {
            throw new IllegalArgumentException("Assignment with id " + assignmentId + " has been already closed!");
        }
        assignment.setEndTime(LocalDateTime.now());
        Assignment savedAssignment = assignmentRepository.save(assignment);
        return assignmentMapper.toDto(savedAssignment);
    }

    public void deleteAssignment(int assignmentId) {
        assignmentRepository.deleteById(assignmentId);
    }
}
