package main;

import db.DatabaseHandler;
import model.*;
import net.Client;

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
    private Client client;
    private ArrayList<Person> persons;
    private ArrayList<Appointment> appointments;
    private ArrayList<MeetingRoom> rooms;
    private ArrayList<Group> groups;
    private TreeMap<Integer, ArrayList<String>> allGroupMembers;
    private TreeMap<Integer, ArrayList<Alarm>> activeAlarms;

    public Register(Client client){
        this.client = client;
    }

    /**
     * Authenticates account.
     * @param user
     * @param pass
     * @return
     */
    public boolean authenticate(String user, String pass){
        Packet response = this.client.request(new Packet("AUTHENTICATE", user, pass));
        if (response.getName().equals("AUTHENTICATION")){
            if ((boolean)response.getObjects()[0]){
                return true;
            }
        }
        return false;
    }

    /**
     * Create account
     * @param user
     * @param pass
     * @param name
     * @param email
     */
    public void createAccount(String user, String pass, String name, String email){
        Packet response = this.client.request(new Packet("CREATE_ACCOUNT",user,pass,name,email));

        if(response.getName().equals("ACCOUNT_CREATED")){
            Person p = (Person) response.getObjects()[0];
            persons.add(p);
            p.toString();
        }
    }

    /**
     * Get all appointments
     * @return
     */
    public ArrayList<Appointment> getAppointments(){
        if (this.appointments == null){
            Packet response = this.client.request(new Packet("GET_APPOINTMENTS"));
            appointments = (ArrayList<Appointment>)response.getObjects()[0];
        }
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
     * Adds appointment
     * @param name
     * @param start
     * @param end
     * @param description
     * @param priority
     * @param username
     * @param mr
     */
    public void addAppointment(String name, String start, String end, String description, int priority, String username, MeetingRoom mr){
        Packet response = this.client.request(new Packet("ADD_APPOINTMENT", name, start, end, description, priority, username, mr));
        if (response.getName().equals("APPOINTMENT_ADDED")){
            appointments.add((Appointment)response.getObjects()[0]);
        }
    }

    /**
     * Edit appointment
     * @param a Appointment
     * @param mr MeetingRoom
     */
    public void editAppointment(Appointment a, MeetingRoom mr){
        this.client.request(new Packet("EDIT_APPOINTMENT", a, mr));
    }

    /**
     * Delete appointment
     * @param a Appointment
     */
    public void deleteAppointment(Appointment a){
        Packet response = this.client.request(new Packet("DELETE_APPOINTMENT",a));
        if (response.getName().equals("APPOINTMENT_DELETED")){
            appointments.remove(a);
        }
    }

    /**
     * Get an arraylist of every person
     * @return
     */
    public ArrayList<Person> getPersons(){
        if (persons == null){
            Packet response = this.client.request(new Packet("GET_ALL_PEOPLE"));
            if(response.getName().equals("ALL_PERSONS")){
                persons = (ArrayList<Person>)response.getObjects()[0];
            }
        }
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
        if (rooms == null){
            Packet response = this.client.request(new Packet("GET_ALL_ROOMS"));
            if (response.getName().equals("ALL_ROOMS")){
                rooms = (ArrayList<MeetingRoom>) response.getObjects()[0];
            }
        }
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
        Packet response = this.client.request(new Packet("ADD_ROOM",name,capacity));
        if(response.getName().equals("ROOM_ADDED")){
            rooms.add((MeetingRoom) response.getObjects()[0]);
        }
    }

    /**
     * Get all groups
     * @return
     */
    public ArrayList<Group> getGroups(){
        if (groups == null){
            Packet response = this.client.request(new Packet("GET_ALL_GROUPS"));
            if (response.getName().equals("ALL_GROUPS")){
                groups = (ArrayList<Group>) response.getObjects()[0];
            }
        }
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
        Packet response = this.client.request(new Packet("ADD_GROUP", name));
        if (response.getName().equals("GROUP_ADDED")){
            groups.add((Group)response.getObjects()[0]);
        }
    }

    /**
     * Add person to group
     * @param g
     * @param p
     */
    public void addPersonToGroup(Group g, Person p){
        Packet response = this.client.request(new Packet("ADD_PERSON_TO_GROUP", g,p));
        if(response.getName().equals("PERSON_ADDED_TO_GROUP")){
            if(allGroupMembers.containsKey(g.getGroupID())){
                allGroupMembers.get(g.getGroupID()).add(p.getUsername());
            } else{
                ArrayList<String> temp = new ArrayList<String>();
                temp.add(p.getUsername());
                allGroupMembers.put(g.getGroupID(),temp);
            }
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
        if (activeAlarms == null){
            Packet response = this.client.request(new Packet("GET_ALL_ALARMS"));
            if (response.getName().equals("ALL_ALARMS")){
                activeAlarms = (TreeMap<Integer,ArrayList<Alarm>>) response.getObjects()[0];
            }
        }

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
