package pl.jania1857.fms.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.jania1857.fms.security.JwtService;
import pl.jania1857.fms.utils.PasswordGenerator;

import static pl.jania1857.fms.user.Role.DRIVER;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordGenerator passwordGenerator;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public RegisterResponse register(RegisterRequest request) {

        final String generatedPassword = passwordGenerator.generateOneTimePassword();

        User user = User.builder()
                .firstname(request.firstname())
                .lastname(request.lastname())
                .username(request.username() == null || request.username().isEmpty() ? generateUsername(request.firstname(), request.lastname()) : request.username())
                .password(passwordEncoder.encode(generatedPassword))
                .role(request.role() == null ? DRIVER : request.role())
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
                jwtToken,
                user.isWasLoggedIn(),
                user.isChangedPassword()
        );

        user.setWasLoggedIn(true);
        repository.save(user);

        return response;
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

    public String changePassword(PasswordChangeRequest request, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();

        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Old password does not match");

            user.setPassword(passwordEncoder.encode(request.newPassword()));
            user.setChangedPassword(true);
            repository.save(user);
        }
    }
}
