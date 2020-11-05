package org.pm.serverClass;

//import org.pm.serverClass.messClientHandler;
import java.io.*;
import java.util.*;
import java.net.*;
import java.lang.*;

public class messServer  {

    private static final int PORT_NUMBER = 6900;
    private static ServerSocket serverSocket = null;
    private static String hostName;
    protected static HashMap<Socket, messClientHandler> connectedClients = new HashMap<>();

    public static void main(String args[]) throws IOException {

        serverSocket = new ServerSocket(PORT_NUMBER);
        hostName = InetAddress.getLocalHost().getHostName();

       // serverOs = new PrintWriter(serverSocket.getOutputStream(), true);
        //create a client socket

        //DataInputStream clientIs;
        //DataOutputStream clientOs;

        int clientCounter = 0;
        //creating new connections with Clients, then every connection receive a new thread to
        while(true) {
            addConnection();
            ++clientCounter;
        }

    }
    private static void addConnection() {
        PrintWriter clientOs = null;
        BufferedReader clientIs = null;
        Socket clientSocket = null;

        try {
            clientSocket = serverSocket.accept();//returns the socket and establish a connection between server and client
            //clientIs = new DataInputStream(clientSocket.getInputStream());
            //clientOs = new DataOutputStream(clientSocket.getOutputStream());
            clientIs = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            clientOs = new PrintWriter(clientSocket.getOutputStream(), true);
            messClientHandler newConnection = new messClientHandler(clientSocket, clientIs, clientOs/*, connectedClients*/);
            connectedClients.put(clientSocket, newConnection);
            System.out.println(connectedClients.size());
            Thread thread = new Thread(newConnection);
            thread.start();
            System.out.println("new client connected");
         //   updateClientsList();
        } catch(Exception e) {System.out.println(e);}

    }

 /*   private static void updateClientsList() {
        //TODO ten synchronized ogarnac lepiej
            for (Map.Entry<Socket, messClientHandler> client : connectedClients.entrySet())
                client.getValue().activeClients = new HashMap<Socket, messClientHandler>(connectedClients);

    }
*/
    protected static void removeClient(Socket s) {
        connectedClients.remove(s);
       // updateClientsList();
    }

}

