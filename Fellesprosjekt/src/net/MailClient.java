package net;

import db.DatabaseHandler;
import model.Alarm;
import model.Appointment;
import model.Person;
import util.GeneralUtil;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.SQLException;
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
    private DatabaseHandler db;
    private Person sender;
    private String location;

    /**
     * Constructor
     * @param db
     */
    public MailClient(DatabaseHandler db) {
        this.db = db;
        mailInfo = GeneralUtil.readFile("smtp.txt");
        username = mailInfo.get(0);
        password = mailInfo.get(1);
    }

    /**
     * Sends email.
     * @param recipient
     * @param appointment
     * @return true if successful
     */
    public boolean sendEmail(String recipient, Appointment appointment){

        if (appointment.getRoom().getRoomID() == 1){
            location = appointment.getAlternativeLocation();
        }else{
            location = appointment.getRoom().getRoomName();
        }

        try{
            sender = db.getPersonByUsername(appointment.getOwnerName());
        } catch (SQLException e){
            System.out.println("Error retrieving sender");
        }

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        System.out.println ("Created properties");

        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username,password);   //To change body of overridden methods use File | Settings | File Templates.
            }
        };

        System.out.println ("Created authenticator");

        Session session = Session.getInstance(properties, authenticator);
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("from@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject("CalendarAlarm");
            message.setText("This is an automated message sent to alert you that you have been invited to a meeting by " + sender.getName() + ". The meeting will take place at " +
                    appointment.getAppointmentStart() + " at location " +  location +"" +   ".\n\nPlease do not respond to this message.");
            Transport.send(message);
            System.out.println ("Sent email");
            return true;
        } catch (MessagingException e){
            e.printStackTrace();
            return false;
        }
    }

}
