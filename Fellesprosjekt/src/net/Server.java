package net;

import db.DatabaseHandler;
import model.Packet;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: FF63
 * Date: 3/9/14
 * Time: 2:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class Server{

    private static Server singleton;

    protected int serverPort = 8080;
    protected int eventPort = 8081;
    protected boolean running = false;
    protected DatabaseHandler db;
    protected final LinkedList<ServerEventThread> serverEventThreads;

    /**
     * Constructor
     * @param serverPort
     * @param eventPort
     */
    public Server(int serverPort, int eventPort){
        this.serverPort = serverPort;
        this.eventPort = eventPort;
        db = new DatabaseHandler(this);
        serverEventThreads = new LinkedList<ServerEventThread>();

        singleton = this;

        try{
            final ServerSocket serverAcceptor = new ServerSocket(this.serverPort);
            final ServerSocket eventAcceptor = new ServerSocket(this.eventPort);
            this.running = true;

            new Thread(){
                public void run(){
                    while(running){
                        try{
                            Socket serverSocket = serverAcceptor.accept();

                            addRequest(serverSocket);

                        } catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            }.start();

            new Thread(){
                public void run(){
                    while(running){
                        try{
                            Socket eventSocket = eventAcceptor.accept();
                            addEvent(eventSocket);
                        }catch(IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            }.start();

        }catch (IOException e){
            throw new RuntimeException("Cannot open port " + this.serverPort, e);
        }
    }

    /**
     * Add request
     * @param serverSocket
     */
    public void addRequest(Socket serverSocket){
        new ServerRequestThread(serverSocket,this,db).start();
    }

    /**
     * Add event
     * @param eventSocket
     */
    public void addEvent(Socket eventSocket){
        synchronized (serverEventThreads){
            ServerEventThread serverEventThread = new ServerEventThread(eventSocket,this);
            serverEventThreads.add(serverEventThread);
            serverEventThread.start();
        }
    }

    /**
     * Removes thread
     * @param serverEventThread
     */
    public static void removeEventThread(ServerEventThread serverEventThread){
        synchronized (singleton.serverEventThreads){
            singleton.serverEventThreads.remove(serverEventThread);
        }
    }

    /**
     * Broadcasts packet to all connected clients
     * @param p
     */
    public static void broadcast(Packet p){
        synchronized (singleton.serverEventThreads){
            for(ServerEventThread set : singleton.serverEventThreads){
                set.broadcast(p);
            }
        }
    }
}
