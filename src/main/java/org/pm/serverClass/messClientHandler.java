package org.pm.serverClass;

//import org.pm.serverClass.messServer;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import org.pm.serverClass.messServer;
public class messClientHandler implements Runnable {

    //final private messServer mainServer;
    private Socket clientSocket;
    final private PrintWriter os;
    final private BufferedReader is;
    //  protected HashMap<Socket, messClientHandler> activeClients = new HashMap<>();
    protected String clientName = null;
    // final private DataInputStream is;
    // final private DataOutputStream os;


    protected messClientHandler(Socket s, BufferedReader is, PrintWriter os/*, HashMap aC*/) {

        this.clientSocket = s;
        this.is = is;
        this.os = os;
        //   this.activeClients = aC;
    }

    @Override
    public void run() {
        try {
            //adding new user to chatroom part
            //choosing user name
            clientName = changeClientName();

            //notifying other users about new user / welcoming new user
            this.os.println("<system> "+ clientName + " welcome in our chat room. Type /... to write special messages. Type /help to get more info.");

            for(Map.Entry<Socket, org.pm.serverClass.messClientHandler> client : messServer.connectedClients.entrySet()) {
                if (this.clientName != client.getValue().clientName)
                    client.getValue().os.println("<system> User: " + this.clientName + " enter to the chat room");

            }

            //conversation part - in infinity loop, /quit to leave the chat room (break the loop)

            String message = null;
            boolean isQuit = false;
            while(!isQuit) {
                message = is.readLine();
                System.out.println(message);

                if (message.charAt(0) == '/')
                    isQuit = specialMessage(message);

                else
                    regularMessage(message);
            }


            //ending conversation  step1 - removing from active clients step2 - closing streams and socket
            for(Map.Entry<Socket, org.pm.serverClass.messClientHandler> client : messServer.connectedClients.entrySet()) {
                if (this.clientName != client.getValue().clientName)
                    client.getValue().os.println("<system> User: " + this.clientName + " leave the chat room");
            }
            is.close();
            os.close();
            messServer.removeClient(clientSocket);
            clientSocket.close();


        } catch(Exception e){System.out.println(e);}

    }


    private String changeClientName () {
        String newClientName = null;
        try {
            while (true) {

                this.os.println("<system> Type your username. Max length is 8char, do not use white spaces.");
                newClientName = this.is.readLine();
                if (!newClientName.trim().equals(newClientName))
                    this.os.println("<system> Invalid username. Do not use white spaces!");
                else if (newClientName.length() > 8)
                    this.os.println("<system> Invalid username. This username is too long!");
                else if (newClientName == "system" || newClientName == "help" || newClientName == "users")
                    this.os.println("<system> Invalid username. This username is currently in use!");
                else {
                    boolean isUnique = true;
                    for (Map.Entry<Socket, org.pm.serverClass.messClientHandler> client : messServer.connectedClients.entrySet()) {
                        if (newClientName.equals(client.getValue().clientName)) {
                            this.os.println("<system> Invalid username. This username is currently in use!");
                            isUnique = false;
                            break;
                        }
                    }
                    if (isUnique)
                        break;
                }
            }
        }catch(Exception e){System.out.println(e);}

        return newClientName;
    }

    //handling special messages
    private boolean specialMessage (String msg) {
        //TODO, have to be synchronized
        String[] splitMsg = null;
        splitMsg = msg.split(" ", 2); //splitMsg[0] - functional part, splitMsg[1] - right message
        splitMsg[0] = splitMsg[0].substring(1);
        switch (splitMsg[0]) {
            case "help":
                this.os.println("<system> Welcome to PROZ Messenger 1.0." +
                        "Start your message from /[command] to write special message." +
                        "Command list:" +
                        "/[user_name] [right message] - to writte direct message to [user_name]" +
                        "/[change] - to change your user name" +
                        "/[whoiam] - to chceck your user name" +
                        "/[users] - to list active users" +
                        "/[server] - to display information about the server" +
                        "/[quit] - to leave the chat room"
                );
                break;
            case "change":
                this.clientName = changeClientName();
                break;
            case "whoiam":
                this.os.println("<system> Your current user name is: " + this.clientName);
                break;
            case "users":
                for (Map.Entry<Socket, org.pm.serverClass.messClientHandler> client : messServer.connectedClients.entrySet())
                    this.os.println(client.getValue().clientName);
                break;
            case "server":
                break;
            case "quit":
                return true;
            default:
                if(splitMsg[1] != null)
                    directMessage(splitMsg[0], splitMsg[1]);
                break;

        }
        return false;
    }


    //handling direct messages
    private void directMessage (String targetName, String msg) {
        for (Map.Entry<Socket, org.pm.serverClass.messClientHandler> client : messServer.connectedClients.entrySet()) {

            if (targetName.equals(client.getValue().clientName)) {
                client.getValue().os.println("<PRIVATE FROM " + this.clientName +">" + msg);
                return;
            }
        }
        this.os.println(targetName);
        this.os.println("<system> User: " + targetName + " does not exist. Type /help to get more info.");
    }

