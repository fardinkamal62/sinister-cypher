package src.com.SinisterCypher.authentication;

import src.com.SinisterCypher.storage.EncryptionService;
import src.com.SinisterCypher.storage.PasswordStorage;

import java.security.SecureRandom;
import java.util.Base64;

public class AuthenticationService {
    public AuthenticationService() {}

    public void registerUser(String username, String password) {
        String salt = generateSalt();
        String hashedPassword = hashPassword(password, username);

        PasswordStorage.storeUser(username, salt, hashedPassword);

        System.out.println("User " + username + " registered successfully.");
    }

    public User loginUser(String username, String password) {
        String hashedPassword = hashPassword(password, username);
        return PasswordStorage.retrieveUser(username, hashedPassword);
    }

    private String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    private String hashPassword(String password, String salt) {
        String saltedPassword = password + salt;
        EncryptionService encryptionService = new EncryptionService();
        return encryptionService.encrypt(saltedPassword);
    }
}
