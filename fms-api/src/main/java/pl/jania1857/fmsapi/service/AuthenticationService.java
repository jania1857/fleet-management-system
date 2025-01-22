package pl.jania1857.fmsapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.jania1857.fmsapi.dto.AuthenticationRequest;
import pl.jania1857.fmsapi.dto.AuthenticationResponse;
import pl.jania1857.fmsapi.dto.RegistrationRequest;
import pl.jania1857.fmsapi.dto.RegistrationResponse;
import pl.jania1857.fmsapi.model.User;
import pl.jania1857.fmsapi.repository.UserRepository;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public RegistrationResponse register(RegistrationRequest request) {

        String password = generateSecurePassword();
        String username = generateUsername(request.firstname(), request.lastname());

        User user = new User();

        user.setFirstname(request.firstname());
        user.setLastname(request.lastname());
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        userRepository.save(user);

        String token = jwtService.generateToken(user);

        return new RegistrationResponse(token, username, password);
    }

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

        return new AuthenticationResponse(token);

    }

    private String generateUsername(String firstname, String lastname) {
        String baseUsername = (firstname.length() > 3 ? firstname.substring(0, 3) : firstname.toLowerCase()) +
                              (lastname.length() > 3 ? lastname.substring(0, 3) : lastname.toLowerCase());

        String uniqueUsername = baseUsername;
        int counter = 1;
        while (userRepository.existsByUsername(uniqueUsername)) {
            uniqueUsername = baseUsername + counter;
            counter++;
        }

        return uniqueUsername;
    }
    private String generateSecurePassword() {
        String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String specialCharacters = "!@#$%^&*()-_=+[]{}|;:,.<>?";
        String allcharacters = upperCaseLetters + lowerCaseLetters + digits + specialCharacters;

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        password.append(upperCaseLetters.charAt(random.nextInt(upperCaseLetters.length())));
        password.append(lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length())));
        password.append(digits.charAt(random.nextInt(digits.length())));
        password.append(specialCharacters.charAt(random.nextInt(specialCharacters.length())));

        for (int i = 4; i < 8; i++) {
            password.append(allcharacters.charAt(random.nextInt(allcharacters.length())));
        }

        return shuffleString(password.toString());
    }
    private String shuffleString(String input) {
        SecureRandom random = new SecureRandom();
        char[] array = input.toCharArray();
        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            char temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
        return new String(array);
    }
}
