package com.main.server;

import com.main.server.ClientThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class MyServer {
    private static final int PORT = 8100;
    private List<User> network;
    private int runningClients=0;
    private boolean isClosing;
    public MyServer(){
        network = new ArrayList<>();
    }

    public void startServer(){
        boolean running = true;
        ServerSocket serverSocket = null ;
        try {
            serverSocket = new ServerSocket(PORT);
            while (running) {
                System.out.println ("Waiting for a client ...");
                Socket socket = serverSocket.accept();

                new ClientThread(socket,serverSocket,network,runningClients,isClosing).start();
            }
        }catch (SocketException e) {
            System.err.println("Server stopped!");
        }
        catch ( IOException e) {
            System.err. println ("Ooops... " + e);
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
