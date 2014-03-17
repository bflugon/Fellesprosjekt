package model;

import java.util.ArrayList;

/**
 * Created by Kristian on 17.03.14.
 */
public class Calendar extends java.util.Observable{

    public ArrayList<Person>  persons = new ArrayList<Person>();

    public void addPersonToCalendar(Person person) {
        persons.add(person);
        notifyObservers(persons);
    }
}
