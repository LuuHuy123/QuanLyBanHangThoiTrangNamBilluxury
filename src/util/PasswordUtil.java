package util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtil {

    private PasswordUtil() {

    }

    public static String hashSHA256(String password) {

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Mật khẩu không được để trống.");
        }

        try {

            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            byte[] hashBytes =
                    digest.digest(
                            password.getBytes(StandardCharsets.UTF_8));

            return bytesToHex(hashBytes);

        } catch (NoSuchAlgorithmException e) {

            throw new RuntimeException(
                    "Không hỗ trợ thuật toán SHA-256.", e);
        }
    }

    public static boolean verifyPassword(
            String rawPassword,
            String hashedPassword) {

        if (rawPassword == null || hashedPassword == null) {
            return false;
        }

        return hashSHA256(rawPassword)
                .equals(hashedPassword);
    }

    private static String bytesToHex(byte[] bytes) {

        StringBuilder sb = new StringBuilder();

        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }
}