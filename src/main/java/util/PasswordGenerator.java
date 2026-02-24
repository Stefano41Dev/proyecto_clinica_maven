package util;

import org.mindrot.jbcrypt.BCrypt;

import java.security.SecureRandom;

public class PasswordGenerator {
    private static final String CHARACTERS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private static final SecureRandom random = new SecureRandom();

    public static String generarPasswordPlano(int longitud) {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < longitud; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }

        return sb.toString();
    }

    public static String generarHash(String passwordPlano) {
        return BCrypt.hashpw(passwordPlano, BCrypt.gensalt(12));
    }
}
