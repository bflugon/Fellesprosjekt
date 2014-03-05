package main;

import db.DatabaseHandler;
import model.*;

import java.sql.SQLException;
import java.util.ArrayList;

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
    private ArrayList<Alarm> alarms;
    private ArrayList<MeetingRoom> rooms;
    private ArrayList<Group> groups;

    public Register(DatabaseHandler handler){
        this.mHandler = handler;

        try{
            this.persons = handler.getAllPersons();
        } catch (SQLException e){
            this.persons = new ArrayList<Person>();
        }

        try{
            this.appointments = handler.getAllAppointments();
        } catch (SQLException e){
            this.appointments = new ArrayList<Appointment>();
        }
    }
}
