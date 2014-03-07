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
    private String username;
    private int attends;
    private String alarmTime;

    public Alarm(int alarmID, String username, Date alarmTime, int attends){
        this.alarmID = alarmID;
        this.username = username;
        this.alarmTime = GeneralUtil.dateToString(alarmTime);
        this.attends = attends;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int isAttends() {
        return attends;
    }

    public void setAttends(int attends) {
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
