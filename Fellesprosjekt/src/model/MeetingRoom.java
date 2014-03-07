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
    private int capacity;

    public MeetingRoom(int roomID, String roomName, int capacity){
        this.roomID = roomID;
        this.roomName = roomName;
        this.capacity = capacity;
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

    public int getCapacity() {
        return capacity;
    }


    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return "MeetingRoom{" +
                "roomID=" + roomID +
                ", roomName='" + roomName + '\'' +
                ", capacity=" + capacity +
                '}';
    }
}
