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
    private final MailClient mailClient;


    public ServerRequest(DatabaseHandler db){
        this.db = db;
        this.mailClient = new MailClient(db);
    }

    /**
     * Handles requests from clients and gets response.
     * @param request
     * @return
     */
    public Packet getResponse(Packet request){
        try{
            String name = request.getName().toUpperCase();
            Object[] objects = request.getObjects();
            if(name.equals("CONNECTING")){
                db.connectToDatabase();
            }else if(name.equals("GET_APPOINTMENTS")){
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
                return addAppointment((String) objects[0], (String) objects[1], (String) objects[2], (String) objects[3], (int) objects[4], (String) objects[5], (MeetingRoom) objects[6], (String) objects[7]);
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
                return updateAttending((Integer) objects[0],(String) objects[1], (Integer) objects[2]);
            } else if (name.equals("SEND_EMAIL")){
                return sendEmail((String) objects[0], (Appointment) objects[1]);
            } else if (name.equals("INVITE_PERSON_APPOINTMENT")){
                return invitePerson((Person) objects[0], (Appointment) objects[1]);
            } else if (name.equals("GET_USER_APPOINTMENTS")){
                return getUserAppointment((String) objects[0]);
            } else if (name.equals("GET_INVITEES")){
                return getInvitees((Integer) objects[0]);
            } else if (name.equals("GET_PEOPLE_ATTENDING_APP")){
                return getAttending((Integer) objects[0]);
            } else if (name.equals("GET_APP_A_U")){
                return getAppointmentsAttendingForUsername((String) objects[0]);
            } else if (name.equals("GET_APP_NA_U")){
                return getAppointmentsNotAttendingForUsername((String) objects[0]);
            } else if (name.equals("GET_APP_C_U")){
                return getAppointmentsCreatedByUsername((String) objects[0]);
            } else if (name.equals("GET_DC_A")){
                return getDateChangedForAppointment((Integer) objects[0]);
            } else if (name.equals("GET_NAP")){
                return getNotAttendingPeople((Integer) objects[0]);
            } else if (name.equals("SET_LOGIN_DATE")){
                return updateLogin((String) objects[0]);
            } else if (name.equals("GET_ROOM_APP")){
                return getRoomAppointments((Integer) objects[0]);
            }

            return new Packet("ERROR");
        }catch(Exception e){
            e.printStackTrace();
            return new Packet("ERROR", e);
        }
    }



    /**
     * All of these methods sends the information to the database, and returns
     * a packet containing information and/or confirmation.
     **/
    private Packet authenticate(String username, String password) throws SQLException{
        return new Packet("AUTHENTICATION",db.authenticate(username, password));
    }

    private Packet createAccount(String user, String pass, String name, String email) throws SQLException{
        return new Packet("ACCOUNT_CREATED",db.createAccount(user,pass,name,email));
    }

    private Packet addAppointment(String name, String start, String end, String description, int priority, String username, MeetingRoom mr, String altLoc) throws SQLException{
        return new Packet("APPOINTMENT_ADDED", db.addAppointment(name,start,end,description,priority,username,mr,altLoc));
    }

    private Packet editAppointment(Appointment a, MeetingRoom mr) throws SQLException{
        db.editAppointment(a.getAppointmentID(),a.getAppointmentName(),a.getAppointmentStart(),a.getAppointmentEnd(),a.getDescription(),a.getPriority(),mr,a.getAlternativeLocation());
        return new Packet("APPOINTMENT_EDITED");
    }

    private Packet deleteAppointment(Appointment a) throws SQLException{
        db.deleteAppointment(a.getAppointmentID());
        return new Packet("APPOINTMENT_DELETED");
    }

    private Packet getUserAppointment(String username) throws SQLException{
        return new Packet("USER_APPOINTMENT",this.db.getUserAppointments(username));
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

    private Packet getAttending(int appointmentID) throws SQLException{
        return new Packet("PEOPLE_ATTENDING_APP", this.db.getAttending(appointmentID));
    }

    private Packet invitePerson(Person p, Appointment a) throws SQLException{
        if(this.db.invitePerson(p,a)){
            return new Packet("PERSON_INVITED");
        }
        return new Packet("ERROR");
    }

    private Packet addAlarm(String username, int appointmentID, int hasAlarm, String alarmTime, int attending) throws SQLException{
        if (this.db.addAlarm(username, appointmentID, hasAlarm, alarmTime,attending)){
            return new Packet("ALARM_ADDED");
        }
        return new Packet("ERROR");
    }

    private Packet updateAttending(int appointmentID,String username, int attending) throws SQLException{
        this.db.updateAttending(appointmentID, username, attending);
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

    private Packet getInvitees(int appointmentID) throws SQLException{
        return new Packet("INVITEES", this.db.getInvitees(appointmentID));
    }


    private Packet updateLogin(String username) throws SQLException{
        return new Packet("SET_LOGIN_DATE",db.updateLastLogin(username));
    }

    private Packet getNotAttendingPeople(int aID) throws SQLException{
        return new Packet("GOT_NAP",db.getNotAttending(aID));
    }

    private Packet getDateChangedForAppointment(int aID) throws SQLException{
        return new Packet("GOT_DC_A",db.getDateChangedForAppointment(aID));
    }

    private Packet getAppointmentsCreatedByUsername(String username) throws SQLException{
        return new Packet("GOT_APP_C_U",db.getAppointmentsCreatedByUser(username));
    }

    private Packet getAppointmentsNotAttendingForUsername(String username) throws SQLException{
        return new Packet("GOT_APP_NA_U",db.getAppointmentsAttendingByUsername(username,0));
    }

    private Packet getAppointmentsAttendingForUsername(String username) throws SQLException{
        return new Packet("GOT_APP_A_U",db.getAppointmentsAttendingByUsername(username,1));
    }

    private Packet getRoomAppointments(int roomID) throws SQLException{
        return new Packet("ROOM_APPOINTMENTS", db.getRoomAppointments(roomID));
    }

    private Packet sendEmail(String recipient, Appointment appointment){
        if(mailClient.sendEmail(recipient,appointment)){
            return new Packet("MAIL_SENT");
        }else{
            return new Packet("ERROR");
        }
    }
}
