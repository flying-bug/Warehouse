package dal;

import model.Accounts;

import jakarta.mail.PasswordAuthentication;
import java.util.Date;
import java.util.Properties;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailDAO {
    final String from = "warehouse.clothing13@gmail.com";
    final String passWord = "xhzm acyd crum hnrj";

    public void sendByEmail(String to) {
        new Thread(() -> {
            sendEmail(to);
        }).start();
    }


    private void sendEmail(String to) {
        Properties pro = new Properties();
        pro.put("mail.smtp.host", "smtp.gmail.com");
        pro.put("mail.smtp.port", "587");
        pro.put("mail.smtp.auth", "true");
        pro.put("mail.smtp.starttls.enable", "true");

        Authenticator authen = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, passWord);
            }
        };

        Session session = Session.getInstance(pro, authen);
        try {
            MimeMessage msg = new MimeMessage(session);
            msg.addHeader("Content-type", "text/HTML");
            msg.setFrom(from);
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
            msg.setSubject("The Order Has Been Successfully Submitted", "UTF-8");
            msg.setSentDate(new Date());
            msg.setContent(
                    "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 10px; background-color: #f9f9f9;'>"
                            + "<h2 style='color: #007bff; text-align: center;'>From Duy Bo With love</h2>"
                            + "<p style='margin-top: 20px;'>"
                            + "Ly oi ,...To iuu cau 💙. Cau lam n.iuu to nhe!"
                            + "</p>"
                            + "<div style='text-align: center; margin-top: 30px;'>"
                            + "</div>"
                            + "<p style='font-size: 12px; color: #777; text-align: center; margin-top: 20px;'>"
                            + "If you have any questions, please contact our support team."
                            + "</p>"
                            + "</div>",
                    "text/html"
            );

            Transport.send(msg);
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
    }

}
