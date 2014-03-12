package test;

import db.DatabaseHandler;
import main.Register;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: FF63
 * Date: 3/6/14
 * Time: 9:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class RegisterTest {
    private Register reg;

    public RegisterTest(){
        //DatabaseHandler mHandler = new DatabaseHandler();
    }

    @Test
    public void testGetAppointment() throws Exception {
        assert(reg.getAppointment(1).getAppointmentID() == 1);
        assert(reg.getAppointment(1).getAppointmentName().equals("Herp"));
    }

    @Test
    public void testGetPersons() throws Exception {

    }

    @Test
    public void testGetPersonByUsername() throws Exception {

    }

    @Test
    public void testGetRooms() throws Exception {

    }

    @Test
    public void testGetRoom() throws Exception {

    }

    @Test
    public void testGetMembersOfGroup() throws Exception {

    }
}
