package pl.jania1857.fms.domain.user;

public record RegisterRequest(
        String firstname,
        String lastname,
        String username,
        Role role
) {
}
