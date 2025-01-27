package pl.jania1857.fmsapi.dto.publicdto;

import pl.jania1857.fmsapi.dto.UserDto;

public record AuthenticationResponse(
        String token,
        UserDto userData
) {
}
