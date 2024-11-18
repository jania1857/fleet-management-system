package pl.jania1857.fms.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.jania1857.fms.security.JwtService;
import pl.jania1857.fms.utils.PasswordGenerator;

import java.util.Optional;

import static pl.jania1857.fms.domain.user.Role.DRIVER;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordGenerator passwordGenerator;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    public RegisterResponse register(RegisterRequest request) {

        final String generatedPassword = passwordGenerator.generateOneTimePassword();

        User user = User.builder()
                .firstname(request.firstname())
                .lastname(request.lastname())
                .username(request.username() == null || request.username().isEmpty() ? generateUsername(request.firstname(), request.lastname()) : request.username())
                .password(passwordEncoder.encode(generatedPassword))
                .role(request.role() == null ? DRIVER : request.role())
                .enabled(false)
                .build();

        repository.save(user);
        return new RegisterResponse(
                user.getUsername(),
                generatedPassword
        );
    }

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        User user = repository.findByUsername(request.username())
                .orElseThrow(() -> new UsernameNotFoundException(request.username()));

        String jwtToken = jwtService.generateToken(user);
        LoginResponse response = new LoginResponse(
                jwtToken
        );

        repository.save(user);

        return response;
    }

    public void changePassword(PasswordChangeRequest request) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.username());

        if (!passwordEncoder.matches(request.oldPassword(), userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        Optional<User> optionalUser = repository.findByUsername(request.username());
        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("Username not found");
        }

        User user = optionalUser.get();
        if (!user.isEnabled()) {
            user.setEnabled(true);
        }
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        repository.save(user);
    }

    private String generateUsername(String firstname, String lastname) {
        String username = lastname + firstname.charAt(0);
        String cleanUsername = username;
        boolean replicated = false;

        int counter = 1;

        do {
            if (replicated) {
                username = cleanUsername + counter;
            }
            counter++;
            replicated = true;
        } while (repository.existsByUsername(username));

        return username;
    }
}
