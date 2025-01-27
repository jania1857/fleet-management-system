package pl.jania1857.fmsapi.service.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.jania1857.fmsapi.dto.publicdto.AuthenticationRequest;
import pl.jania1857.fmsapi.dto.publicdto.AuthenticationResponse;
import pl.jania1857.fmsapi.model.User;
import pl.jania1857.fmsapi.repository.UserRepository;
import pl.jania1857.fmsapi.service.mapper.UserMapper;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    public AuthenticationResponse login(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new UsernameNotFoundException(request.username()));
        String token = jwtService.generateToken(user);

        return new AuthenticationResponse(token, userMapper.toDto(user));
    }
}
