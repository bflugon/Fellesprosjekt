package net;

import model.Packet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: FF63
 * Date: 3/9/14
 * Time: 4:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class RequestThread extends Thread{
    Socket clientSocket;
    ServerRequest serverRequest;
    ObjectInputStream input;
    ObjectOutputStream output;
    Server server;

    public RequestThread(Socket clientSocket, Server server){
        System.out.println("Server: Making request thread");
        try{
            this.clientSocket = clientSocket;
            this.server = server;
            serverRequest = new ServerRequest();
            System.out.println("Server: Creating Streams");
            this.output = new ObjectOutputStream(this.clientSocket.getOutputStream());
            this.input = new ObjectInputStream(this.clientSocket.getInputStream());
            System.out.println("Server: Made request thread");
        } catch(IOException e){
            System.out.println("Server: Something went wrong");
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        while(server.running){
            try{
                Packet packet = (Packet) this.input.readObject();
                Packet response = serverRequest.getResponse(packet);
                this.output.writeObject(response);
            }catch(IOException | ClassNotFoundException e){
                break;
            }
        }
    }
}
