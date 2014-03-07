package main;

import db.DatabaseHandler;
import model.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created with IntelliJ IDEA.
 * User: FF63
 * Date: 3/5/14
 * Time: 7:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class Register {
    private DatabaseHandler mHandler;
    private ArrayList<Person> persons;
    private ArrayList<Appointment> appointments;
    private ArrayList<MeetingRoom> rooms;
    private ArrayList<Group> groups;
    private TreeMap<Integer, ArrayList<String>> allGroupMembers;
    private TreeMap<Integer, ArrayList<Alarm>> activeAlarms;

    public Register(DatabaseHandler handler){
        this.mHandler = handler;
        try{
            this.persons = handler.getAllPersons();
        }catch (SQLException e){
            this.persons = new ArrayList<Person>();
        }try{
            this.appointments = handler.getAllAppointments();
        } catch (SQLException e){
            this.appointments = new ArrayList<Appointment>();
        }try{
            this.rooms = handler.getAllRooms();
        } catch (SQLException e){
            this.rooms = new ArrayList<MeetingRoom>();
        }try{
            this.groups = handler.getAllGroups();
        } catch (SQLException e){
            this.groups = new ArrayList<Group>();
        }try{
            this.allGroupMembers = handler.getAllMembersOfGroups();
        } catch (SQLException e){
            this.allGroupMembers = new TreeMap<Integer, ArrayList<String>>();
        }try{
            this.activeAlarms = handler.getAllActiveAlarms();
        }catch (SQLException e){
            this.activeAlarms = new TreeMap<Integer, ArrayList<Alarm>>();
        }
    }

    /**
     * Get all appointments
     * @return
     */
    public ArrayList<Appointment> getAppointments(){
        return appointments;
    }

    /**
     * Get appointment by appointment ID
     * @param AID
     * @return
     */
    public Appointment getAppointment(int AID){
        for (Appointment a : appointments){
            if(a.getAppointmentID() == AID){
                return a;
            }
        }
        return null;
    }

    /**
     * Get an arraylist of every person
     * @return
     */
    public ArrayList<Person> getPersons(){
        return persons;
    }

    /**
     * Get person by username
     * @param username
     * @return
     */
    public Person getPersonByUsername(String username){
        for(Person p : persons){
            if (p.getUsername().equals(username)){
                return p;
            }
        }
        return null;
    }

    /**
     * Get all rooms
     * @return
     */
    public ArrayList<MeetingRoom> getRooms(){
        return rooms;
    }

    /**
     * Get room by roomID
     * @param roomID
     * @return
     */
    public MeetingRoom getRoom(int roomID){
        for(MeetingRoom mr : rooms){
            if (roomID == mr.getRoomID()){
                return mr;
            }
        }
        return null;
    }

    /**
     * Get all groups
     * @return
     */
    public ArrayList<Group> getGroups(){
        return groups;
    }

    /**
     * Returns a group by groupID
     * @param groupID
     * @return
     */
    public Group getGroup(int groupID){
        for(Group group : groups){
            if(group.getGroupID() == groupID){
                return group;
            }
        }
        return null;
    }

    /**
     * Gets members of a group by groupID
     * @param groupID
     * @return
     */
    public ArrayList<Person> getMembersOfGroup(int groupID){

        if(!allGroupMembers.containsKey(groupID)){
            return null;
        }

        ArrayList<Person> results = new ArrayList<Person>();
        ArrayList<String> names = allGroupMembers.get(groupID);

        for(Person p : persons){
            for(String s : names){
                if(p.getUsername().equals(s)){
                    results.add(p);
                }
            }
        }
        return results;
    }

    /**
     * get all active alarms
     * @return
     */
    public TreeMap<Integer,ArrayList<Alarm>> getAllActiveAlarms(){
        return activeAlarms;
    }

    /**
     * Get an arraylist of active alarms by appointment
     * @param appointmentID
     * @return
     */
    public ArrayList<Alarm> getActiveAlarmByAID(int appointmentID){
        if(!activeAlarms.containsKey(appointmentID)){
            return null;
        }

        return activeAlarms.get(appointmentID);
    }

}
