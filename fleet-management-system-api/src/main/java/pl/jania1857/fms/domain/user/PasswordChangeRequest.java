package pl.jania1857.fms.domain.user;

public record PasswordChangeRequest(
        String username,
        String oldPassword,
        String newPassword
) {
}
