package net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: FF63
 * Date: 3/9/14
 * Time: 2:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class Server implements Runnable {
    protected int serverPort = 8080;
    protected ServerSocket serverSocket = null;
    protected boolean running = true;
    protected Thread runningThread = null;

    public Server(int serverPort){
        this.serverPort = serverPort;
    }

    public void run(){
        synchronized (this){
            this.runningThread = Thread.currentThread();
        }
        openServerSocket();
        while (running){
            Socket clientSocket;
            try{
                clientSocket = this.serverSocket.accept();
            }
            catch (IOException e){
                if (!running){
                    System.out.println("Server is stopped");
                    return;
                }
                throw new RuntimeException("ERROR: Couldn't accept client connection",e);
            }
            new RequestThread(clientSocket, this).start();
        }

    }

    private void openServerSocket(){
        try{
            this.serverSocket = new ServerSocket(this.serverPort);
        }
        catch (IOException e){
            throw new RuntimeException("Cannot open port " + this.serverPort, e);
        }
    }
}
