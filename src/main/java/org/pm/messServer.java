package org.pm;
import javafx.application.Application;
import javafx.stage.Stage;
import java.util.*;
import java.net.*;
import java.lang.*;
//import java.io.*;

public class messServer extends Application implements Runnable {


    private static final int PORT_NUMBER = 6789;
    private ServerSocket ss;
    private String hostName;

    private HashMap<Socket, String> connectedClients = new HashMap<>();

    //private Timer timer;

    public  messServer() {
        Thread serverTh = new Thread(this);
        serverTh.start(); //run()
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void run() {
        //creating server

        try {
            ss = new ServerSocket(PORT_NUMBER);
            hostName = InetAddress.getLocalHost().getHostName();
        } catch(Exception e){System.out.println(e);}

        Socket s;
        while(true) {
            try {
                s = ss.accept();//returns the socket and establish a connection between server and client
            } catch(Exception e){System.out.println(e);}

        }
    }

    public void addClient(Socket s){
        // TODO
    }

    public static void main(String[] args){
        // TODO
    }
}
