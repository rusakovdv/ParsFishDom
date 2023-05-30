package ParsFish;


import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;
public class EmailAlert {


    public static void Email(String str) {
       final String username = "";
       final String password = "";
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.host", "smtp-relay.sendinblue.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.starttls.enable", true);
        prop.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(prop,
                new jakarta.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("alexados566@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse("lifmen007@gmail.com")
            );
            message.setRecipients(Message.RecipientType.CC,
                    InternetAddress.parse(""));
            message.setSubject("Обнарурежены изменения в доменах");
            message.setText(str);

            if(str.isEmpty()) {
                System.out.println("Изменений не обнаружено ");
            } else {
                Transport.send(message);
            }



            System.out.println("Done");

        } catch (NullPointerException e) {
            System.out.println("Изменений не обнаружено ");
        }
        catch (MessagingException e) {
            System.out.println("Сообщение не было отправлено на почту");
        }

    }

}

