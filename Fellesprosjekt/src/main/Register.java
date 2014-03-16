package main;

import model.*;
import net.Client;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
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
    private ArrayList<Appointment> userAppointments;
    private ArrayList<MeetingRoom> rooms;
    private ArrayList<Group> groups;
    private TreeMap<Integer, ArrayList<String>> allGroupMembers;
    private TreeMap<Integer, ArrayList<Alarm>> activeAlarms;

    public String getUserName() {
        return userName;
    }

    private String userName;

    public Register(Client client){
        this.client = client;
        this.client.addListener(new registerPacketListener());

        //Checks that the server is connected to the database
        this.client.request(new Packet("CONNECTING"));
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
                userName = user;
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
     * Get all user appointments by username.
     * @param username
     * @return
     */
    public ArrayList<Appointment> getUserAppointments(String username){
        if(this.userAppointments == null){
            Packet response = this.client.request(new Packet("GET_USER_APPOINTMENT",username));
            this.userAppointments = (ArrayList<Appointment>) response.getObjects()[0];
        }
        return userAppointments;
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
    public void addAppointment(String name, String start, String end, String description, int priority, String username, MeetingRoom mr, String alternativeLocation){
        Packet response = this.client.request(new Packet("ADD_APPOINTMENT", name, start, end, description, priority, username, mr, alternativeLocation));
        if (response.getName().equals("APPOINTMENT_ADDED")){
            Appointment a = (Appointment)response.getObjects()[0];
            appointments.add(a);
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
            MeetingRoom mr = (MeetingRoom) response.getObjects()[0];
            rooms.add(mr);
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
        Packet response = this.client.request(new Packet("ADD_GROUP",name));
        if (response.getName().equals("GROUP_ADDED")){
            Group g = (Group) response.getObjects()[0];
            groups.add(g);
        }
    }

    /**
     * Add person to group
     * @param g
     * @param p
     */
    public void addPersonToGroup(Group g, Person p){
        Packet response = this.client.request(new Packet("ADD_PERSON_TO_GROUP",g,p));
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
     * Gets all people that's member of a group
     * @return
     */
    public TreeMap<Integer, ArrayList<String>> getAllMembersOfGroup(){
        if (this.allGroupMembers == null){
            Packet response = this.client.request(new Packet("GET_ALL_GROUPMEMBERS"));
            allGroupMembers = (TreeMap<Integer, ArrayList<String>>)response.getObjects()[0];
        }
        return allGroupMembers;
    }

    /**
     * Gets members of a group by groupID
     * @param groupID
     * @return
     */
    public ArrayList<Person> getMembersOfGroup(int groupID){
        if (this.allGroupMembers == null){
            getAllMembersOfGroup();
        }

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
        if (activeAlarms == null){
            getAllActiveAlarms();
        }

        if(!activeAlarms.containsKey(appointmentID)){
            return null;
        }

        return activeAlarms.get(appointmentID);
    }

    /**
     * Invite person to appointment.
     * Sets alarm to null by default, remember to call addAlarm() if this person is meant to get an alarm!
     * @param p
     * @param a
     * @return
     */
    public boolean invitePerson(Person p, Appointment a){
        Packet response = this.client.request(new Packet("INVITE_PERSON_APPOINTMENT", p, a));
        if (response.getName().equals("PERSON_INVITED")){
            return true;
        }
        return false;
    }

    /**
     * Get list of people invited to an appointment
     * @param appointmentID
     * @return
     */
    public ArrayList<Person> getInvitees(int appointmentID){
        Packet response = this.client.request(new Packet("GET_INVITEES", appointmentID));
        if (response.getName().equals("INVITEES")){
            return (ArrayList<Person>) response.getObjects()[0];
        }
        return null;
    }

    /**
     * Updates register based on broadcast recieved by server
     * @param p
     */
    public void broadcast(Packet p){
        if (p.getName().equals("ALARM_UPDATED")){
            getAllActiveAlarms();
        }else if (p.getName().equals("GROUP_MEMBER_UPDATED")){
            getAllMembersOfGroup();
        }else if (p.getName().equals("GROUP_UPDATED")){
            getGroups();
        }else if (p.getName().equals("ROOM_UPDATED")){
            getRooms();
        }else if (p.getName().equals("APP_UPDATED")){
            getAppointments();
        }else if (p.getName().equals("PERSON_UPDATED")){
            getPersons();
        }
    }

    /**
     * Packetlistener.
     */
    private class registerPacketListener implements Client.PacketListener{
        @Override
        public void packetSent(Packet p) {
            System.out.println(p.getName());
            broadcast(p);
        }
    }

    public void setGroups(ArrayList<Group> groups) {
        this.groups = groups;
    }
    public void setPersons(ArrayList<Person> persons) {
        this.persons = persons;
    }
    public void setAppointments(ArrayList<Appointment> appointments) {
        this.appointments = appointments;
    }
    public void setRooms(ArrayList<MeetingRoom> rooms) {
        this.rooms = rooms;
    }
    public void setActiveAlarms(TreeMap<Integer, ArrayList<Alarm>> activeAlarms) {
        this.activeAlarms = activeAlarms;
    }

}
