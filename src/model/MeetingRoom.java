package model;

/**
 * Created with IntelliJ IDEA.
 * User: FF63
 * Date: 2/28/14
 * Time: 3:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class MeetingRoom {
    private final int roomID;
    private String roomName;

    public MeetingRoom(int roomID, String roomName){
        this.roomID = roomID;
        this.roomName = roomName;
    }

    public int getRoomID(){
        return roomID;
    }

    public String getRoomName(){
        return roomName;
    }

    public void setRoomName(String roomName){
        this.roomName = roomName;
    }

}
