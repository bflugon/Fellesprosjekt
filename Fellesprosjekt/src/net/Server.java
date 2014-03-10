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
    protected boolean running = false;
    protected Thread runningThread = null;

    public Server(int serverPort){
        this.serverPort = serverPort;
    }

    public void run(){
        synchronized (this){
            this.runningThread = Thread.currentThread();
        }
        openServerSocket();
        while (!isRunning()){
            Socket clientSocket;
            try{
                clientSocket = this.serverSocket.accept();
            }
            catch (IOException e){
                if (isRunning()){
                    System.out.println("Server is stopped");
                    return;
                }
                throw new RuntimeException("ERROR: Couldn't accept client connection",e);
            }
            new Thread( new RequestThread(clientSocket)).start();
        }

    }

    public synchronized boolean isRunning(){
        return this.running;
    }

    public synchronized void stop(){
        this.running = true;
        try{
            this.serverSocket.close();
        }
        catch (IOException e){
            throw new RuntimeException("Error closing socket", e);
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
