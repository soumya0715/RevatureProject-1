package org.main;

import org.junit.jupiter.api.Test;
import service.UserService;
import util.PasswordGenerator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;


public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

    // ---------- UI HELPERS ----------
    public static void printLine() {
        System.out.println("==================================================");
    }

    public static void printHeader(String title) {
        printLine();
        System.out.println("   " + title);
        printLine();
    }
    // -------------------------------

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        UserService userService = new UserService();

        logger.info("Password Manager application started");

        while (true) {
            try {

                // ---------- MAIN MENU ----------
                printHeader("SECURE PASSWORD MANAGER");

                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Forgot Password");
                System.out.println("4. Exit");
                System.out.print("\nEnter Choice: ");

                int choice = Integer.parseInt(sc.nextLine());
                logger.info("Main menu choice selected: {}", choice);

                // ---------- REGISTER ----------
                if (choice == 1) {

                    System.out.print("Enter Name            : ");
                    String name = sc.nextLine();

                    System.out.print("Enter Email           : ");
                    String email = sc.nextLine();

                    System.out.print("Enter Master Password : ");
                    String masterPassword = sc.nextLine();

                    System.out.print("Security Question     : ");
                    String question = sc.nextLine();

                    System.out.print("Answer                : ");
                    String answer = sc.nextLine();

                    userService.register(name, email, masterPassword, question, answer);

                    logger.info("User registered successfully. Email={}", email);
                    System.out.println("\n✅ Registration Successful!");

                }

                // ---------- LOGIN ----------
                else if (choice == 2) {

                    System.out.print("Enter Email  : ");
                    String email = sc.nextLine();

                    System.out.print("Enter Master Password : ");
                    String masterPassword = sc.nextLine();

                    if (userService.login(email, masterPassword)) {

                        int userId = userService.getUserId(email);
                        logger.info("Login successful. userId={}", userId);

                        System.out.println("\n✅ Login Successful!");
                        System.out.println("\n");

                        boolean loggedIn = true;

                        // ---------- USER MENU ----------
                        while (loggedIn) {

                            printHeader("USER DASHBOARD");

                            System.out.println("1. Update Profile");
                            System.out.println("2. Add Account");
                            System.out.println("3. List Accounts");
                            System.out.println("4. View Account Details");
                            System.out.println("5. Update Account Password");
                            System.out.println("6. Delete Account");
                            System.out.println("7. Change Security Question");
                            System.out.println("8. Change Master Password");
                            System.out.println("9. Logout");
                            System.out.print("\nSelect Option: ");

                            int opt = Integer.parseInt(sc.nextLine());
                            logger.info("User menu option selected: {}", opt);

                            switch (opt) {

                                // UPDATE PROFILE
                                case 1:
                                    System.out.print("Re-enter Master Password : ");
                                    String mp = sc.nextLine();

                                    System.out.print("New Name                : ");
                                    String newName = sc.nextLine();

                                    System.out.print("New Email               : ");
                                    String newEmail = sc.nextLine();

                                    userService.updateProfile(userId, mp, newName, newEmail);
                                    System.out.println("\n✅ Profile Updated!");
                                    break;

                                // ADD ACCOUNT
                                case 2:
                                    System.out.print("Account Name : ");
                                    String accName = sc.nextLine();

                                    System.out.print("Username     : ");
                                    String uname = sc.nextLine();

                                    System.out.print("Generate Password? (y/n): ");
                                    boolean gen = sc.nextLine().equalsIgnoreCase("y");

                                    String pwd;

                                    if (gen) {

                                        // Always generate 12 character strong password
                                        int len = 12;

                                        System.out.print("Lowercase? (y/n): ");
                                        boolean l = sc.nextLine().equalsIgnoreCase("y");

                                        System.out.print("Uppercase? (y/n): ");
                                        boolean u = sc.nextLine().equalsIgnoreCase("y");

                                        System.out.print("Digits? (y/n): ");
                                        boolean d = sc.nextLine().equalsIgnoreCase("y");

                                        System.out.print("Symbols? (y/n): ");
                                        boolean s = sc.nextLine().equalsIgnoreCase("y");

                                        pwd = PasswordGenerator.generate(len, l, u, d, s);
                                        System.out.println("\n✅ Password Generated!");
                                    }
                                    else {
                                        System.out.print("Enter Password: ");
                                        pwd = sc.nextLine();
                                    }

                                    userService.addAccount(userId, accName, uname, pwd);
                                    System.out.println("\n✅ Account Added!");
                                    break;

                                // LIST ACCOUNTS
                                case 3:
                                    userService.listPasswords(userId);
                                    break;

                                // VIEW ACCOUNT
                                case 4:
                                    userService.listPasswords(userId);

                                    System.out.print("Account ID : ");
                                    int accId = Integer.parseInt(sc.nextLine());

                                    System.out.print("Master Password: ");
                                    String mp2 = sc.nextLine();

                                    String details =
                                            userService.viewPassword(userId, accId, mp2);

                                    System.out.println("\n" + details);
                                    break;

                                // UPDATE PASSWORD
                                case 5:
                                    userService.listPasswords(userId);

                                    System.out.print("Account ID : ");
                                    int pid = Integer.parseInt(sc.nextLine());

                                    System.out.print("New Password: ");
                                    String newPwd = sc.nextLine();

                                    userService.updatePassword(pid, newPwd);
                                    System.out.println("\n✅ Password Updated!");
                                    break;

                                // DELETE ACCOUNT
                                case 6:
                                    userService.listPasswords(userId);

                                    System.out.print("Account ID : ");
                                    int del = Integer.parseInt(sc.nextLine());

                                    userService.deletePassword(del);
                                    System.out.println("\n✅ Account Deleted!");
                                    break;

                                // CHANGE SECURITY QUESTION
                                case 7:
                                    System.out.print("New Question: ");
                                    String nq = sc.nextLine();

                                    System.out.print("New Answer  : ");
                                    String na = sc.nextLine();

                                    userService.changeSecurityQuestion(userId, nq, na);
                                    System.out.println("\n✅ Security Question Updated!");
                                    break;

                                // CHANGE MASTER PASSWORD
                                case 8:
                                    userService.generateVerificationCode(userId);
                                    System.out.println("\n✅ Verification Code Sent!");

                                    System.out.print("Enter Code: ");
                                    String code = sc.nextLine();

                                    if (userService.validateCode(userId, code)) {

                                        System.out.print("Old Password: ");
                                        String old = sc.nextLine();

                                        System.out.print("New Password: ");
                                        String newM = sc.nextLine();

                                        userService.changeMasterPassword(userId, old, newM);
                                        System.out.println("\n✅ Master Password Changed!");
                                    } else {
                                        System.out.println("\n❌ Invalid / Expired Code!");
                                    }
                                    break;

                                // LOGOUT
                                case 9:
                                    loggedIn = false;
                                    System.out.println("\n✅ Logged Out Successfully!");
                                    break;

                                default:
                                    System.out.println("\n❌ Invalid Option!");
                            }
                        }

                    } else {
                        System.out.println("\n❌ Invalid Email or Password!");
                    }
                }

                // ---------- FORGOT PASSWORD ----------
                // ---------- FORGOT PASSWORD ----------
                else if (choice == 3) {

                    System.out.print("Enter Registered Email : ");
                    String email = sc.nextLine();

                    int userId = userService.getUserId(email);

                    if (userId == -1) {
                        System.out.println("\n❌ Email not found!");
                    }
                    else {

                        // 1️⃣ Fetch and show security question
                        String question = userService.getSecurityQuestion(userId);
                        System.out.println("\nSecurity Question: " + question);

                        // 2️⃣ Verify answer
                        System.out.print("Enter Answer: ");
                        String answer = sc.nextLine();

                        if (!userService.verifySecurityAnswer(userId, answer)) {
                            System.out.println("\n❌ Incorrect Answer!");
                        }
                        else {

                            // 3️⃣ Ask new password
                            System.out.print("Enter New Master Password: ");
                            String newPass = sc.nextLine();

                            userService.resetMasterPassword(userId, newPass);

                            System.out.println("\n✅ Master Password Reset Successfully!");
                        }
                    }
                }


                // EXIT
                else if (choice == 4) {
                    System.out.println("\nThank you for using Password Manager!");
                    break;
                }

            } catch (Exception e) {
                logger.error("Unhandled exception", e);
                System.out.println("\n❌ Something went wrong. Try again!");
            }
        }

        sc.close();
    }
}
