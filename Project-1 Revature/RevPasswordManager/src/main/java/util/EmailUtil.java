package util;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailUtil {

    private static final String FROM_EMAIL = "soumya20901@gmail.com"; // your Gmail
    private static final String APP_PASSWORD = "blnv dsxu hltl jxik"; // Gmail app password

    public static void sendEmail(String to, String subject, String body) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // TLS
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
            }
        });

        session.setDebug(false); // prints detailed SMTP logs

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            System.out.println("Verification code sent to " + to);
        } catch (MessagingException e) {
            System.out.println("Failed to send email!");
            e.printStackTrace();
        }
    }

    // Quick test
    public static void main(String[] args) {
        sendEmail("soumya20901@example.com", "Test Email", "Hello, this is a test email.");
    }
}
