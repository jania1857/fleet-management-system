package pl.jania1857.fmsapi.dto;

public record AuthenticationRequest(
        String username,
        String password
) {
}
