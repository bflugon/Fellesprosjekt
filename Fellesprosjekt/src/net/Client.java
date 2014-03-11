package net;

import model.Packet;
import util.GeneralUtil;

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
    protected int clientRequestPort = 8080;
    private Socket clientRequestSocket;
    private ObjectInputStream clientRequestInput;
    private ObjectOutputStream clientRequestOutput;
    private String ip;

    public Client (){
        ip = GeneralUtil.readFile("ip.txt").get(0);
        try{
            System.out.println("Client: Binding Socket");
            this.clientRequestSocket = new Socket(ip, clientRequestPort);
        } catch (IOException e){
            e.printStackTrace();
        }

        try{
            System.out.println("Client: Creating new Streams");
            clientRequestOutput = new ObjectOutputStream(this.clientRequestSocket.getOutputStream());
            clientRequestInput = new ObjectInputStream(this.clientRequestSocket.getInputStream());
            System.out.println("Client: Created new streams");
        } catch (IOException e){
            System.out.println("Client: Something went wrong when creating streams");
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
