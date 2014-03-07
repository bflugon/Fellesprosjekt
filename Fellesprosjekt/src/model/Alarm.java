package model;

import util.GeneralUtil;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: FF63
 * Date: 2/27/14
 * Time: 4:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class Alarm {

    private final int alarmID;
    private int userID;
    private int appointmentID;
    private boolean answered;
    private boolean attends;
    private String alarmTime;

    public Alarm(int alarmID, int userID, int appointmentID, Date alarmTime){
        this.alarmID = alarmID;
        this.userID = userID;
        this.appointmentID = appointmentID;
        this.answered = false;
        this.attends = false;
        this.alarmTime = GeneralUtil.dateToString(alarmTime);
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public boolean isAnswered() {
        return answered;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }

    public boolean isAttends() {
        return attends;
    }

    public void setAttends(boolean attends) {
        this.attends = attends;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public void setAlarmTime(Date alarmTime){
        this.alarmTime = GeneralUtil.dateToString(alarmTime);
    }

    public int getAlarmID(){
        return alarmID;
    }

}
