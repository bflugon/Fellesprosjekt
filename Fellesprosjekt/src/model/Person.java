package model;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: FF63
 * Date: 2/27/14
 * Time: 5:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class Person implements Serializable {
    private final String username;
    private String name;
    private String email;

    public Person(String username, String name, String email){
        this.username = username;
        this.name = name;
        this.email = email;
    }

    public String getUsername(){
        return username;
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

    @Override
    public String toString() {
        return "Person{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
