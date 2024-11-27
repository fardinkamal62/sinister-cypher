package src.com.SinisterCypher;

import java.security.SecureRandom;
import java.util.Scanner;

import src.com.SinisterCypher.storage.PasswordStorage;
import src.com.SinisterCypher.authentication.User;

public class PasswordGenerator {
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_=+<>?";
    private static final String ALL_CHARACTERS = UPPERCASE + LOWERCASE + DIGITS + SPECIAL_CHARACTERS;

    private final SecureRandom random = new SecureRandom();
    private int length;

    private static User user;
    private static Scanner scanner;

    public PasswordGenerator(int length, User user, Scanner scanner) {
        this.length = length;
        PasswordGenerator.user = user;
        PasswordGenerator.scanner = scanner;

        System.out.println("Generating password of length: " + length);
        String password = generatePassword();
        System.out.println("Generated Password: " + password);
        store(password);
    }

    private String generatePassword() {
        StringBuilder password = new StringBuilder(length);
        
        password.append(getRandomCharacter(UPPERCASE));
        password.append(getRandomCharacter(LOWERCASE));
        password.append(getRandomCharacter(DIGITS));
        password.append(getRandomCharacter(SPECIAL_CHARACTERS));

        for (int i = 4; i < length; i++) {
            password.append(getRandomCharacter(ALL_CHARACTERS));
        }

        return shuffleString(password.toString());
    }

    private char getRandomCharacter(String characterSet) {
        return characterSet.charAt(random.nextInt(characterSet.length()));
    }

    private String shuffleString(String input) {
        char[] characters = input.toCharArray();
        for (int i = characters.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            // Swap characters
            char temp = characters[i];
            characters[i] = characters[j];
            characters[j] = temp;
        }
        return new String(characters);
    }

    public static void store(String password) {
        scanner.nextLine();
        System.out.println("Do you want to store this password? (Y/N)");

        String response = scanner.nextLine();

        if (response.equalsIgnoreCase("Y")) {
            System.out.print("Enter Key: ");
            String key = scanner.nextLine();
            PasswordStorage.storePassword(key, password, user.getUsername());
        } else {
            System.out.println("Password not stored.");
        }
    }
}
