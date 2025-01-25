package pl.jania1857.fmsapi.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.jania1857.fmsapi.model.User;
import pl.jania1857.fmsapi.repository.UserRepository;
import pl.jania1857.fmsapi.utils.Role;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner init() {
        return args -> {
            if (userRepository.count() != 0) {
                logger.info("Initialization of users not required. Skipping...");
            } else {
                User driver = User.builder()
                        .username("driver")
                        .password(passwordEncoder.encode("driver"))
                        .firstname("driver")
                        .lastname("driver")
                        .role(Role.DRIVER)
                        .build();
                User manager = User.builder()
                        .username("manager")
                        .password(passwordEncoder.encode("manager"))
                        .firstname("manager")
                        .lastname("manager")
                        .role(Role.MANAGER)
                        .build();
                User admin = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .firstname("admin")
                        .lastname("admin")
                        .role(Role.ADMIN)
                        .build();

                userRepository.save(driver);
                userRepository.save(manager);
                userRepository.save(admin);

                if (userRepository.count() != 3) {
                    logger.warn("Initialization of default user account failed");
                } else {
                    logger.info("Initialization of default user account successful");
                }
            }

        };
    }
}
