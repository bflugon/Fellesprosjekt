package db;

import model.Appointment;
import model.Person;

import java.sql.*;
import java.util.ArrayList;

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
            query.setString(1,username);
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
     * @return an ArrayList containing every person.
     * @throws SQLException
     */
    public ArrayList<Person> getAllPersons() throws SQLException{
        PreparedStatement query = this.db.prepareStatement("SELECT * FROM person");
        ResultSet rs = query.executeQuery();

        if (!rs.next()){
            return null;
        }

        ArrayList<Person> results = new ArrayList<Person>();
        results.add(new Person(rs.getString("Username"),rs.getString("Name"), rs.getString("Email")));

        while(rs.next()){
            results.add(new Person(rs.getString("Username"),rs.getString("Name"), rs.getString("Email")));
        }

        return results;
    }

    public ArrayList<Appointment> getAllAppointments() throws SQLException{
        PreparedStatement query = this.db.prepareStatement("SELECT * FROM appointment");
        ResultSet rs = query.executeQuery();

        if (!rs.next()){
            return null;
        }

        ArrayList<Appointment> results = new ArrayList<Appointment>();
        //results.add(new Appointment(rs.getInt("AID"),OWNERID, APPOINTMENTDATE, DURATION, DESCRIPTION, LOCATION));

        return results;
    }
}
