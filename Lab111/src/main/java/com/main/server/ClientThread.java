package com.main.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ClientThread extends Thread {
    private final Socket socket;
    private final ServerSocket serverSocket;
    private List<User> network;
    private int clientsRunning;
    private boolean isClosing;
    private PrintWriter out;
    private User userLogged;
    private boolean running;

    public ClientThread(Socket socket, ServerSocket serverSocket, List<User> network, int clientsRunning, boolean isClosing) {
        this.socket = socket;
        this.serverSocket = serverSocket;
        this.network = network;
        this.userLogged = null;
        this.clientsRunning = clientsRunning;
        this.isClosing = isClosing;
    }

    public void run() {
        if (!isClosing) {

            clientsRunning++;

            try {
                // Get the request from the input stream: client → server
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));

                // Send the response to the oputput stream: server → client
                out = new PrintWriter(socket.getOutputStream());
                running = true;
                socket.setSoTimeout(20000);
                while (running) {
                    String request = in.readLine();
                    String raspuns;
                    System.out.println("Server recived the request: " + request);
                    if (request.equals("stop")) {
                        running = false;
                        stopServer();
                    } else if (request.startsWith("register")) {
                        register(request);
                    } else if (request.startsWith("login")) {
                        login(request);

                    } else if (request.startsWith("friend")) {
                        friend(request);
                    } else if (request.startsWith("send")) {
                        sendMessage(request);
                    } else if (request.equals("read")) {
                        readMessage();
                    } else if (request.equals("exit")) {
                        clientOut();
                    }
                }

            } catch (SocketTimeoutException e) {
                System.err.println("Client afk: " + this.socket.getLocalAddress());
            } catch (IOException e) {
                System.err.println("Communication error... " + e);
            } finally {
                try {
                    socket.close(); // or use try-with-resources
                } catch (IOException e) {
                    System.err.println(e);
                }
            }
        }

    }

    private void clientOut() {
        System.out.println("Client " + this.socket.getLocalAddress() + " is out");
        out.println("Bye bye!");
        out.flush();
        clientsRunning--;
        running = false;
    }


    private void readMessage() {
        if (userLogged != null) {
            for (User user : network) {
                if (user.getName().equals(userLogged.getName())) {
                    String raspuns;
                    if (user.getMessages() == null)
                        raspuns = "There are no messages!";
                    else
                        raspuns = user.getMessages().toString();
                    out.println(raspuns);
                    out.flush();
                }
            }
        }

    }

    private void sendMessage(String request) {
        String splitter = "^[^\\s]*\\s";
        String message = "Message from " + userLogged.getName() + ": " + request.split(splitter)[1] + "\n";

        for (User friend : userLogged.getFriends()) {
            friend.addMessage(message);
        }
        String raspuns;
        raspuns = "Message sent!";
        out.println(raspuns);
        out.flush();


    }


    private void friend(String request) {

        if (userLogged != null) {

            List<String> friendsAsString = Arrays.asList(request.substring(request.indexOf(' ') + 1).split(" "));

            friendsAsString = checkFriends(friendsAsString);
            if (friendsAsString == null) {
                return;
            }
            List<User> friends = new ArrayList<>();
            for (String friend : friendsAsString) {
                for (User user : network) {
                    if (user.getName().equals(userLogged.getName())) {
                        if (user.getFriends() != null) {
                            for (User friendUser : user.getFriends()) {
                                if (!friendUser.getName().equals(friend)) {
                                    friends.add(user);
                                }
                            }
                        }
                        for (User user1 : network) {
                            if (user1.getName().equals(friend)) {
                                friends.add(user1);
                            }

                        }
                    }

                }
            }

            userLogged.setFriends(friends);
            String raspuns = "Friends " + friends + " added!";
            out.println(raspuns);
            out.flush();

        } else {
            String raspuns = "You have to login first!";
            out.println(raspuns);
            out.flush();
        }

    }

    private List<String> checkFriends(List<String> friends) {
        List<String> listChecked = new ArrayList<>();
        List<String> friendsDontExist = new ArrayList<>();

        for (String friend : friends) {

            if (checkUsername(friend)) {
                listChecked.add(friend);

            } else {
                friendsDontExist.add(friend);
            }

        }
        if (friendsDontExist.isEmpty())
            return listChecked;
        else {
            String raspuns = "The friends \"" + friendsDontExist + "\" doesn't exist!";
            out.println(raspuns);
            out.flush();
            return null;
        }
    }

    private boolean checkUsername(String friend) {
        for (User user : network) {
            if (user.getName().equals(friend)) {
                return true;
            }
        }
        return false;
    }

    private void login(String request) {
        String splitter = " ";
        User user = new User(request.split(splitter)[1]);


        if (userExists(user)) {

            userLogged = user;
            String raspuns = "Hello " + user.getName();
            out.println(raspuns);
        } else {
            String raspuns = "User doesn't exist!";
            out.println(raspuns);
        }
        out.flush();
    }

    private boolean userExists(User user) {
        for (User userFound : network) {
            if (userFound.getName().equals(user.getName())) {
                return true;
            }
        }
        return false;
    }

    private void stopServer() throws IOException {

        String raspuns = "Server stopped!";
        out.println(raspuns);
        out.flush();
        clientsRunning--;
        while (clientsRunning != 0) {
            try{
                wait(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        serverSocket.close();
    }


    private void register(String request) {
        String splitter = " ";
        User user = new User(request.split(splitter)[1]);
        if (this.network.contains(user)) {
            String raspuns = "User already exists!";
            out.println(raspuns);
            out.flush();
        } else {
            String raspuns = "User registered!";
            out.println(raspuns);
            out.flush();
            this.network.add(user);
        }

    }


}

