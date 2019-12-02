package assignment6;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Server interface responsible for accepting client connections
 * It contains a collection of current online connections
 * The collection is passed to the client handler class for modification
 * Runs on separate thread to ensure multiple client connectivity with no interruption
 * The class overrides the Runnable method of Thread class
 */
public class ServerInterface extends Thread {
    private int serverPort;
    private List<clientHandler> onlineClients = new ArrayList<>();

    ServerInterface(int serverPort) {
        this.serverPort = serverPort;
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(serverPort);
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                clientHandler clientHandler = new clientHandler(socket, this);
                clientHandler.start();
            }
        } catch (IOException e) {
            System.out.println("Error creating server socket: " + e.getMessage());
        }
    }

    /**
     * A getter for online clients
     * @return Arraylist of current online accepted client connections
     */

    List<clientHandler> getOnlineClients() {
        return onlineClients;
    }
}


