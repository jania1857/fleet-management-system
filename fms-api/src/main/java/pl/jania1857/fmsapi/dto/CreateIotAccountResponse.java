package pl.jania1857.fmsapi.dto;

import pl.jania1857.fmsapi.model.User;

public record CreateIotAccountResponse(
        User iotUser,
        String password
) {
}
