package db;

import model.Appointment;
import model.MeetingRoom;
import model.Person;
import util.GeneralUtil;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: FF63
 * Date: 3/5/14
 * Time: 6:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class DatabaseHandler {
    private Connection db;


    /**
     * Initializes database connection.
     */
    public DatabaseHandler(){
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            this.db = DriverManager.getConnection(DatabaseSettings.getURL(), DatabaseSettings.getUsername(),DatabaseSettings.getPassword());
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
            if(password.equals(rs.getString("password"))){
                return true;
            }
            return false;

        } catch (SQLException e) {
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
     * @throws SQLException
     */
    public void createAccount(String username, String password, String name, String email) throws SQLException{
        try{
            PreparedStatement query = this.db.prepareStatement("INSERT INTO person(Username, PName, Password, Email VALUES (?,?,?,?)");
            query.setString(1,username);
            query.setString(2,password);
            query.setString(3,name);
            query.setString(4,email);
            query.executeUpdate();

        }catch (Exception e){
            e.printStackTrace();

        }
    }

    /**
     * Returns an ArrayList containing every person in the db.
     * @return ArrayList<Person>
     * @throws SQLException
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
     * Returns an ArrayList containing all the Appointments in the db
     * @return ArrayList<Appointment>
     * @throws SQLException
     */
    public ArrayList<Appointment> getAllAppointments() throws SQLException{
       PreparedStatement query = this.db.prepareStatement("select appointment.AID, appointment.AName, appointment.Description, appointment.Start, appointment.End, appointment.Priority, appointment.DateCreated, isleader.Username, room.RName, room.RID, room.Capacity FROM appointment INNER JOIN isleader ON appointment.AID = isleader.AID INNER JOIN takesplace ON appointment.AID = takesplace.AID INNER JOIN room ON takesplace.RID = room.RID ORDER BY appointment.AID");
        ResultSet rs = query.executeQuery();

        if (!rs.next()){
            return null;
        }

        ArrayList<Appointment> results = new ArrayList<Appointment>();
        results.add(new Appointment(rs.getInt("AID"),rs.getString("Username"),rs.getString("AName"), rs.getTimestamp("Start"), rs.getTimestamp("End"), rs.getInt("Priority"), rs.getString("Description"), rs.getTimestamp("DateCreated"), new MeetingRoom(rs.getInt("RID"),rs.getString("RName"), rs.getInt("Capacity"))));

        while(rs.next()){
            results.add(new Appointment(rs.getInt("AID"),rs.getString("Username"),rs.getString("AName"), rs.getTimestamp("Start"), rs.getTimestamp("End"), rs.getInt("Priority"), rs.getString("Description"), rs.getTimestamp("DateCreated"), new MeetingRoom(rs.getInt("RID"),rs.getString("RName"), rs.getInt("Capacity"))));
        }
        return results;
    }

    /**
     * Adds an appointment to db
     * @param name
     * @param start
     * @param end
     * @param description
     * @param priority
     * @throws SQLException
     */
    public void addAppointment(String name, String start, String end, String description, int priority, String ownerUsername, MeetingRoom mr) throws SQLException{
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
    }

    /**
     * Adds the leader of an appointment to the db. Called from within addAppointment.
     * @param aID
     * @param ownerUsername
     * @throws SQLException
     */
    private void addLeader(int aID,String ownerUsername) throws SQLException{
        int id = getNextAutoIncrement("isleader");

        PreparedStatement query = this.db.prepareStatement("INSERT INTO isleader(ilID,Username,AID) VALUES (?,?,?)");
        query.setInt(1,id);
        query.setString(2,ownerUsername);
        query.setInt(3,aID);
        query.executeUpdate();
    }

    /**
     * Adds where the meeting takes place. Gets called from within addAppointment
     * @param aID
     * @param mr
     * @throws SQLException
     */
    private void addTakesPlace(int aID, MeetingRoom mr) throws SQLException{
        int id = getNextAutoIncrement("takesplace");

        PreparedStatement query = this.db.prepareStatement("INSERT INTO takesplace(tpID,AID,RID) VALUES(?,?,?)");
        query.setInt(1,id);
        query.setInt(2,aID);
        query.setInt(3,mr.getRoomID());
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
     * @throws SQLException
     */
    public void editAppointment(int aID, String name, String start, String end, String description, int priority, MeetingRoom mr) throws SQLException{
        PreparedStatement query = this.db.prepareStatement("UPDATE appointment SET AName = ?, Start = ?, End = ?, Description = ?, Priority = ?, DateChanged = ? WHERE AID = ?");
        query.setString(1,name);
        query.setString(2,start);
        query.setString(3,end);
        query.setString(4,description);
        query.setInt(5,priority);
        java.util.Date currentTime = new java.util.Date();
        query.setTimestamp(6,Timestamp.valueOf(GeneralUtil.dateToString(currentTime)));
        query.setInt(7,aID);
        query.executeUpdate();

        editTakesPlace(aID, mr);
    }

    /**
     * Edit where the metting takes place. Called from within editAppointment.
     * @param aID
     * @param mr
     * @throws SQLException
     */
    private void editTakesPlace(int aID, MeetingRoom mr) throws SQLException{
        PreparedStatement query = this.db.prepareStatement("UPDATE takesplace SET RID = ? WHERE AID = ?");
        query.setInt(1,mr.getRoomID());
        query.setInt(2, aID);
        query.executeUpdate();
    }

    /**
     * Returns an ArrayList containing all meeting rooms
     * @return Arraylist<MeetingRoom>
     * @throws SQLException
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
     * @throws SQLException
     */
    public void addRoom(String roomName, int capacity) throws SQLException{
        int id = getNextAutoIncrement("room");

        PreparedStatement query = this.db.prepareStatement("INSERT INTO room(RID,RName,Capacity) VALUES (?,?,?)");

        query.setInt(1,id);
        query.setString(2,roomName);
        query.setInt(3,capacity);

        query.executeUpdate();
    }

    /**
     * Creates a new group and adds it to db
     * @param GName
     * @throws SQLException
     */
    public void addGroup(String GName) throws SQLException{
         int id = getNextAutoIncrement("group");

        PreparedStatement query = this.db.prepareStatement("INSERT INTO group(GID,GName) VALUES (?,?)");
        query.setInt(1,id);
        query.setString(2,GName);
        query.executeUpdate();
    }

    /**
     * Adds a person to a group
     * @param gID
     * @param p
     * @throws SQLException
     */
    public void addMemberOfGroup(int gID, Person p) throws SQLException{
        int id = getNextAutoIncrement("memberof");

        PreparedStatement query = this.db.prepareStatement("INSERT INTO memberof(moID, GID, Username)");
        query.setInt(1,id);
        query.setInt(2,gID);
        query.setString(3,p.getUsername());
    }

    /**
     * Returns next auto increment of table
     * @param table
     * @return
     * @throws SQLException
     */
    private int getNextAutoIncrement(String table) throws SQLException{
        PreparedStatement query = this.db.prepareStatement("SHOW TABLE STATUS LIKE ?");
        query.setString(1, table);

        ResultSet rs = query.executeQuery();
        rs.next();

        return rs.getInt("Auto_increment");
    }
}
