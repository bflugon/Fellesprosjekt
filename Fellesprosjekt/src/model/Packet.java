package model;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: FF63
 * Date: 3/9/14
 * Time: 7:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class Packet implements Serializable {
    private final String name;
    private final Object[] objects;

    public Packet(String name, Object... objects){
        this.name = name;
        this.objects = objects;
    }

    public String getName(){
        return name;
    }

    public Object[] getObjects(){
        return objects;
    }

}
