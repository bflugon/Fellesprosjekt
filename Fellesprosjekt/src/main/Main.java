package main;

import db.DatabaseHandler;
import model.Person;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: FF63
 * Date: 2/27/14
 * Time: 4:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    /**
     * Entry point
     * @param args
     */
    public static void main(String[] args){
       Main main = new Main();
       main.testDB();

    }

    public void testDB(){

        System.out.println("Creating connection");
        DatabaseHandler db = new DatabaseHandler();

        try{

            System.out.println("Trying to authenticate with username 'Bob' and password 'hei'");
            if(db.authenticate("Bob", "hei")){
                System.out.println("Authentication successful");
            } else{
                System.out.println("Authentication failed");
            }

            System.out.println("Fetching all users and prints names");
            ArrayList<Person> persons = db.getAllPersons();

            for (Person p : persons){
                System.out.println(p.getName());
            }


        } catch(SQLException e){
            e.printStackTrace();
        }

        System.out.println("Closing db connection");
        db.close();
    }

}
