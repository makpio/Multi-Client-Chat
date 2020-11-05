package org.pm.clientClass;


import java.util.*;
import java.net.*;
import java.lang.*;
import java.io.*;

//
public class messClient{

    private static final int SERVER_PORT = 6900;


    public static void main(String args[]) throws UnknownHostException, IOException, Exception {

        Socket clientSocket = null;
        Scanner scanner = new Scanner(System.in);

        InetAddress ip = InetAddress.getByName("localhost");
        clientSocket = new Socket(ip, SERVER_PORT);


        final BufferedReader clientIs = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        final PrintWriter clientOs = new PrintWriter(clientSocket.getOutputStream(), true);

        // sendMessage thread
        Thread sendMessage = new Thread(new Runnable() {
            @Override

            public void run() {
                synchronized (this) {
                    while (true) {

                        // read the message to deliver.
                        String message = scanner.nextLine();

                        clientOs.println(message);

                    }
                }
            }
        });

        // readMessage thread
        Thread readMessage = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    while (true) {
                        try {
                            // read the message sent to this client
                            String message = clientIs.readLine();
                            System.out.println(message);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        sendMessage.start();
        readMessage.start();

    }
}