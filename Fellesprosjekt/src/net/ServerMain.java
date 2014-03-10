package net;

import db.DatabaseHandler;

/**
 * Created with IntelliJ IDEA.
 * User: FF63
 * Date: 3/9/14
 * Time: 8:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class ServerMain {
    public static void main(String[] args){
        Server server = new Server(8080);
        System.out.println("Server starting");
        new Thread(server).start();

    }
}
