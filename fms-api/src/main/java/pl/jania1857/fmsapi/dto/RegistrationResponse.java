package pl.jania1857.fmsapi.dto;

public record RegistrationResponse(
        String token,
        String username,
        String generatedPassword
) {
}
