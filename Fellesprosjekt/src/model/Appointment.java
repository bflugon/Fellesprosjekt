package model;

import util.GeneralUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: FF63
 * Date: 2/27/14
 * Time: 4:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class Appointment implements Serializable {
    private final int appointmentID;
    private String ownerName;
    private String appointmentName;
    private Date appointmentStart;
    private Date appointmentEnd;
    private String description;
    private MeetingRoom meetingRoom;
    private int priority;
    private Date createdDate;
    private String alternativeLocation;


    public Appointment(int appointmentID, String ownerName, String appointmentName, Date appointmentStart, Date appointmentEnd, int priority, String description, Date createdDate, MeetingRoom mr, String alternativeLocation){
        this.appointmentID = appointmentID;
        this.ownerName = ownerName;
        this.appointmentName = appointmentName;
        this.appointmentStart = appointmentStart;
        this.appointmentEnd = appointmentEnd;
        this.priority = priority;
        this.description = description;
        this.meetingRoom = mr;
        this.createdDate = createdDate;
        this.alternativeLocation = alternativeLocation;
    }

    public int getAppointmentID() {
        return appointmentID;
    }

    public String getAppointmentStart(){
        return GeneralUtil.dateToString(appointmentStart);
    }

    public String getAppointmentEnd(){
        return GeneralUtil.dateToString(appointmentEnd);
    }

    public void setAppointmentStart(Date appointmentStart){
        this.appointmentStart = appointmentStart;
    }

    public void setAppointmentEnd(Date appointmentEnd){
        this.appointmentEnd = appointmentEnd;
    }

    public void setAlternativeLocation(String alternativeLocation){
        this.alternativeLocation = alternativeLocation;
    }

    public String getAlternativeLocation(){
        return alternativeLocation;
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

    public MeetingRoom getRoom(){
        return meetingRoom;
    }

    public String getOwnerName(){
        return ownerName;
    }

    public void setOwnerName(String ownerName){
        this.ownerName = ownerName;
    }

    public String getCreatedDate() {
        return GeneralUtil.dateToString(createdDate);
    }

    public Appointment getAppointment(){
        return this;
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
                ", priority=" + priority +
                '}';
    }

}
