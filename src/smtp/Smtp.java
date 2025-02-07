/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smtp;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;

/**
 *
 * @author Adrian
 */
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import security.Security;

public class Smtp {
    static String EMAIL;
    static String PASSWORD;
    static Properties props;
    
    public static void sendEmail(String receiver, String subject, 
            String text) throws Exception {
        setSmtpData();
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL, PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL));
            message.setRecipients(Message.RecipientType.TO, 
                    InternetAddress.parse(receiver));
            message.setSubject(subject);
            message.setText(text);

            Transport.send(message);
            System.out.println("Email sent successfully to " + receiver);
        } catch (MessagingException e) {
            System.err.println("Error sending email: " + e.getMessage());
            throw e;
        }
    }
    
    private static void setSmtpData() {
        setFilePropData();
        props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
    }

    private static void setFilePropData() {
        EMAIL = Security.descifrarTexto("Codorniz", "EMAIL");
        PASSWORD = Security.descifrarTexto("Codorniz", "PASSWORD");
    }
}
