package net;

import model.Packet;

import java.io.*;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: FF63
 * Date: 3/9/14
 * Time: 4:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class RequestThread extends Thread implements Runnable {
    Socket clientSocket;
    Server server;
    ServerRequest serverRequest;
    ObjectInputStream input;
    ObjectOutputStream output;



    public RequestThread(Socket clientSocket, Server server){
        System.out.println("Making request thread");
        try{
            this.clientSocket = clientSocket;
            this.server = server;
            serverRequest = new ServerRequest(server);
            System.out.println("Creating Streams");
            this.output = new ObjectOutputStream(this.clientSocket.getOutputStream());
            this.output.flush();
            this.input = new ObjectInputStream(this.clientSocket.getInputStream());
            System.out.println("Made request thread");
        } catch(IOException e){
            System.out.println("Something went wrong");
            e.printStackTrace();
        }
    }

    public void run(){
        try{
            Packet packet = (Packet) this.input.readObject();
            Packet response = serverRequest.getResponse(packet);
            this.output.writeObject(response);
        }catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
