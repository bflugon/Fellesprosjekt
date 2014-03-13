package net;

import model.Config;
import model.Packet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: FF63
 * Date: 3/9/14
 * Time: 2:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class Client{
    protected int clientRequestPort = 8080;
    protected int clientEventPort = 8081;
    private Socket clientRequestSocket;
    private Socket clientEventSocket;
    private ObjectInputStream clientRequestInput;
    private ObjectOutputStream clientRequestOutput;
    private String ip;
    private ArrayList<PacketListener> packetListeners;

    /**
     * Constructor. Creates connection to the server.
     */
    public Client (){
        ip = Config.getIp();
        packetListeners = new ArrayList<PacketListener>();
        try{
            System.out.println("Client: Binding Socket");
            this.clientRequestSocket = new Socket(ip, clientRequestPort);
            this.clientEventSocket = new Socket(ip,clientEventPort);
        } catch (IOException e){
            e.printStackTrace();
        }

        try{
            System.out.println("Client: Creating new request Streams");
            clientRequestOutput = new ObjectOutputStream(this.clientRequestSocket.getOutputStream());
            clientRequestInput = new ObjectInputStream(this.clientRequestSocket.getInputStream());
            System.out.println("Client: Created request streams");
        } catch (IOException e){
            System.out.println("Client: Something went wrong when creating streams");
            e.printStackTrace();
        }

        new ClientEventThread(clientEventSocket,this).start();
    }

    /**
     * Sends a request packet to the server
     * @param request
     * @return
     */
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

    /**
     * Sends packets to listeners.
     * @param p
     */
    public void broadcast (Packet p){
        for (PacketListener listener : packetListeners){
            listener.packetSent(p);
        }
    }

    /**
     * PacketListener
     */
    public static interface PacketListener{
        public void packetSent(Packet p);
    }

    /**
     * Adds listener.
     * @param packetListener
     */
    public void addListener (PacketListener packetListener){
        packetListeners.add(packetListener);
    }

}
