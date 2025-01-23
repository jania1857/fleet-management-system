package pl.jania1857.fmsapi.dto.publicdto;

public record AuthenticationRequest(
        String username,
        String password
) {
}
