package model;

/**
 * Created with IntelliJ IDEA.
 * User: FF63
 * Date: 2/27/14
 * Time: 5:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class Person {
    private final int personID;
    private String name;
    private String email;

    public Person(int personID, String name, String email){
        this.personID = personID;
        this.name = name;
        this.email = email;
    }

    public int getPersonID(){
        return personID;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getEmail(){
        return email;
    }
}
