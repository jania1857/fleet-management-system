package pl.jania1857.fmsapi.dto;

import pl.jania1857.fmsapi.utils.Role;

import java.util.List;

public record UserDto(
        Integer id,
        String username,
        String firstname,
        String lastname,
        Role role,
        List<AssignmentDto> assignments
) {
}
