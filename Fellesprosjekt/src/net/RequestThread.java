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
    ServerRequest serverRequest;
    ObjectInputStream input;
    ObjectOutputStream output;



    public RequestThread(Socket clientSocket){
        System.out.println("Server: Making request thread");
        try{
            this.clientSocket = clientSocket;
            serverRequest = new ServerRequest();
            System.out.println("Server: Creating Streams");
            this.output = new ObjectOutputStream(this.clientSocket.getOutputStream());
            this.output.flush();
            this.input = new ObjectInputStream(this.clientSocket.getInputStream());
            System.out.println("Server: Made request thread");
        } catch(IOException e){
            System.out.println("Server: Something went wrong");
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
