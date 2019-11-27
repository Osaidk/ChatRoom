package assignment6;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerInterface extends Thread {
    private int portNum;
    private List<clientHandler> onlineHandlers = new ArrayList<>();

    public ServerInterface(int portNum) {
        this.portNum = portNum;
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(portNum);
            while (true) {
                Socket socket = serverSocket.accept();
                clientHandler clientHandler = new clientHandler(socket, this);
                clientHandler.start();
            }
        } catch (IOException e) {
            System.out.println("Error creating server socket: " + e.getMessage());
        }
    }



    public List<clientHandler> getOnlineHandlers() {
        return onlineHandlers;
    }
}


