package pl.jania1857.fmsapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.jania1857.fmsapi.dto.all.ChangePasswordRequest;
import pl.jania1857.fmsapi.dto.CreateUserRequest;
import pl.jania1857.fmsapi.dto.CreateUserResponse;
import pl.jania1857.fmsapi.model.User;
import pl.jania1857.fmsapi.repository.UserRepository;

import java.security.Principal;
import java.security.SecureRandom;
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CreateUserResponse createUser(CreateUserRequest request) {

        String password = generateSecurePassword();
        String username = generateUsername(request.firstname(), request.lastname());

        User user = new User();

        user.setFirstname(request.firstname());
        user.setLastname(request.lastname());
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(request.role());

        userRepository.save(user);

        return new CreateUserResponse(username, password);
    }

    public void changePassword(ChangePasswordRequest request, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
            throw new BadCredentialsException("Old password does not match");
        }

        if (!request.newPassword().equals(request.confirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }





    private String generateUsername(String firstname, String lastname) {
        String baseUsername = (firstname.length() > 3 ? firstname.substring(0, 3).toLowerCase() : firstname.toLowerCase()) +
                (lastname.length() > 3 ? lastname.substring(0, 3).toLowerCase() : lastname.toLowerCase());

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

