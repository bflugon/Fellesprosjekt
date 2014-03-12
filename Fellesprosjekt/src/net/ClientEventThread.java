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
 * Time: 5:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class ClientEventThread extends Thread {

    Socket clientSocket;
    ObjectInputStream input;
    ObjectOutputStream output;
    Client client;
    boolean running;

    public ClientEventThread(Socket clientSocket, Client client){
        System.out.println("Client: Making event thread");
        try{
            this.clientSocket = clientSocket;
            this.client = client;
            System.out.println("Client: Creating event streams");
            this.output = new ObjectOutputStream(this.clientSocket.getOutputStream());
            this.input = new ObjectInputStream(this.clientSocket.getInputStream());
            System.out.println("Client: Made event thread");
        } catch(IOException e){
            System.out.println("Client: Something went wrong");
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        this.running = true;
        while(running){
            try{
                Packet packet = (Packet) this.input.readObject();
                System.out.println(packet.getName());
                this.client.broadcast(packet);
            }catch(IOException | ClassNotFoundException e){
                break;
            }
        }
    }
}
