package main;

import db.DatabaseHandler;
import model.Appointment;
import model.Person;
import util.GeneralUtil;

import java.security.Timestamp;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created with IntelliJ IDEA.
 * User: FF63
 * Date: 2/27/14
 * Time: 4:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    /**
     * Entry point
     * @param args
     */
    public static void main(String[] args){
       Main main = new Main();
       main.testDB();

    }

    public void testDB(){

        System.out.println("Creating connection");
        DatabaseHandler db = new DatabaseHandler();

        try{
            /*
            System.out.println("Trying to authenticate with username 'Bob' and password 'hei'");
            if(db.authenticate("Bob", "hei")){
                System.out.println("Authentication successful");
            } else{
                System.out.println("Authentication failed");
            }

            System.out.println("Fetching all users and prints names");
            ArrayList<Person> persons = db.getAllPersons();

            for (Person p : persons){
                System.out.println(p.getName());
            }
            */


            System.out.println("Getting all appointments and prints them");
            ArrayList<Appointment> appointments = db.getAllAppointments();

            for(Appointment a : appointments){
                System.out.println(a.toString());
            }

            /*
            System.out.println("Adding appointment");
            db.addAppointment("Test123", "2014-05-09 12:00:00", "2014-05-09 13:00:00", "Testing blabla", 2,"2014-05-03 12:00:00");
            */

        } catch(SQLException e){
            e.printStackTrace();
        }

        System.out.println("Closing db connection");
        db.close();
    }

}
