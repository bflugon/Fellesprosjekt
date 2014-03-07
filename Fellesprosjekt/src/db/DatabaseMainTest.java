package db;

/**
 * Created with IntelliJ IDEA.
 * User: FF63
 * Date: 3/6/14
 * Time: 2:09 PM
 * To change this template use File | Settings | File Templates.
 */

import main.Register;
import model.Appointment;
import model.MeetingRoom;
import model.Person;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * DO NOT TOUCH!
 */
public class DatabaseMainTest {

    public static void main(String[] args){
        DatabaseMainTest main = new DatabaseMainTest();
        main.testDB();
    }

    public void testDB(){

        System.out.println("Creating connection");
        DatabaseHandler db = new DatabaseHandler();
        Register reg = new Register(db);

        try{

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

            System.out.println("Getting all appointments and prints them");
            ArrayList<Appointment> appointments = db.getAllAppointments();

            for(Appointment a : appointments){
                System.out.println(a.toString());
            }

            System.out.println("Getting all rooms and prints them");
            ArrayList<MeetingRoom> meetingRooms = db.getAllRooms();

            for (MeetingRoom mr : meetingRooms ){
                System.out.println(mr.toString());
            }

            /*
            System.out.println("Adding appointment: (\"name\",\"2014-03-06 13:00:00\",\"2014-03-06 13:00:00\", \"Test\", 3, \"Bob\", mr);");
            db.addAppointment("name","2014-03-06 13:00:00","2014-03-06 13:00:00", "Test", 3, "Bob", meetingRooms.get(0));
            */

            /*
            System.out.println("Edit first appointment");
            db.editAppointment(1,"Edit","2014-03-06 13:00:00","2014-03-06 13:00:00","EditTest",2,meetingRooms.get(0));
             */

            /*
            System.out.println("Deleting appointment");
            db.deleteAppointment(5);
            */

            /*
            System.out.println("Adding new room");
            db.addRoom("RomTest",2);
            */


        } catch(SQLException e){
            e.printStackTrace();
        }

        System.out.println("Closing db connection");
        db.close();
    }

}
