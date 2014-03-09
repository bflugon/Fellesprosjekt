package net;

import db.DatabaseHandler;
import model.Packet;

import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: FF63
 * Date: 3/9/14
 * Time: 3:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class ServerRequest {
    private Server server;
    private final DatabaseHandler db;

    public ServerRequest(Server server){
        this.server = server;
        this.db = new DatabaseHandler();
    }

    public Packet getResponse(Packet request){
        try{
            String name = request.getName().toUpperCase();
            Object[] objects = request.getObjects();
            if(name.equals("GET_APPOINTMENTS")){
                return getAllAppointments();
            }
            return new Packet("ERROR");
        }catch(Exception e){
            e.printStackTrace();
            return new Packet("ERROR", e);
        }
    }

    private Packet getAllAppointments() throws SQLException{
        return new Packet("ALL_APPOINTMENTS", this.db.getAllAppointments());
    }
}
