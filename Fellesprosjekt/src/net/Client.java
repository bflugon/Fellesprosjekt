package net;

import main.Register;
import model.Packet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: FF63
 * Date: 3/9/14
 * Time: 2:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class Client {
    protected int clientEventPort = 8080;
    protected int clientRequestPort = 8080;
    private Socket clientEventSocket;
    private Socket clientRequestSocket;
    private ObjectInputStream clientEventInput;
    private ObjectOutputStream clientEventOutput;
    private ObjectInputStream clientRequestInput;
    private ObjectOutputStream clientRequestOutput;
    private Register register;

    public Client (String ip){
        this.register = register;

        try{
            System.out.println("Binding Socket");
            //this.clientEventSocket = new Socket(ip, clientEventPort);
            this.clientRequestSocket = new Socket(ip, clientRequestPort);
        } catch (IOException e){
            e.printStackTrace();
        }

        try{
            System.out.println("Creating new Streams");
            //clientEventInput = new ObjectInputStream(this.clientEventSocket.getInputStream());
            //clientEventOutput = new ObjectOutputStream(this.clientEventSocket.getOutputStream());
            clientRequestOutput = new ObjectOutputStream(this.clientRequestSocket.getOutputStream());
            clientRequestOutput.flush();
            clientRequestInput = new ObjectInputStream(this.clientRequestSocket.getInputStream());
            System.out.println("Created new streams");
        } catch (IOException e){
            System.out.println("Something went wrong when creating streams");
            e.printStackTrace();
        }
    }

    public Packet request(Packet request){
        Packet response;
        try{
            this.clientRequestOutput.writeObject(request);
            response = (Packet) this.clientRequestInput.readObject();
            return response;
        } catch (Exception e){
            e.printStackTrace();
            return (new Packet("ERROR", e));
        }
    }
}
