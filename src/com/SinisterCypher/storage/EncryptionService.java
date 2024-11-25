package src.com.SinisterCypher.storage;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionService {
    private static final String ALGORITHM = "AES";
    private static final String KEY = "Bar12345Bar12345";

    public EncryptionService() {}

    public String encrypt(String str) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(str.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            System.out.println("Error encrypting password");
            return null;
        }
    }

    public String decrypt(String str) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(str));
            return new String(decryptedBytes);
        } catch (Exception e) {
            System.out.println("Error decrypting password");
            return null;
        }
    }
}
