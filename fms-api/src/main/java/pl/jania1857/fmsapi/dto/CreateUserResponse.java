package pl.jania1857.fmsapi.dto;

public record CreateUserResponse(
        String username,
        String generatedPassword
) {
}
