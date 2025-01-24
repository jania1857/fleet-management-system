package pl.jania1857.fmsapi.service.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.jania1857.fmsapi.dto.UserDto;
import pl.jania1857.fmsapi.model.User;

@Service
@RequiredArgsConstructor
public class UserMapper {
    private final AssignmentMapper assignmentMapper;

    public UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getFirstname(),
                user.getLastname(),
                user.getRole(),
                user.getAssignments().stream().map(assignmentMapper::toDto).toList()
        );
    }
}
