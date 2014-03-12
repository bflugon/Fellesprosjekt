package net;

import model.Packet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: FF63
 * Date: 3/12/14
 * Time: 5:44 AM
 * To change this template use File | Settings | File Templates.
 */
public class ServerEventThread extends Thread{
    Socket clientSocket;
    ObjectInputStream input;
    ObjectOutputStream output;
    Server server;

    /**
     * Server event thread constructor
     * @param clientSocket
     * @param server
     */
    public ServerEventThread(Socket clientSocket, Server server){
        System.out.println("Server: Making event thread");
        try{
            this.clientSocket = clientSocket;
            this.server = server;
            System.out.println("Server: Creating Streams");
            this.output = new ObjectOutputStream(this.clientSocket.getOutputStream());
            this.input = new ObjectInputStream(this.clientSocket.getInputStream());
            System.out.println("Server: Made event thread");
        } catch(IOException e){
            System.out.println("Server: Something went wrong");
            e.printStackTrace();
        }
    }

    /**
     * Keeps checking for input
     */
    @Override
    public void run(){
        while(server.running){
            try{
                this.input.readObject();
            }catch(IOException | ClassNotFoundException e){
                break;
            }
        }
    }

    /**
     * Broadcasts packet to connected clients
     * @param p
     */
    public void broadcast(Packet p){
        try{
            this.output.writeObject(p);
        } catch(IOException e){
            //Do nothing
        }
    }
}
