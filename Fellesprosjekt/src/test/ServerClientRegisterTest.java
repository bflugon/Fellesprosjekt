package test;

import main.Register;
import model.Appointment;
import model.Group;
import model.MeetingRoom;
import model.Person;
import net.Client;
import util.GeneralUtil;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: FF63
 * Date: 3/10/14
 * Time: 12:21 AM
 * To change this template use File | Settings | File Templates.
 */
public class ServerClientRegisterTest {

    public static void main(String[] args){
        Register reg = new Register(new Client());


        ArrayList<Appointment> appointments = reg.getAppointments();
        ArrayList<Person> persons = reg.getPersons();
        ArrayList<Group> groups = reg.getGroups();
        ArrayList<MeetingRoom> meetingRooms = reg.getRooms();
        reg.getAllMembersOfGroup();

        GeneralUtil.sendEmail("markuslund92@gmail.com",appointments.get(0));

        //System.out.println(reg.getPersonByUsername("herp").toString());
        //System.out.println(reg.getGroup(1).getGroupName());
        //reg.addRoom("TestRom",5);
        //reg.addGroup("TestGruppe");
        //reg.addPersonToGroup(reg.getGroup(1), reg.getPersonByUsername("herp"));
        //reg.addAppointment("Test","2014-03-06 13:00:00", "2014-03-06 14:00:00", "AddTest", 1, "herp", reg.getRoom(1));

        for(Person p : persons){
            System.out.println(p.toString());
        }

        for(Appointment a: appointments){
            System.out.println(a.toString());
        }

        for (Group g : groups){
            System.out.println(g.toString());
        }

        for (MeetingRoom mr : meetingRooms){
            System.out.println(mr.toString());
        }

    }
}
