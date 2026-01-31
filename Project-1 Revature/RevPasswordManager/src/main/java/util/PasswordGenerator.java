package util;
import java.security.SecureRandom;

public class PasswordGenerator {
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SYMBOLS = "!@#$%^&*()-_=+[]{}<>?";

    public static String generate(int length, boolean useLower, boolean useUpper,
                                  boolean useDigits, boolean useSymbols) {

        StringBuilder allChars = new StringBuilder();
        StringBuilder result = new StringBuilder();

        SecureRandom random = new SecureRandom();

        if (useLower) {
            allChars.append(LOWERCASE);
            result.append(LOWERCASE.charAt(random.nextInt(LOWERCASE.length())));
        }

        if (useUpper) {
            allChars.append(UPPERCASE);
            result.append(UPPERCASE.charAt(random.nextInt(UPPERCASE.length())));
        }

        if (useDigits) {
            allChars.append(DIGITS);
            result.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        }

        if (useSymbols) {
            allChars.append(SYMBOLS);
            result.append(SYMBOLS.charAt(random.nextInt(SYMBOLS.length())));
        }

        if (allChars.length() == 0) {
            throw new IllegalArgumentException("No character sets selected!");
        }

        while (result.length() < length) {
            result.append(allChars.charAt(random.nextInt(allChars.length())));
        }

        // Shuffle characters
        for (int i = 0; i < result.length(); i++) {
            int j = random.nextInt(result.length());
            char temp = result.charAt(i);
            result.setCharAt(i, result.charAt(j));
            result.setCharAt(j, temp);
        }

        return result.toString();
    }

    public static String generate(int length) {
        return generate(length, true, true, true, true);
    }

}