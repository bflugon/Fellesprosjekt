package model;

/**
 * Created with IntelliJ IDEA.
 * User: FF63
 * Date: 2/27/14
 * Time: 4:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class Alarm {

    private final int alarmID;
    private int alarmFlag;

    public Alarm(int alarmID, int alarmFlag){
        this.alarmID = alarmID;
        this.alarmFlag = alarmFlag;
    }

    public int getAlarmID(){
        return alarmID;
    }

    public int getAlarmFlags(){
        return alarmFlag;
    }

    public void setAlarmFlags(int alarmFlags){
        this.alarmFlag = alarmFlags;
    }
}
