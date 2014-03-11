package net;

import model.Alarm;
import util.GeneralUtil;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: FF63
 * Date: 3/10/14
 * Time: 11:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class MailClient {
    private final ArrayList<String> mailInfo;
    private final String username;
    private final String password;


    public MailClient(String recipient, Alarm alarm) {
        mailInfo = GeneralUtil.readFile("smtp.txt");
        username = mailInfo.get(0);
        password = mailInfo.get(1);

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username,password);   //To change body of overridden methods use File | Settings | File Templates.
            }
        };

        Session session = Session.getInstance(properties, authenticator);
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("from@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject("CalendarAlarm");
            message.setText("This is an automated message sent to alert you that you have a meeting at " + alarm.getAlarmTime() + ".\n\n Please do not respond to this message.");
            Transport.send(message);
        } catch (MessagingException e){
            e.printStackTrace();
        }
    }

}