    //handling regular messages
    private void regularMessage (String msg) {
        for (Map.Entry<Socket, org.pm.serverClass.messClientHandler> client : messServer.connectedClients.entrySet())
            client.getValue().os.println("<" + this.clientName +">" + msg);

    }

    private void notifyMainServer (Socket clientSocket) {
        //TODO, h t b snchr
    }
}



/*package server;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.*;
import java.net.*;
import java.lang.*;


public class messClientHandler implements Runnable {

    final private messServer mainServer;
    final private Socket clientSocket;
    final private PrintWriter os;
    final private BufferedReader is;
    // private ArrayList<messClientHandler> activeClients = new ArrayList<>();
    protected HashMap<Socket, messClientHandler> activeClients = new HashMap<>();
    public String clientName = null;
    // final private DataInputStream is;
    // final private DataOutputStream os;


    public messClientHandler(messServer mS, Socket s, BufferedReader is, PrintWriter os, HashMap aC) {

        this.mainServer = mS;
        this.clientSocket = s;
        this.is = is;
        this.os = os;
        this.activeClients = aC;
        this.run();
    }

    @Override
    public void run() {
        try {
        //adding new user to chatroom part
            //choosing user name
            clientName = changeClientName();

            //notifying other users about new user / welcoming new user
            this.os.println("<system> Welcome in chat room. Type /... to write special messages. Type /help to get more info.");

            for(Map.Entry<Socket, messClientHandler> client : activeClients.entrySet()) {
                if (this.clientName != client.getValue().clientName)
                    client.getValue().os.println("<system> User:" + this.clientName + "enter to the chat room");

            }

        //conversation part - in infinity loop, /quit to leave the chat room (break the loop)

            String message = null;
            while(true) {
                message = is.readLine();

                if (message == "/quit")
                    break;

                if (message.charAt(0) == '/')
                    specialMessage(message);

                else
                    regularMessage(message);
            }


        //ending conversation  step1 - removing from active clients step2 - closing streams and socket
            this.mainServer.removeClient(this.clientSocket);

            is.close();
            os.close();
            clientSocket.close();


        } catch(Exception e){System.out.println(e);}

    }


    private String changeClientName () {
        String clientName = null;
        try {
            while (true) {

                this.os.println("<system> Type your username. Max length is 8char, do not use white spaces.");
                clientName = this.is.readLine();
                if (clientName.trim() != clientName)
                    this.os.println("<system> Invalid username. Do not use white spaces!");
                else if (clientName.length() > 8)
                    this.os.println("<system> Invalid username. This username is too long!");
                else if (clientName == "system" || clientName == "help" || clientName == "users")
                    this.os.println("<system> Invalid username. This username is currently in use!");
                else {
                    boolean isUnique = true;
                    for (Map.Entry<Socket, messClientHandler> client : this.activeClients.entrySet()) {
                        if (this.clientName == client.getValue().clientName) {
                            this.os.println("<system> Invalid username. This username is currently in use!");
                            isUnique = false;
                            break;
                        }
                    }
                    if (isUnique)
                        break;
                }
            }
        }catch(Exception e){System.out.println(e);}

        return clientName;
    }

    //handling special messages
    private void specialMessage (String msg) {
        //TODO, have to be synchronized
        String[] splitMsg = msg.split(" ", 2); //splitMsg[0] - functional part, splitMsg[1] - right message
        splitMsg[0] = splitMsg[0].substring(1);
        switch (splitMsg[0]) {
            case "help":
                this.os.println("<system> Welcome to PROZ Messenger 1.0." +
                        "Start your message from /[command] to write special message." +
                        "Command list:" +
                        "/[user_name] [right message] - to writte direct message to [user_name]" +
                        "/[change] - to change your user name" +
                        "/[whoiam] - to chceck your user name" +
                        "/[users] - to list active users" +
                        "/[server] - to display information about the server"
                );
            case "change":
                this.changeClientName();
            case "whoiam":
                this.os.println("<system> Your current user name is: " + this.clientName);
            case "users":
                //TODO
            case "server":
                //TODO
            default:
                directMessage(splitMsg[0], splitMsg[1]);


        }
    }

    //handling direct messages
    private void directMessage (String targetName, String msg) {
        for (Map.Entry<Socket, messClientHandler> client : activeClients.entrySet()) {
            if (client.getValue().clientName == targetName) {
                client.getValue().os.println("<PRIVATE FROM " + this.clientName +">" + msg);
                return;
            }
        }

        this.os.println("<system> User:" + targetName + "does not exist. Type /help to get more info.");
    }

    //handling regular messages
    private void regularMessage (String msg) {
        for (Map.Entry<Socket, messClientHandler> client : activeClients.entrySet())
            client.getValue().os.println("<" + this.clientName +">" + msg);

    }

    private void notifyMainServer (Socket clientSocket) {
        //TODO, h t b snchr
    }
}
*/