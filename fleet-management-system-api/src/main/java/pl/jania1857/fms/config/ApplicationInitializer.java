package pl.jania1857.fms.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.jania1857.fms.user.User;
import pl.jania1857.fms.user.UserRepository;

import static pl.jania1857.fms.user.Role.ADMIN;

@RequiredArgsConstructor
@Component
public class ApplicationInitializer implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Value("${admin.username}")
    private String adminUsername;
    @Value("${admin.password}")
    private String adminPassword;


    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByRole(ADMIN).isEmpty()) {
            User admin = User.builder()
                    .firstname("Administrator")
                    .lastname("systemu")
                    .username(adminUsername)
                    .password(passwordEncoder.encode(adminPassword))
                    .role(ADMIN)
                    .enabled(true)
                    .build();
            userRepository.save(admin);

        }
    }
}
