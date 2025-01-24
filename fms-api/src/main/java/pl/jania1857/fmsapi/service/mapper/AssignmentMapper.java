package pl.jania1857.fmsapi.service.mapper;

import org.springframework.stereotype.Service;
import pl.jania1857.fmsapi.dto.AssignmentDto;
import pl.jania1857.fmsapi.model.Assignment;

@Service
public class AssignmentMapper {
    public AssignmentDto toDto(Assignment assignment) {
        return new AssignmentDto(
                assignment.getId(),
                assignment.getStartTime(),
                assignment.getEndTime(),
                assignment.getVehicle().getId(),
                assignment.getUser().getId()
        );
    }
}
