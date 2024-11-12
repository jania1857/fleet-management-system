package pl.jania1857.fms.user;

public record RegisterResponse(
        String username,
        String oneTimePassword
) {
}
