package src.com.SinisterCypher.storage;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptionService {
    public EncryptionService() {}

    private String hashPasswordString(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(str.getBytes());
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error hashing password");
            return null;
        }
    }

    public String encrypt(String str) {
        return hashPasswordString(str);
    }
}
