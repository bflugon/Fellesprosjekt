package model;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: FF63
 * Date: 2/27/14
 * Time: 5:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class Group {
    private final int groupID;
    private ArrayList<Person> members;
    private String groupName;

    public Group(int groupID, String groupName){
        this.groupID = groupID;
        members = new ArrayList<Person>();
        this.groupName = groupName;
    }

    public Group(int groupID, String groupName, ArrayList<Person> members){
        this.groupID = groupID;
        this.members = members;
        this.groupName = groupName;
    }

    public void addPerson(Person p){
        if (!members.contains(p)){
            members.add(p);
        }
    }

    public void removePerson(Person p){
        if (members.contains(p)){
            members.remove(p);
        }
    }

    public ArrayList<Person> getMembers(){
        return members;
    }

    public void setMembers(ArrayList<Person> members){
        this.members = members;
    }

    public String getGroupName(){
        return groupName;
    }

    public void setGroupName(String groupName){
        this.groupName = groupName;
    }
}
