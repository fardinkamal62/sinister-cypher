package com.SinisterCypher.authentication;

import com.SinisterCypher.storage.EncryptionService;
import com.SinisterCypher.storage.PasswordStorage;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class Authentication {
    private List<User> users;
    private PasswordStorage passwordStorage;
    private EncryptionService encryptionService;

    public Authentication(PasswordStorage passwordStorage, EncryptionService encryptionService) {
        this.users = new ArrayList<>();
        this.passwordStorage = passwordStorage;
        this.encryptionService = encryptionService;
    }

    public void registerUser(String username, String password) {
        String salt = generateSalt();
        String hashedPassword = hashPassword(password, salt);
        User user = new User(username, salt, hashedPassword);
        users.add(user);
        System.out.println("User " + username + " registered successfully.");
    }

    public boolean loginUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                String hashedPassword = hashPassword(password, user.getSalt());
                if (hashedPassword.equals(user.getHashedPassword())) {
                    System.out.println("Authentication successful.");
                    return true;
                } else {
                    System.out.println("Authentication failed.");
                    return false;
                }
            }
        }
        System.out.println("User not found.");
        return false;
    }

    private String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    private String hashPassword(String password, String salt) {
        // Simple hashing example using SHA-256
        String saltedPassword = password + salt;
        return encryptionService.encrypt(saltedPassword); // Encrypt the salted password
    }
}
