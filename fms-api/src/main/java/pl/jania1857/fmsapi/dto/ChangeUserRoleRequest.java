package pl.jania1857.fmsapi.dto;

import pl.jania1857.fmsapi.utils.Role;

public record ChangeUserRoleRequest(
        Role newRole
) {
}
