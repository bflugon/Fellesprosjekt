package model;

/**
 * Created with IntelliJ IDEA.
 * User: FF63
 * Date: 2/27/14
 * Time: 4:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class Alarm {

    private int alarmID;
    private int alarmFlags;

    public Alarm(int alarmID, int alarmFlags){
        this.alarmID = alarmID;
        this.alarmFlags = alarmFlags;
    }

    public int getAlarmID(){
        return alarmID;
    }

    public void setAlarmID(int alarmID){
        this.alarmID = alarmID;
    }

    public int getAlarmFlags(){
        return alarmFlags;
    }

    public void setAlarmFlags(int alarmFlags){
        this.alarmFlags = alarmFlags;

    }
}
