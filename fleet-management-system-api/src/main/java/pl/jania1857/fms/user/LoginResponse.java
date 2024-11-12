package pl.jania1857.fms.user;

public record LoginResponse(
        String token,
        boolean isChangedPassword
) {
}
