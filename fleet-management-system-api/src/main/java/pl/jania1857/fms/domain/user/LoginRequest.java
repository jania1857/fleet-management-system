package pl.jania1857.fms.domain.user;

public record LoginRequest(
        String username,
        String password
) {
}
