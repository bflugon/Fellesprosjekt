package model;

import java.util.ArrayList;

/**
 * Created by Kristian on 17.03.14.
 */
public class Calendar extends java.util.Observable{

    public ArrayList<Person_Appointments> calendar = new ArrayList<>();

    public void addAppointment(Person_Appointments pa) {
        if ( pa != null ) {
            calendar.add(pa);
            setChanged();
            notifyObservers(calendar);
        }
    }

    public void addAppointments(ArrayList<Person_Appointments> calendars) {
        if ( calendars != null ) {
            for (Person_Appointments pa : calendars) {
                if ( pa != null ) calendar.add(pa);
            }
            setChanged();
            notifyObservers(calendar);
        }

    }
}
