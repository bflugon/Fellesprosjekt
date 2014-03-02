package model;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: FF63
 * Date: 2/27/14
 * Time: 4:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class Appointment {
    private final int appointmentID;
    private int ownerID;
    private Date appointmentDate;
    private int duration;
    private String description;
    private String location;

    public Appointment(int appointmentID, int ownerID, Date appointmentDate, int duration, String description, String location){
        this.appointmentID = appointmentID;
        this.ownerID = ownerID;
        this.appointmentDate = appointmentDate;
        this.duration = duration;
        this.description = description;
        this.location = location;
    }

    public int getAppointmentID() {
        return appointmentID;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation(){
        return location;
    }

    public void setLocation(String location){
        this.location = location;
    }

    public int getOwnerID(){
        return ownerID;
    }

    public void setOwnerID(int ownerID){
        this.ownerID = ownerID;
    }
}
