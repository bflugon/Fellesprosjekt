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
    private String ownerName;
    private String appointmentName;
    private Date appointmentStart;
    private Date appointmentEnd;
    private String description;
    private String location;
    private int priority;

    public Appointment(int appointmentID, String ownerName, String appointmentName, Date appointmentStart, Date appointmentEnd, int priority, String description, String location){
        this.appointmentID = appointmentID;
        this.ownerName = ownerName;
        this.appointmentName = appointmentName;
        this.appointmentStart = appointmentStart;
        this.appointmentEnd = appointmentEnd;
        this.priority = priority;
        this.description = description;
        this.location = location;
    }

    public int getAppointmentID() {
        return appointmentID;
    }

    public Date getAppointmentStart(){
        return appointmentStart;
    }

    public Date getAppointmentEnd(){
        return appointmentEnd;
    }

    public void setAppointmentStart(Date appointmentStart){
        this.appointmentStart = appointmentStart;
    }

    public void setAppointmentEnd(Date appointmentEnd){
        this.appointmentEnd = appointmentEnd;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getAppointmentName() {

        return appointmentName;
    }

    public void setAppointmentName(String appointmentName) {
        this.appointmentName = appointmentName;
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

    public String getOwnerName(){
        return ownerName;
    }

    public void setOwnerName(String ownerName){
        this.ownerName = ownerName;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentID=" + appointmentID +
                ", ownerName='" + ownerName + '\'' +
                ", appointmentName='" + appointmentName + '\'' +
                ", appointmentStart=" + appointmentStart +
                ", appointmentEnd=" + appointmentEnd +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", priority=" + priority +
                '}';
    }
}
