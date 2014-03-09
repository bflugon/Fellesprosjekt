package net;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

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
    protected boolean isStopped = false;
    protected Thread runningThread = null;

    public Server(int serverPort){
        this.serverPort = serverPort;
    }

    public void run(){
        synchronized (this){
            this.runningThread = Thread.currentThread();
        }
        openServerSocket();
        while (!isStopped){
            Socket clientSocket;
            try{
                clientSocket = this.serverSocket.accept();
            } catch (IOException e){
                if (isStopped()){
                    System.out.println("Server is stopped");
                    return;
                }
                throw new RuntimeException("Error accepting client connection",e);
            }
            new Thread( new RequestThread(clientSocket,this)).start();

        }
        System.out.println("Server stopped");

    }

    public synchronized boolean isStopped(){
        return this.isStopped;
    }

    public synchronized void stop(){
        this.isStopped = true;
        try{
            this.serverSocket.close();
        } catch (IOException e){
            throw new RuntimeException("Error closing socket", e);
        }
    }

    private void openServerSocket(){
        try{
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e){
            throw new RuntimeException("Cannot open port " + this.serverPort, e);
        }
    }
}
