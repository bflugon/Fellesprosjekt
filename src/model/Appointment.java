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
    private int appointmentID;
    private Date appointmentDate;
    private int duration;
    private String description;

    public Appointment(int appointmentID, Date appointmentDate, int duration, String description){
        this.setAppointmentID(appointmentID);
        this.setAppointmentDate(appointmentDate);
        this.setDuration(duration);
        this.setDescription(description);
    }

    public int getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
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
}
