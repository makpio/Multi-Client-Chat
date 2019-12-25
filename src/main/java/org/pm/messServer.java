package org.pm;
import java.util.*;
import java.net.*;


public class messServer implements Runnable {


    private static final int PORT_NUMBER = 6789;
    private ServerSocket server;

    private ArrayList<Socket> connectionsList = new ArrayList<>();


    @Override
    public void run() {
    }
}
