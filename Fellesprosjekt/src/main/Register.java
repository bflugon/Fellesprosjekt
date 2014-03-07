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
     * Create account
     * @param user
     * @param pass
     * @param name
     * @param email
     */
    public void createAccount(String user, String pass, String name, String email){
        try{
            Person p = mHandler.createAccount(user,pass,name,email);
            persons.add(p);
        }catch (SQLException e){
            e.printStackTrace();
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
     * Adds appointment to database.
     * @param a Appointment
     * @param mr MeetingRoom
     */
    public void addAppointment(Appointment a, MeetingRoom mr){
        try{
            Appointment appointment = mHandler.addAppointment(a.getAppointmentName(),a.getAppointmentStart(),a.getAppointmentEnd(),a.getDescription(),a.getPriority(),a.getOwnerName(),mr);
            appointments.add(appointment);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Edit appointment
     * @param a Appointment
     * @param mr MeetingRoom
     */
    public void editAppointment(Appointment a, MeetingRoom mr){
        try{
            mHandler.editAppointment(a.getAppointmentID(),a.getAppointmentName(),a.getAppointmentStart(),a.getAppointmentEnd(),a.getDescription(),a.getPriority(),mr);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Delete appointment
     * @param a Appointment
     */
    public void deleteAppointment(Appointment a){
        try{
            mHandler.deleteAppointment(a.getAppointmentID());
            appointments.remove(a);
        }catch (SQLException e){
            e.printStackTrace();
        }
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
     * Add new room
     * @param name
     * @param capacity
     */
    public void addRoom(String name, int capacity){
        try{
            MeetingRoom room = mHandler.addRoom(name, capacity);
            rooms.add(room);
        }catch (SQLException e){
            e.printStackTrace();
        }
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
     * Add group
     * @param name
     */
    public void addGroup(String name){
        try{
            Group group = mHandler.addGroup(name);
            groups.add(group);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Add person to group
     * @param g
     * @param p
     */
    public void addPersonToGroup(Group g, Person p){
        try{
            mHandler.addPersonToGroup(g.getGroupID(),p);

            if(allGroupMembers.containsKey(g.getGroupID())){
                allGroupMembers.get(g.getGroupID()).add(p.getUsername());
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
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
