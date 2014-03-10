package net;

import db.DatabaseHandler;
import main.Register;
import model.Appointment;
import model.Packet;
import model.Person;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: FF63
 * Date: 3/9/14
 * Time: 8:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClientTest {
    public static void main (String[] args){
        Client client = new Client("localhost");
        System.out.println("Trying to get appointments");
        Packet packet = client.request(new Packet("GET_APPOINTMENTS"));
        System.out.println("Got appointments");
        if (packet.getName().equals("ALL_APPOINTMENTS")){
            System.out.println("Iterating over appointments");
            ArrayList<Appointment> appointments = (ArrayList<Appointment>) packet.getObjects()[0];
            for (Appointment a : appointments){
                System.out.println(a.toString());
            }
        }else{
            System.out.println("Get name didn't equal ALL_APPOINTMENTS");
        }

        packet = client.request(new Packet("GET_ALL_PERSONS"));
        ArrayList<Person> persons = (ArrayList<Person>)packet.getObjects()[0];
        for (Person p : persons){
            System.out.println(p.toString());
        }
    }
}
