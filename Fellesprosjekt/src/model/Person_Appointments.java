package model;

import java.util.ArrayList;

/**
 * Created by Kristian on 17.03.14.
 */
public class Person_Appointments {

    private Person person;
    private ArrayList<Appointment> appointments;

    public Person_Appointments(Person person, ArrayList<Appointment> appointments) {
        this.person = person;
        this.appointments = appointments;
    }

    public Person getPerson () {
        return person;
    }

    public ArrayList<Appointment> getAppointments () {
        return appointments;
    }
}
