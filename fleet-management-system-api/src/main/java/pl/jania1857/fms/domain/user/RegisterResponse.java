package pl.jania1857.fms.domain.user;

public record RegisterResponse(
        String username,
        String oneTimePassword
) {
}
