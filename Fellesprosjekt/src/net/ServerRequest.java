package net;

import db.DatabaseHandler;
import model.*;

import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: FF63
 * Date: 3/9/14
 * Time: 3:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class ServerRequest {
    private final DatabaseHandler db;

    public ServerRequest(){
        this.db = new DatabaseHandler();
    }

    public Packet getResponse(Packet request){
        try{
            String name = request.getName().toUpperCase();
            Object[] objects = request.getObjects();
            if(name.equals("GET_APPOINTMENTS")){
                return getAllAppointments();
            }else if(name.equals("GET_ALL_PEOPLE")){
                return getAllPersons();
            }else if(name.equals("AUTHENTICATE")){
                return authenticate((String)objects[0],(String) objects[1]);
            } else if(name.equals("CREATE_ACCOUNT")){
                return createAccount((String) objects[0], (String) objects[1], (String) objects[2], (String) objects[3]);
            } else if(name.equals("GET_ALL_ALARMS")){
                return getAllAlarms();
            } else if(name.equals("GET_ALL_GROUPS")){
                return getAllGroups();
            } else if(name.equals("GET_ALL_GROUPMEMBERS")){
                return getAllGroupMembers();
            } else if(name.equals("GET_ALL_ROOMS")){
                return getAllRooms();
            } else if(name.equals("ADD_APPOINTMENT")){
                return addAppointment((Appointment) objects[0], (MeetingRoom) objects[1]);
            } else if(name.equals("EDIT_APPOINTMENT")){
                return editAppointment((Appointment) objects[0], (MeetingRoom) objects[1]);
            } else if(name.equals("DELETE_APPOINTMENT")){
                return deleteAppointment((Appointment) objects[0]);
            } else if(name.equals("ADD_ROOM")){
                return addRoom((String) objects[0], (Integer) objects[1]);
            } else if(name.equals("ADD_GROUP")){
                return addGroup((String) objects[0]);
            } else if (name.equals("ADD_PERSON_TO_GROUP")){
                return addPersonToGroup((Group) objects[0], (Person) objects[1]);
            } else if (name.equals("ADD_ALARM")){
                return addAlarm((String) objects[0], (Integer) objects[1], (Integer) objects[2],(String) objects[3],(Integer) objects[4]);
            } else if (name.equals("UPDATE_ATTENDING")){
                return updateAttending((Integer) objects[0], (Integer) objects[1]);
            }
            //Create a shit ton of if statements
            return new Packet("ERROR");
        }catch(Exception e){
            e.printStackTrace();
            return new Packet("ERROR", e);
        }
    }

    private Packet authenticate(String username, String password) throws SQLException{
        return new Packet("AUTHENTICATION",db.authenticate(username, password));
    }

    private Packet createAccount(String user, String pass, String name, String email) throws SQLException{
        return new Packet("ACCOUNT_CREATED",db.createAccount(user,pass,name,email));
    }

    private Packet addAppointment(Appointment a, MeetingRoom mr) throws SQLException{
        return new Packet("APPOINTMENT_ADDED", db.addAppointment(a.getAppointmentName(), a.getAppointmentStart(), a.getAppointmentEnd(), a.getDescription(), a.getPriority(), a.getOwnerName(), mr));
    }

    private Packet editAppointment(Appointment a, MeetingRoom mr) throws SQLException{
        db.editAppointment(a.getAppointmentID(),a.getAppointmentName(),a.getAppointmentStart(),a.getAppointmentEnd(),a.getDescription(),a.getPriority(),mr);
        return new Packet("APPOINTMENT_EDITED");
    }

    private Packet deleteAppointment(Appointment a) throws SQLException{
        db.deleteAppointment(a.getAppointmentID());
        return new Packet("APPOINTMENT_DELETED");
    }

    private Packet getAllAppointments() throws SQLException{
        return new Packet("ALL_APPOINTMENTS", this.db.getAllAppointments());
    }

    private Packet getAllPersons() throws SQLException{
        return new Packet("ALL_PERSONS", this.db.getAllPersons());
    }

    private Packet getAllAlarms() throws  SQLException{
        return new Packet("ALL_ALARMS", this.db.getAllAlarms());
    }

    private Packet addAlarm(String username, int appointmentID, int hasAlarm, String alarmTime, int attending) throws SQLException{
        return new Packet("ALARM_ADDED", this.db.addAlarm(username, appointmentID, hasAlarm, alarmTime,attending));
    }

    private Packet updateAttending(int alarmID, int attending) throws SQLException{
        this.db.updateAttending(alarmID, attending);
        return new Packet("ATTENDING_UPDATED");
    }

    private Packet getAllGroups() throws SQLException{
        return new Packet("ALL_GROUPS", this.db.getAllGroups());
    }

    private Packet addGroup(String gName) throws SQLException{
        return new Packet("GROUP_ADDED", this.db.addGroup(gName));
    }

    private Packet getAllGroupMembers() throws SQLException{
        return new Packet("ALL_GROUP_MEMBERS", this.db.getAllMembersOfGroups());
    }

    private Packet addPersonToGroup(Group g, Person p) throws SQLException{
        this.db.addPersonToGroup(g.getGroupID(),p);
        return new Packet("PERSON_ADDED_TO_GROUP");
    }

    private Packet getAllRooms() throws SQLException{
        return new Packet("ALL_ROOMS", this.db.getAllRooms());
    }

    private Packet addRoom(String name, int capacity) throws SQLException{
        return new Packet("ROOM_ADDED", this.db.addRoom(name,capacity));
    }
}
