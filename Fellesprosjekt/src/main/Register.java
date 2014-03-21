package main;

import com.javafx.tools.doclets.formats.html.SourceToHTMLConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private final Client client;



    private ArrayList<Person> persons;
    private ArrayList<Appointment> appointments;
    private ArrayList<Appointment> userAppointments;
    private ObservableList<Appointment> alertAppointments;
    private ArrayList<MeetingRoom> rooms;
    private ArrayList<Group> groups;
    private TreeMap<Integer, ArrayList<String>> allGroupMembers;
    private TreeMap<Integer, ArrayList<Alarm>> activeAlarms;
    private ObservableList<Person> availableAdditionalPersonCalendars;
    private ObservableList<Person> selectedAdditionalPersonCalendars;

    private String userName;

    public String getEmail() {
        return email;
    }

    private String email;

    private Boolean hasAlarm;

    public Boolean getHidesNotAttendingMeetings() {
        return hidesNotAttendingMeetings;
    }

    public void setHidesNotAttendingMeetings(Boolean hidesNotAttendingMeetings) {
        this.hidesNotAttendingMeetings = hidesNotAttendingMeetings;
    }

    private Boolean hidesNotAttendingMeetings;

    private int hoursBeforeAlarm;



    public ObservableList<Person> getAvailableAdditionalPersonCalendars() {


        if (availableAdditionalPersonCalendars == null){
            Packet response = this.client.request(new Packet("GET_ALL_PEOPLE"));
            if(response.getName().equals("ALL_PERSONS")){
                availableAdditionalPersonCalendars = FXCollections.observableArrayList();

                for (Person person : (ArrayList<Person>)response.getObjects()[0]){

                    System.out.println("peron username: " + person.getUsername());
                    System.out.println("** username: " + userName);

                    if (!person.getUsername().equals(userName)){
                        availableAdditionalPersonCalendars.add(person);

                    }
                }


            }
        }

        return availableAdditionalPersonCalendars;
    }

    public ObservableList<Person> getSelectedAdditionalPersonCalendars() {

        if (selectedAdditionalPersonCalendars == null){
            selectedAdditionalPersonCalendars = FXCollections.observableArrayList();
        }
        return selectedAdditionalPersonCalendars;
    }


    public Register(Client client){
        this.client = client;
        this.client.addListener(new registerPacketListener());

        //Checks that the server is connected to the database
        this.client.request(new Packet("CONNECTING"));
    }


    public ObservableList<Appointment> getAlertAppointments() {
        if (alertAppointments == null){return  alertAppointments = FXCollections.observableArrayList();}
        return alertAppointments;
    }


    public Boolean getHasAlarm() {
        return hasAlarm;
    }
    public void setHoursBeforeAlarm(int hoursBeforeAlarm) {
        this.hoursBeforeAlarm = hoursBeforeAlarm;
    }

    public void setHasAlarm(Boolean hasAlarm) {
        this.hasAlarm = hasAlarm;
    }

    public int getHoursBeforeAlarm() {
        return hoursBeforeAlarm;
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
            if(persons == null){
                this.getPersons();
            }
            persons.add(p);
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
        System.out.println(username);
        Packet response = this.client.request(new Packet("GET_USER_APPOINTMENT",username));
        System.out.println(response.getName());
        if (response.getName().equals(username)){
            System.out.println("Halla" + (ArrayList<Appointment>)response.getObjects()[0]);

            for (Appointment a :(ArrayList<Appointment>)response.getObjects()[0] ){
                System.out.println("Appointment name: "+ a.getAppointmentName());
            }
            return  (ArrayList<Appointment>) response.getObjects()[0];
        }else{
            System.out.println("Returns empty array");
            return new ArrayList<Appointment>();
        }
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
    public Appointment addAppointment(String name, String start, String end, String description, int priority, String username, MeetingRoom mr, String alternativeLocation){
        Packet response = this.client.request(new Packet("ADD_APPOINTMENT", name, start, end, description, priority, username, mr, alternativeLocation));
        if (response.getName().equals("APPOINTMENT_ADDED")){
            Appointment a = (Appointment)response.getObjects()[0];
            //appointments.add(a);
            return a;
        }
        return null;
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

    public ArrayList<Person> getAttendingPeople(int appointmentID){
        Packet response = this.client.request(new Packet("GET_PEOPLE_ATTENDING_APP", appointmentID));
        if (response.getName().equals("PEOPLE_ATTENDING_APP")){
            return (ArrayList<Person>) response.getObjects()[0];
        }
        return null;
    }

    public ArrayList<Appointment> getAppointmentsAttendingForUsername(String username){
        Packet response = this.client.request(new Packet("GET_APP_A_U", username));
        if (response.getName().equals("GOT_APP_A_U")){
            return (ArrayList<Appointment>) response.getObjects()[0];
        }
        return null;
    }

    public ArrayList<Appointment> getAppointmentsNotAttendingForUsername(String username){
        Packet response = this.client.request(new Packet("GET_APP_NA_U", username));
        if (response.getName().equals("GOT_APP_NA_U")){
            return (ArrayList<Appointment>) response.getObjects()[0];
        }
        return null;
    }

    public ArrayList<Appointment> getAppointmentsCreatedByUsername(String username){
        Packet response = this.client.request(new Packet("GET_APP_C_U", username));
        if (response.getName().equals("GOT_APP_C_U")){
            return (ArrayList<Appointment>) response.getObjects()[0];
        }
        return null;
    }

    public String getDateChangedForAppointment(int aID){
        Packet response = this.client.request(new Packet("GET_DC_A", aID));
        if (response.getName().equals("GOT_DC_A")){
            return (String) response.getObjects()[0];
        }
        return null;
    }

    public ArrayList<Person> getNotAttendingPeople(int aID){
        Packet response = this.client.request(new Packet("GET_NAP", aID));
        if (response.getName().equals("GOT_NAP")){
            return (ArrayList<Person>) response.getObjects()[0];
        }
        return null;
    }

    public String setUserLastLoggedIn(String username){
        Packet response = this.client.request(new Packet("SET_LOGIN_DATE", username));
        if (response.getName().equals("SET_LOGIN_DATE")){
            return (String) response.getObjects()[0];
        }
        return null;
    }

    public boolean updateAttending(int appointmentID, String username, int attends){
        Packet response = this.client.request(new Packet("UPDATE_ATTENDING", appointmentID, username, attends));
        if (response.getName().equals("ATTENDING_UPDATED")){
            return true;
        }
        return false;
    }

    public ArrayList<Appointment> getRoomAppointments(int roomID){
        Packet response = this.client.request(new Packet("GET_ROOM_APP",roomID));
        if (response.getName().equals("ROOM_APPOINTMENTS")){
            return (ArrayList<Appointment>) response.getObjects()[0];
        }
        return null;
    }

    /**
     * Updates register based on broadcast recieved by server
     * @param p
     */
    public void broadcast(Packet p){
        if (p.getName().equals("ALARM_UPDATED")){
            activeAlarms = (TreeMap<Integer, ArrayList<Alarm>>)p.getObjects()[0];
        }else if (p.getName().equals("GROUP_MEMBER_UPDATED")){
            allGroupMembers = (TreeMap<Integer, ArrayList<String>>)p.getObjects()[0];
        }else if (p.getName().equals("GROUP_UPDATED")){
            groups = (ArrayList<Group>)p.getObjects()[0];
        }else if (p.getName().equals("ROOM_UPDATED")){
            rooms = (ArrayList<MeetingRoom>)p.getObjects()[0];
        }else if (p.getName().equals("APP_UPDATED")){
            appointments = (ArrayList<Appointment>)p.getObjects()[0];
        }else if (p.getName().equals("PERSON_UPDATED")){
            persons = (ArrayList<Person>)p.getObjects()[0];
        }else if (p.getName().equals("INVITED_UPDATED")){
            //Do what?
        }
    }

    public String getUsername() {
        return this.userName;
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
