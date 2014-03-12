package db;

import model.*;
import net.Server;
import util.GeneralUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created with IntelliJ IDEA.
 * User: FF63
 * Date: 3/5/14
 * Time: 6:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class DatabaseHandler {
    private Connection db;
    private Server server;
    /**
     * Initializes database connection.
     */
    public DatabaseHandler(Server server){
        this.server = server;
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            this.db = DriverManager.getConnection(DatabaseSettings.getURL(), DatabaseSettings.getUsername(), DatabaseSettings.getPassword());
            System.out.println("Connection established!");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Closes database connection
     */
    public void close(){
        try{
            this.db.close();
            System.out.println("Connection closed");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Checks entered username/password with db.
     * @param username Username
     * @param password Password
     * @return true if authentication is successful, false if authentication failed
     */
    public boolean authenticate(String username, String password){
        try{
            PreparedStatement query = this.db.prepareStatement("SELECT password FROM person WHERE username = ?");
            query.setString(1, username);
            ResultSet rs = query.executeQuery();

            if (!rs.next()){
                return false;
            }

            String hash = encryptPassword(password);

            if(hash.equals(rs.getString("password"))){
                return true;
            }
            return false;

        } catch (SQLException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Creates an account
     * @param username
     * @param password
     * @param name
     * @param email
     * @return
     * @throws java.sql.SQLException
     */
    public Person createAccount(String username, String password, String name, String email) throws SQLException{
        try{
            PreparedStatement query = this.db.prepareStatement("INSERT INTO person(Username, Password, PName, Email) VALUES (?,?,?,?)");
            query.setString(1, username);
            query.setString(2,encryptPassword(password));
            query.setString(3,name);
            query.setString(4,email);
            query.executeUpdate();
            broadcast(new Packet("PERSON_UPDATED"));
            return (new Person(username, name, email));

        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns an ArrayList containing every person in the db.
     * @return ArrayList<Person>
     * @throws java.sql.SQLException
     */
    public ArrayList<Person> getAllPersons() throws SQLException{
        PreparedStatement query = this.db.prepareStatement("SELECT * FROM person");
        ResultSet rs = query.executeQuery();

        if (!rs.next()){
            return null;
        }

        ArrayList<Person> results = new ArrayList<Person>();
        results.add(new Person(rs.getString("Username"),rs.getString("PName"), rs.getString("Email")));

        while(rs.next()){
            results.add(new Person(rs.getString("Username"),rs.getString("PName"), rs.getString("Email")));
        }

        return results;
    }

    /**
     * Returns person by username
     * @param username
     * @return
     * @throws SQLException
     */
    public Person getPersonByUsername(String username) throws SQLException{
        PreparedStatement query = this.db.prepareStatement("SELECT * FROM person WHERE Username = ?");
        query.setString(1,"username");
        ResultSet rs = query.executeQuery();

        if (!rs.next()){
            return null;
        }

        return new Person(rs.getString("Username"), rs.getString("PName"), rs.getString("Email"));
    }

    /**
     * Returns an ArrayList containing all the Appointments in the db
     * @return ArrayList<Appointment>
     * @throws java.sql.SQLException
     */
    public ArrayList<Appointment> getAllAppointments() throws SQLException{
       PreparedStatement query = this.db.prepareStatement("select appointment.AID, appointment.AName, appointment.Description, appointment.Start, appointment.End, appointment.Priority, appointment.DateCreated, appointment.AlternativeLocation, isleader.Username, room.RName, room.RID, room.Capacity FROM appointment INNER JOIN isleader ON appointment.AID = isleader.AID INNER JOIN takesplace ON appointment.AID = takesplace.AID INNER JOIN room ON takesplace.RID = room.RID ORDER BY appointment.AID");
        ResultSet rs = query.executeQuery();

        if (!rs.next()){
            return null;
        }

        ArrayList<Appointment> results = new ArrayList<Appointment>();
        results.add(new Appointment(rs.getInt("AID"),rs.getString("Username"),rs.getString("AName"), rs.getTimestamp("Start"), rs.getTimestamp("End"), rs.getInt("Priority"), rs.getString("Description"), rs.getTimestamp("DateCreated"), new MeetingRoom(rs.getInt("RID"),rs.getString("RName"), rs.getInt("Capacity")),rs.getString("AlternativeLocation")));

        while(rs.next()){
            results.add(new Appointment(rs.getInt("AID"),rs.getString("Username"),rs.getString("AName"), rs.getTimestamp("Start"), rs.getTimestamp("End"), rs.getInt("Priority"), rs.getString("Description"), rs.getTimestamp("DateCreated"), new MeetingRoom(rs.getInt("RID"),rs.getString("RName"), rs.getInt("Capacity")),rs.getString("AlternativeLocation")));
        }
        return results;
    }

    /**
     * Adds an appointment to db
     * String start and end should be in the format yyyy-MM-dd hh:mm:ss
     * @param name
     * @param start
     * @param end
     * @param description
     * @param priority
     * @throws java.sql.SQLException
     */
    public Appointment addAppointment(String name, String start, String end, String description, int priority, String ownerUsername, MeetingRoom mr, String altLoc) throws SQLException{
        int id = getNextAutoIncrement("appointment");

        PreparedStatement query = this.db.prepareStatement("INSERT INTO appointment(AID, AName, Start, End, Description, Priority, DateCreated) VALUES (?,?,?,?,?,?,?)");
        query.setInt(1,id);
        query.setString(2, name);
        query.setTimestamp(3, Timestamp.valueOf(start));
        query.setTimestamp(4, Timestamp.valueOf(end));
        query.setString(5, description);
        query.setInt(6,priority);
        java.util.Date currentTime = new java.util.Date();
        query.setTimestamp(7, Timestamp.valueOf(GeneralUtil.dateToString(currentTime)));
        query.executeUpdate();

        addLeader(id,ownerUsername);
        addTakesPlace(id,mr);

        broadcast(new Packet("APP_UPDATED"));
        return (new Appointment(id, ownerUsername,name, GeneralUtil.stringToDate(start), GeneralUtil.stringToDate(end),priority,description,new java.util.Date(),mr, altLoc));
    }

    /**
     * Adds the leader of an appointment to the db. Called from within addAppointment.
     * @param aID
     * @param ownerUsername
     * @throws java.sql.SQLException
     */
    private void addLeader(int aID,String ownerUsername) throws SQLException{
        int id = getNextAutoIncrement("isleader");

        PreparedStatement query = this.db.prepareStatement("INSERT INTO isleader(ilID,Username,AID) VALUES (?,?,?)");
        query.setInt(1,id);
        query.setString(2,ownerUsername);
        query.setInt(3, aID);
        query.executeUpdate();
    }

    /**
     * Adds where the meeting takes place. Gets called from within addAppointment
     * @param aID
     * @param mr
     * @throws java.sql.SQLException
     */
    private void addTakesPlace(int aID, MeetingRoom mr) throws SQLException{
        int id = getNextAutoIncrement("takesplace");

        PreparedStatement query = this.db.prepareStatement("INSERT INTO takesplace(tpID,AID,RID) VALUES(?,?,?)");
        query.setInt(1,id);
        query.setInt(2, aID);
        query.setInt(3, mr.getRoomID());
        query.executeUpdate();
    }

    /**
     * Edit exisiting appointment
     * @param aID
     * @param name
     * @param start
     * @param end
     * @param description
     * @param priority
     * @param mr
     * @throws java.sql.SQLException
     */
    public void editAppointment(int aID, String name, String start, String end, String description, int priority, MeetingRoom mr) throws SQLException{
        PreparedStatement query = this.db.prepareStatement("UPDATE appointment SET AName = ?, Start = ?, End = ?, Description = ?, Priority = ?, DateChanged = ? WHERE AID = ?");
        query.setString(1,name);
        query.setString(2, start);
        query.setString(3, end);
        query.setString(4, description);
        query.setInt(5,priority);
        java.util.Date currentTime = new java.util.Date();
        query.setTimestamp(6,Timestamp.valueOf(GeneralUtil.dateToString(currentTime)));
        query.setInt(7,aID);
        query.executeUpdate();

        editTakesPlace(aID, mr);
        broadcast(new Packet("APP_UPDATED"));
    }

    /**
     * Edit where the metting takes place. Called from within editAppointment.
     * @param aID
     * @param mr
     * @throws java.sql.SQLException
     */
    private void editTakesPlace(int aID, MeetingRoom mr) throws SQLException{
        PreparedStatement query = this.db.prepareStatement("UPDATE takesplace SET RID = ? WHERE AID = ?");
        query.setInt(1, mr.getRoomID());
        query.setInt(2, aID);
        query.executeUpdate();
    }

    /**
     * Deletes appointment
     * @param aID
     * @throws java.sql.SQLException
     */
    public void deleteAppointment(int aID) throws SQLException{

        PreparedStatement query = this.db.prepareStatement("DELETE FROM isleader WHERE AID = ?");
        query.setInt(1,aID);
        query.executeUpdate();

        query = this.db.prepareStatement("DELETE FROM invitedto WHERE AID = ?");
        query.setInt(1,aID);
        query.executeUpdate();

        query = this.db.prepareStatement("DELETE FROM takesplace WHERE AID = ?");
        query.setInt(1,aID);
        query.executeUpdate();

        query = this.db.prepareStatement("DELETE FROM appointment WHERE AID = ?");
        query.setInt(1, aID);
        query.executeUpdate();

        broadcast(new Packet("APP_UPDATED"));
    }

    /**
     * Returns an ArrayList containing all meeting rooms
     * @return Arraylist<MeetingRoom>
     * @throws java.sql.SQLException
     */
    public ArrayList<MeetingRoom> getAllRooms() throws SQLException{
        PreparedStatement query = this.db.prepareStatement("SELECT * FROM room");
        ResultSet rs = query.executeQuery();

        if (!rs.next()){
            return null;
        }

        ArrayList<MeetingRoom> results = new ArrayList<MeetingRoom>();
        results.add(new MeetingRoom(rs.getInt("RID"), rs.getString("RName"), rs.getInt("Capacity")));

        while (rs.next()){
            results.add(new MeetingRoom(rs.getInt("RID"), rs.getString("RName"), rs.getInt("Capacity")));
        }

        return results;
    }

    /**
     * Creates a new room and adds it to db
     * @param roomName
     * @param capacity
     * @throws java.sql.SQLException
     */
    public MeetingRoom addRoom(String roomName, int capacity) throws SQLException{
        int id = getNextAutoIncrement("room");

        PreparedStatement query = this.db.prepareStatement("INSERT INTO room(RID,RName,Capacity) VALUES (?,?,?)");

        query.setInt(1,id);
        query.setString(2,roomName);
        query.setInt(3, capacity);

        query.executeUpdate();

        broadcast(new Packet("ROOM_UPDATED"));
        return (new MeetingRoom(id,roomName,capacity));
    }

    /**
     * Creates a new group and adds it to db
     * @param GName
     * @throws java.sql.SQLException
     */
    public Group addGroup(String GName) throws SQLException{
        int id = getNextAutoIncrement("groups");

        PreparedStatement query = this.db.prepareStatement("INSERT INTO groups(GID,GName) VALUES (?,?)");
        query.setInt(1,id);
        query.setString(2,GName);
        query.executeUpdate();

        broadcast(new Packet("GROUP_UPDATED"));
        return (new Group(id, GName));
    }

    /**
     * Adds a person to a group
     * @param gID
     * @param p
     * @throws java.sql.SQLException
     */
    public void addPersonToGroup(int gID, Person p) throws SQLException{
        int id = getNextAutoIncrement("memberof");

        PreparedStatement query = this.db.prepareStatement("INSERT INTO memberof(moID, GID, Username) VALUES (?,?,?)");
        query.setInt(1,id);
        query.setInt(2, gID);
        query.setString(3, p.getUsername());
        query.executeUpdate();

        broadcast(new Packet("GROUP_MEMBER_UPDATED"));
    }

    /**
     * Returns a list of all groups
     * @return
     * @throws java.sql.SQLException
     */
    public ArrayList<Group> getAllGroups() throws SQLException{
        PreparedStatement query = this.db.prepareStatement("SELECT * FROM groups");
        ResultSet rs = query.executeQuery();

        if (!rs.next()){
            return null;
        }

        ArrayList<Group> results = new ArrayList<Group>();
        results.add(new Group(rs.getInt("GID"), rs.getString("GName")));
        while (rs.next()){
            results.add(new Group(rs.getInt("GID"),rs.getString("GName")));
        }
        return results;
    }

    /**
     * Get every person which is a member of a group.
     * @return
     * @throws java.sql.SQLException
     */
    public TreeMap<Integer, ArrayList<String>> getAllMembersOfGroups() throws SQLException{
        PreparedStatement query = this.db.prepareStatement("SELECT * FROM memberof");

        ResultSet rs = query.executeQuery();

        if (!rs.next()){
            return null;
        }

        TreeMap<Integer, ArrayList<String>> results = new TreeMap<Integer, ArrayList<String>>();
        int temp = rs.getInt("GID");
        ArrayList<String> strings = new ArrayList<String>();
        strings.add(rs.getString("Username"));

        while(rs.next()){
            //if new groupID is different, put the old one and the arraylist into the TreeMap.
            if (temp != rs.getInt("GID")){
                results.put(temp,strings);
                strings = new ArrayList<String>();
            }
            temp = rs.getInt("GID");
            strings.add(rs.getString("Username"));
        }
        //add the final key + arraylist<String> to the TreeMap
        results.put(temp,strings);
        return results;
    }

    /**
     * Returns a treemap containing all alarms, sorted by appointmentID.
     * @return
     * @throws java.sql.SQLException
     */
    public TreeMap<Integer, ArrayList<Alarm>> getAllAlarms() throws SQLException{
        PreparedStatement query = this.db.prepareStatement("SELECT * FROM invitedto WHERE hasAlarm = 1");
        ResultSet rs = query.executeQuery();

        if (!rs.next()){
            return null;
        }

        TreeMap<Integer, ArrayList<Alarm>> results = new TreeMap<Integer, ArrayList<Alarm>>();
        int temp = rs.getInt("AID");
        ArrayList<Alarm> alarms = new ArrayList<Alarm>();

        alarms.add(new Alarm(rs.getInt("itID"), rs.getString("Username"), rs.getTimestamp("AlarmTime"), rs.getInt("Attends")));

        while(rs.next()){
            if (temp != rs.getInt("AID")){
                results.put(temp,alarms);
                alarms = new ArrayList<Alarm>();
            }
            temp = rs.getInt("AID");
            alarms.add(new Alarm(rs.getInt("itID"), rs.getString("Username"), rs.getTimestamp("AlarmTime"), rs.getInt("Attends")));
        }

        results.put(temp, alarms);
        return results;
    }

    /**
     * Activates or deactivates alarm
     * @param appointmentID
     * @param username
     * @param hasAlarm
     * @throws java.sql.SQLException
     */
    public void activateAlarm(int appointmentID, String username, int hasAlarm) throws SQLException{
        PreparedStatement query = this.db.prepareStatement("UPDATE invitedto SET hasAlarm = ? WHERE AID = ?, Username = ?");
        query.setInt(1, hasAlarm);
        query.setInt(2, appointmentID);
        query.setString(3,username);
        query.executeUpdate();
        broadcast(new Packet("ALARM_UPDATED"));
    }

    /**
     * Activates or deactivates alarm
     * @param alarmID
     * @param hasAlarm
     * @throws java.sql.SQLException
     */
    public void activateAlarm(int alarmID, int hasAlarm) throws SQLException{
        PreparedStatement query = this.db.prepareStatement("UPDATE invitedto SET hasAlarm = ? WHERE itID = ?");
        query.setInt(1, hasAlarm);
        query.setInt(2, alarmID);
        query.executeUpdate();
        broadcast(new Packet("ALARM_UPDATED"));
    }

    /**
     * Add a new alarm
     * @param username
     * @param appointmentID
     * @param hasAlarm
     * @param alarmTime
     * @throws java.sql.SQLException
     */
    public boolean addAlarm(String username, int appointmentID, int hasAlarm, String alarmTime, int attending) throws SQLException{
        PreparedStatement query = this.db.prepareStatement("UPDATE invitedto SET hasAlarm = ?, AlarmTime = ?, Attends = ? WHERE Username = ?, AID = ?");
        query.setInt(1,hasAlarm);
        query.setTimestamp(2, Timestamp.valueOf(alarmTime));
        query.setInt(3,attending);
        query.setString(4, username);
        query.setInt(5,appointmentID);
        query.executeUpdate();

        broadcast(new Packet("ALARM_UPDATED"));
        return true;
    }

    /**
     * Invite person to appointment.
     * @param p
     * @param a
     * @throws SQLException
     */
    public boolean invitePerson(Person p, Appointment a) throws SQLException{
        PreparedStatement query = this.db.prepareStatement("INSERT INTO invitedto(itID,Username,AID,hasAlarm,AlarmTime,Attends) VALUES (?,?,?,?,?,?)");
        int id = getNextAutoIncrement("invitedto");
        query.setInt(1, id);
        query.setString(2, p.getUsername());
        query.setInt(3,a.getAppointmentID());
        query.setInt(4,0);
        query.setNull(5, Types.INTEGER);
        query.setInt(6,0);
        query.executeUpdate();

        return true;
    }

    /**
     * Set attending status of alarm
     * @param alarmID
     * @param attending
     * @throws java.sql.SQLException
     */
    public void updateAttending(int alarmID, int attending) throws SQLException{
        PreparedStatement query = this.db.prepareStatement("UPDATE invitedto SET Attends = ? WHERE itID = ?");
        query.setInt(1,attending);
        query.setInt(2, alarmID);
        query.executeUpdate();
        broadcast(new Packet("INVITED_UPDATED"));
    }

    private void broadcast(Packet p){
        this.server.broadcast(p);
    }

    /**
     * Returns next auto increment of table
     * @param table
     * @return
     * @throws java.sql.SQLException
     */
    private int getNextAutoIncrement(String table) throws SQLException{
        PreparedStatement query = this.db.prepareStatement("SHOW TABLE STATUS LIKE ?");
        query.setString(1, table);

        ResultSet rs = query.executeQuery();
        rs.next();

        return rs.getInt("Auto_increment");
    }

    /**
     * Encrypts password
     * @param string
     * @return
     * @throws NoSuchAlgorithmException
     */
    private String encryptPassword(String string) throws NoSuchAlgorithmException{
        MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
        byte[] result = messageDigest.digest(string.getBytes());
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < result.length; i++){
            buffer.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
        return buffer.toString();
    }
}
