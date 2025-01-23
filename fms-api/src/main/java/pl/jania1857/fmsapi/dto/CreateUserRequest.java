package pl.jania1857.fmsapi.dto;

import pl.jania1857.fmsapi.utils.Role;

public record CreateUserRequest(
        String firstname,
        String lastname,
        Role role
) {
}
