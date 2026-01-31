package service;

import dao.PasswordDAO;
import dao.SecurityQuestionDAO;
import dao.UserDAO;
import dao.VerificationCodeDAO;
import model.VerificationCode;
import model.Password;
import model.SecurityQuestion;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.EmailUtil;
import util.EncryptionUtil;

import java.sql.SQLException;
import java.util.List;
import java.util.Random;

public class UserService {

    private static final Logger logger = LogManager.getLogger(UserService.class);

    private final UserDAO userDAO = new UserDAO();
    private final PasswordDAO passwordDAO = new PasswordDAO();
    private final SecurityQuestionDAO securityDAO = new SecurityQuestionDAO();
    private final VerificationCodeDAO codeDAO = new VerificationCodeDAO();

    // ================= AUTH =================

    public void register(String name, String email, String masterPassword,
                         String question, String answer) throws Exception {

        logger.info("Registering user: {}", email);

        String encryptedPassword = EncryptionUtil.encrypt(masterPassword);
        int userId = userDAO.createUser(name, email, encryptedPassword);

        String answerHash = EncryptionUtil.encrypt(answer);
        securityDAO.addSecurityQuestion(userId, question, answerHash);

        logger.info("User registered successfully with userId={}", userId);
    }

    public boolean login(String email, String masterPassword) throws Exception {
        String encrypted = EncryptionUtil.encrypt(masterPassword);
        boolean success = userDAO.login(email, encrypted);

        if (!success) {
            logger.warn("Invalid login attempt for {}", email);
        }

        return success;
    }

    public int getUserId(String email) throws SQLException {
        int userId = userDAO.getUserIdByEmail(email);
        if (userId == -1) throw new RuntimeException("User not found");
        return userId;
    }

    // ================= PROFILE =================

    public void updateProfile(int userId, String masterPassword,
                              String newName, String newEmail) throws Exception {

        User user = getUserById(userId);
        String encrypted = EncryptionUtil.encrypt(masterPassword);

        if (!user.getMasterPasswordHash().equals(encrypted)) {
            throw new RuntimeException("Master password incorrect!");
        }

        userDAO.updateProfile(userId, newName, newEmail);
        logger.info("Profile updated for userId={}", userId);
    }

    // ================= PASSWORD VAULT =================

    public void addAccount(int userId, String accountName, String username, String password) throws Exception {
        String encrypted = EncryptionUtil.encrypt(password);
        passwordDAO.addPassword(userId, accountName, username, encrypted);
        logger.info("Password added for userId={} account={}", userId, accountName);
    }
    public void listPasswords(int userId) throws Exception {

        List<Password> list = passwordDAO.getPasswords(userId);

        if (list.isEmpty()) {
            System.out.println("No accounts found.");
            return;
        }

        System.out.println("\n================ USER VAULT ================");
        System.out.printf("%-5s %-20s %-20s%n", "ID", "Account", "Username");
        System.out.println("------------------------------------------------");

        for (Password p : list) {
            System.out.printf(
                    "%-5d %-20s %-20s%n",
                    p.getPasswordId(),
                    p.getAccountName(),
                    p.getUsername()
            );
        }

        System.out.println("================================================\n");
    }




    public String viewPassword(int userId, int passwordId, String masterPassword) throws Exception {

        User user = getUserById(userId);
        String encrypted = EncryptionUtil.encrypt(masterPassword);

        if (!user.getMasterPasswordHash().equals(encrypted)) {
            throw new RuntimeException("Master password incorrect!");
        }

        Password p = passwordDAO.getPassword(passwordId);

        if (p == null) {
            throw new RuntimeException("Account not found");
        }

        return "\n========== Account Details ==========\n" +
                "Account   : " + p.getAccountName() + "\n" +
                "Username  : " + p.getUsername() + "\n" +
                "Password  : " + EncryptionUtil.decrypt(p.getEncryptedPassword()) + "\n" +
                "====================================\n";
    }


    public void updatePassword(int passwordId, String newPassword) throws Exception {
        String encrypted = EncryptionUtil.encrypt(newPassword);
        passwordDAO.updatePassword(passwordId, encrypted);
        logger.info("Password updated for passwordId={}", passwordId);
    }

    public void deletePassword(int passwordId) throws Exception {
        passwordDAO.deletePassword(passwordId);
        logger.info("Password deleted for passwordId={}", passwordId);
    }

    // ================= SECURITY =================

    public void changeSecurityQuestion(int userId, String newQuestion, String newAnswer) throws Exception {
        String answerHash = EncryptionUtil.encrypt(newAnswer);
        securityDAO.updateSecurityQuestion(userId, newQuestion, answerHash);
        logger.info("Security question updated for userId={}", userId);
    }

    // ================= VERIFICATION CODE =================

    public void generateVerificationCode(int userId) throws Exception {
        User user = getUserById(userId);
        String code = String.valueOf(new Random().nextInt(900000) + 100000);
        codeDAO.saveCode(userId, code);

        EmailUtil.sendEmail(user.getEmail(),
                "Password Manager Verification Code",
                "Your verification code is: " + code);

        logger.info("Verification code sent to {}", user.getEmail());
    }

    public boolean validateCode(int userId, String code) throws Exception {
        VerificationCode vc = codeDAO.getValidCode(userId, code);

        if (vc != null) {
            codeDAO.markUsed(vc.getCodeId()); // mark as 1 (true)
            return true;
        }

        return false; // invalid / expired / already used
    }


    public void changeMasterPassword(int userId, String oldPassword, String newPassword) throws Exception {
        User user = getUserById(userId);
        String oldEncrypted = EncryptionUtil.encrypt(oldPassword);

        if (!user.getMasterPasswordHash().equals(oldEncrypted)) {
            throw new RuntimeException("Old master password incorrect!");
        }

        String newEncrypted = EncryptionUtil.encrypt(newPassword);
        userDAO.updateMasterPassword(userId, newEncrypted);

        logger.info("Master password updated for userId={}", userId);
    }
    // Used when user forgot password (no old password required)
    public void resetMasterPassword(int userId, String newPassword) throws Exception {

        String newEncrypted = EncryptionUtil.encrypt(newPassword);
        userDAO.updateMasterPassword(userId, newEncrypted);

        logger.info("Master password reset for userId={}", userId);
    }


    // ================= UTIL =================

    private User getUserById(int userId) throws SQLException {
        User user = userDAO.getUserById(userId);
        if (user == null) throw new RuntimeException("User not found!");
        return user;
    }

    public void forgotPassword(String email, String answer, String newPassword) throws Exception {

        int userId = userDAO.getUserIdByEmail(email);

        if (userId == -1)
            throw new RuntimeException("Email not found");

        SecurityQuestion sq = securityDAO.getQuestionByUserId(userId);

        String encryptedAnswer = EncryptionUtil.encrypt(answer);

        if (!sq.getAnswerHash().equals(encryptedAnswer))
            throw new RuntimeException("Wrong security answer");

        resetMasterPassword(userId, newPassword);
    }
    public String getSecurityQuestion(int userId) throws Exception {
        return securityDAO.getQuestionByUserId(userId).getQuestion();
    }

    public boolean verifySecurityAnswer(int userId, String answer) throws Exception {
        SecurityQuestion sq = securityDAO.getQuestionByUserId(userId);
        String hashed = EncryptionUtil.encrypt(answer);
        return sq.getAnswerHash().equals(hashed);
    }



}
