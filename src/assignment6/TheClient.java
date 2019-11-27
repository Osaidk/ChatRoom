package assignment6;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class TheClient {
    private String server;
    private int portNum;
    private Scanner scanner;
    private Socket socket;
    private OutputStream clientStream;
    private InputStream serverStream;
    private BufferedReader bufferedReader;


    public TheClient(String server, int portNum) {
        this.server = server;
        this.portNum = portNum;
        this.scanner = new Scanner(System.in);
    }


    public static void main(String[] args) throws IOException {
        TheClient theClient = new TheClient("localhost", 6666);
        if (!theClient.establishConnection())
            System.out.println("Connection failed!!");
        else {
            System.out.println("Connection established successfully!!");
            if (!theClient.tryLogon())
                System.out.println("Log on was unsuccesful!");
            else {

            }

        }
    }


    private boolean establishConnection() {
        try {
            socket = new Socket(server, portNum);
            clientStream = socket.getOutputStream();
            serverStream = socket.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(serverStream));
            return true;
        } catch (IOException e) {
            System.out.println("Error creating socket " + e.getMessage());
        }
        return false;
    }


    private boolean tryLogon() {
        String feedback = "";
        System.out.println("Please Enter Your Credentials: <username> <password>");
        String creds = scanner.nextLine();
        try {
            clientStream.write(("logon " + creds + "\n").getBytes());
            feedback = bufferedReader.readLine();
            System.out.println(feedback);
            if("you logged on".equalsIgnoreCase(feedback))
                return true;
            else if ("Wrong Credentials! Try again".equalsIgnoreCase(feedback)) {
                while ("Wrong Credentials! Try again".equalsIgnoreCase(feedback)) {
                    System.out.println("Please Enter Your Credentials again: <username> <password>");
                    creds = scanner.nextLine();
                    clientStream.write(("logon " + creds + "\n").getBytes());
                    feedback = bufferedReader.readLine();
                    System.out.println(feedback);
                }
            }
        } catch (IOException e) {
            System.out.println("Client log on failed due to: " + e.getMessage());
        }
        return feedback.equalsIgnoreCase("you logged on");
    }



}
