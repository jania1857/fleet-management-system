package pl.jania1857.fms.user;

public record LoginRequest(
        String username,
        String password
) {
}