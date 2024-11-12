package pl.jania1857.fms.user;

public record PasswordChangeRequest(
        String oldPassword,
        String newPassword
) {
}
