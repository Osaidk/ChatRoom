package assignment6;

import java.io.*;
import java.net.Socket;

public class Client {
    private String server;
    private int portNum;
    private OutputStream clientStream;
    private InputStream serverStream;
    private BufferedReader bufferedReader;


    public Client(String server, int portNum) {
        this.server = server;
        this.portNum = portNum;
    }


    public static void main(String[] args) {
        Client client = new Client("localhost", 6666);
        if (client.establishConnection())
            System.out.println("Connection established successfully!!");
        else System.out.println("Connection failed!!");
    }


    private boolean establishConnection() {
        try {
            Socket socket = new Socket(server, portNum);
            clientStream = socket.getOutputStream();
            serverStream = socket.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(serverStream));
            return true;
        } catch (IOException e) {
            System.out.println("Error creating socket " + e.getMessage());
        }
        return false;
    }
}
