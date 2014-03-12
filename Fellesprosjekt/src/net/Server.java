package net;

import db.DatabaseHandler;
import model.Packet;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: FF63
 * Date: 3/9/14
 * Time: 2:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class Server implements Runnable {
    protected int serverPort = 8080;
    protected int eventPort = 8081;
    protected ServerSocket serverSocket = null;
    protected ServerSocket eventSocket = null;
    protected boolean running = true;
    protected Thread runningThread = null;
    protected DatabaseHandler db;
    protected ArrayList<ServerEventThread> serverEventThreads;

    /**
     * Constructor
     * @param serverPort
     * @param eventPort
     */
    public Server(int serverPort, int eventPort){
        this.serverPort = serverPort;
        this.eventPort = eventPort;
        db = new DatabaseHandler(this);
        serverEventThreads = new ArrayList<ServerEventThread>();
    }

    /**
     * Keeps checking for new clients.
     */
    public void run(){
        synchronized (this){
            this.runningThread = Thread.currentThread();
        }
        openServerSocket();
        while (running){
            Socket clientSocket;
            Socket eventSocket;
            try{
                clientSocket = this.serverSocket.accept();
                eventSocket = this.eventSocket.accept();
            }
            catch (IOException e){
                if (!running){
                    System.out.println("Server is stopped");
                    return;
                }
                throw new RuntimeException("ERROR: Couldn't accept client connection",e);
            }
            new ServerRequestThread(clientSocket, this,db).start();
            ServerEventThread serverEventThread = new ServerEventThread(eventSocket,this);
            serverEventThreads.add(serverEventThread);
            serverEventThread.start();
        }

    }

    /**
     * Opens server sockets
     */
    private void openServerSocket(){
        try{
            this.serverSocket = new ServerSocket(this.serverPort);
            this.eventSocket = new ServerSocket(this.eventPort);
        }
        catch (IOException e){
            throw new RuntimeException("Cannot open port " + this.serverPort, e);
        }
    }

    /**
     * Broadcasts packet to all connected clients
     * @param p
     */
    public void broadcast(Packet p){
        for(ServerEventThread set : serverEventThreads){
            set.broadcast(p);
        }
    }
}
