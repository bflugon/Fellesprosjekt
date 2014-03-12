package test;

import db.DatabaseHandler;
import model.Appointment;
import model.MeetingRoom;
import model.Person;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: FF63
 * Date: 3/5/14
 * Time: 7:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class DatabaseHandlerTest {
    private static DatabaseHandler db;

    @BeforeClass
    public static void login() throws Exception{
        //db = new DatabaseHandler();
    }

    @Test
    public void testAuthenticate() throws Exception {
        assert(db.authenticate("Bob","hei"));
        assert(!db.authenticate("WRONG", "WRONG"));
    }

    @Test
    public void testGetAllPersons() throws Exception{
        ArrayList<Person> persons = db.getAllPersons();
        assert(persons != null);
        assert(persons.get(0).getUsername().equals("Bob"));
        assert(persons.get(1).getUsername().equals("Stian"));
    }

    @Test
    public void testGetAllAppointments() throws Exception{
        ArrayList<Appointment> appointments = db.getAllAppointments();
        assert(appointments.get(0).getAppointmentID() == 1);
        assert(appointments.get(2).getAppointmentName().equals("Test123"));
        assert(appointments.get(1).getDescription().equals("Test32"));
        assert(appointments.get(0).getAppointmentStart().equals("2014-03-06 13:00:00"));
    }

    @Test
    public void testGetAllRooms() throws Exception{
        ArrayList<MeetingRoom> meetingRooms = db.getAllRooms();
        assert(meetingRooms.get(0).getRoomName().equals("Rom1"));
        assert(meetingRooms.get(1).getCapacity() == 6);
        assert(meetingRooms.get(0).getRoomID() == 1);
    }


    @AfterClass
    public static void logout() throws Exception{
        db.close();
    }
}
