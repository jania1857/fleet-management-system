package pl.jania1857.fms.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class PasswordGenerator {
    private static final String UPPERCASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String ALL_CHARACTERS = UPPERCASE_LETTERS + LOWERCASE_LETTERS + DIGITS;

    private static final SecureRandom random = new SecureRandom();

    public String generateOneTimePassword() {
        StringBuilder password = new StringBuilder(8);
        String characterPool = ALL_CHARACTERS;

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(characterPool.length());
            password.append(characterPool.charAt(index));
        }

        return password.toString();
    }
}
