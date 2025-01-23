package pl.jania1857.fmsapi.dto.all;

public record ChangePasswordRequest(
        String oldPassword,
        String newPassword,
        String confirmPassword
) {
}
