package assignment6;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;

/**
 * Class client handler control commands from the user on command line or GUI
 * Runs on a separate thread for smooth experience
 * Overrides the Runnable method from Thread class
 */

public class clientHandler extends Thread {
    private Socket socket;
    private ServerInterface serverInterface;
    private OutputStream serverCommunication;
    private HashMap<String, String> users;
    private String username;
    private List<clientHandler> onlineClients;
    private boolean loggedOn = false;
    private boolean loggedOffProperly = false;

    /**
     * Constructor for the class
     * Defines hash map values for the expected clients and their passwords
     * Class can be modified to implement a more useful way to define expected clients
     * @param socket reference for client socket
     * @param serverInterface reference for server interface to obtain collection of clients
     */
    clientHandler(Socket socket, ServerInterface serverInterface) {
        this.socket = socket;
        this.serverInterface = serverInterface;
        users = new HashMap<>();
        users.put("osaid", "123456");
        users.put("elaf", "112233");
        users.put("anas", "223344");

    }

    @Override
    public void run() {
        try {
            clientSocketControl();
        } catch (IOException e) {
            System.out.println("TheClient Socket handling msg: " + e.getMessage());
        }
    }

    /**
     * Method controller for different user commands to the server
     * Method also terminates and logs out a user if no longer active
     * @throws IOException thrown if an error is encountered with input/output of server
     */
    private void clientSocketControl() throws IOException {
        serverCommunication = socket.getOutputStream();
        InputStream clientCommunication = socket.getInputStream();
        BufferedReader reader = new BufferedReader
                (new InputStreamReader(clientCommunication));
        String in;
        while ((in = reader.readLine()) != null) {
            String[] commands = in.split(" ", 3);
            if (commands.length > 0) {
                if ("logout".equalsIgnoreCase(commands[0])) {
                    String logoffNotification = " offline!\n";
                    iterateUsers(username, onlineClients, logoffNotification);
                    loggedOffProperly = true;
                    break;
                }
                else if ("logon".equalsIgnoreCase(commands[0])) {
                    logonService(serverCommunication, commands);
                } else if ("send".equalsIgnoreCase(commands[0])) {
                    sendMessage(commands);
                } else {
                    String response = "No such command " + commands[0] + "!";
                    notification(response, clientHandler.this, "\n");
                }
            }
        }
        terminateConnection();
    }

    /**
     * Method terminates, closes the socket, and logs out user if no longer active
     * @throws IOException thrown if an error is encountered with input/output of server
     */

    private void terminateConnection() throws IOException {
        if (loggedOn) {
            removerUser();
            if (!loggedOffProperly) {
                String logoffNotification = " offline!\n";
                iterateUsers(username, onlineClients, logoffNotification);
            }
        }
        socket.close();
    }

    /**
     * Method removes a user from list of online clients if user logs out
     * or no longer active
     */
    private void removerUser() {
        onlineClients.remove(this);
    }

    /**
     * Method handles client log on operations
     * Copies list of online clients for future modifications
     * @param serverCommunication Output stream for server - client communication
     * @param commands An array of user commands
     * @throws IOException thrown if an error is encountered with input/output of server
     */
    private void logonService(OutputStream serverCommunication, String[] commands) throws IOException {
        if (commands.length == 3) {
            username = commands[1];
            String password = commands[2];
            String userPassword = getUserPassword(username);
            if (userPassword != null && userPassword.equals(password)) {
                String msg = "You're Online!\n";
                serverCommunication.write(msg.getBytes());
                onlineClients = serverInterface.getOnlineClients();
                onlineClients.add(this);
                loggedOn = true;
                String selfNotify = " online!\n";
                selfNotifyMsg(serverCommunication, selfNotify);
                String logonNotification = " online!\n";
                iterateUsers(username, onlineClients, logonNotification);
            } else {
                String msg = "Wrong Credentials! Try again\n";
                serverCommunication.write(msg.getBytes());
            }
        } else {
            String msg = "Wrong Credentials! Try again\n";
            serverCommunication.write(msg.getBytes());
        }
    }


    private void sendMessage(String[] commands) throws IOException {
        String user = commands[1];
        String msgBody = commands[2] + "\n";
        if (users.containsKey(user)) {
            for (clientHandler clientHandler : onlineClients) {
                if (user.equalsIgnoreCase(clientHandler.getUsername())) {
                    clientHandler.serverCommunication.write(("send " + username + " " + msgBody).getBytes());
                }
            }
        } else serverCommunication.write("No such user!".getBytes());
    }


    private void selfNotifyMsg(OutputStream serverCommunication, String selfNotify) throws IOException {
        for (clientHandler clientHandler : onlineClients) {
            if (clientHandler != this)
                serverCommunication.write((clientHandler.username + selfNotify).getBytes());
        }
    }


    private void iterateUsers(String username, List<clientHandler> onlineHandlers, String msg) throws IOException {
        for (clientHandler clientHandler : onlineHandlers) {
            if (this != clientHandler) {
                notification(username, clientHandler, msg);
            }
        }
    }


    private void notification(String username, clientHandler clientHandler, String s) throws IOException {
        clientHandler.serverCommunication.write((username + s).getBytes());
    }


    private String getUserPassword(String username) {
        return users.get(username);
    }

    private String getUsername() {
        return username;
    }
}
