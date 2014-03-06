package main;

import db.DatabaseHandler;
import model.*;
import util.GeneralUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

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

        try{
            this.rooms = handler.getAllRooms();
        } catch (SQLException e){
            this.rooms = new ArrayList<MeetingRoom>();
        }
    }

    public ArrayList<Appointment> getAppointments(){
        return appointments;
    }

    public Appointment getAppointment(int AID){
        for (Appointment a : appointments){
            if(a.getAppointmentID() == AID){
                return a;
            }
        }
        return null;
    }

    public ArrayList<Person> getPersons(){
        return persons;
    }

    public Person getPersonByUsername(String username){
        for(Person p : persons){
            if (p.getUsername().equals(username)){
                return p;
            }
        }
        return null;
    }

    public ArrayList<MeetingRoom> getRooms(){
        return rooms;
    }

    public MeetingRoom getRoom(int roomID){
        for(MeetingRoom mr : rooms){
            if (roomID == mr.getRoomID()){
                return mr;
            }
        }
        return null;
    }

}
