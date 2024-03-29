package assignment6;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TheClient {
    private String server;
    private int portNum;
    private OutputStream clientStream;
    private BufferedReader bufferedReader;
    private List<clientStatus> clientStatusList = new ArrayList<>();
    private List<clientMsgHandler> clientMsgHandlerList = new ArrayList<>();

    TheClient(String server, int portNum) {
        this.server = server;
        this.portNum = portNum;
    }


    void includeClientStatus(clientStatus clientStatus) {
        clientStatusList.add(clientStatus);
        System.out.println(clientStatusList.size());
    }


    void sendMsgToUser(String user, String msg) {
        String message = "send " + user + " " + msg + "\n";
        try {
            clientStream.write(message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    boolean establishConnection() {
        try {
            Socket socket = new Socket(server, portNum);
            clientStream = socket.getOutputStream();
            InputStream serverStream = socket.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(serverStream));
            return true;
        } catch (IOException e) {
            System.out.println("Error creating socket " + e.getMessage());
        }
        return false;
    }


    boolean tryLogon(String user, String password) {
        String feedback = "";
        try {
            clientStream.write(("logon " + user + " " + password + "\n").getBytes());
            feedback = bufferedReader.readLine();
            System.out.println(feedback);
            if("You're Online!".equalsIgnoreCase(feedback)) {
                readServeResponses();
                return true;
            }
        } catch (IOException e) {
            System.out.println("Client log on failed due to: " + e.getMessage());
        }
        if (feedback.equalsIgnoreCase("You're Online!")) {
            readServeResponses();
        }
        return feedback.equalsIgnoreCase("You're Online!");
    }


    private void readServeResponses() {
        Runnable task = () -> {
            try {
                getMessages();
            } catch (IOException e) {
                System.out.println("Error receiving traffic from server: " + e.getMessage());
            }
        };
        new Thread(task).start();
    }


    private void getMessages() throws IOException {
        String in;
        while ((in = bufferedReader.readLine()) != null) {
            String[] userCommand = in.split(" ");
            if (userCommand.length > 0) {
                String input = userCommand[1];
                if ("online!".equalsIgnoreCase(input))
                    loggedOnNotification(userCommand);
                else if ("offline!".equalsIgnoreCase(userCommand[1])) {
                    offlineMsg(userCommand);
                    loggedOffNotification(userCommand);
                }
                else if ("send".equalsIgnoreCase(userCommand[0])) {
                    userCommand = in.split(" ", 3);
                    sendMsg(userCommand);
                }
            }
        }
    }


    private void loggedOnNotification(String[] serverFeedback) {
        String username = serverFeedback[0];
        for (clientStatus clientStatus : clientStatusList) {
            clientStatus.loggedOn(username);
        }
    }


    private void loggedOffNotification(String[] serverFeedback) {
        String username = serverFeedback[0];
        for (clientStatus clientStatus : clientStatusList) {
            clientStatus.loggedOff(username);
        }
    }


    private void sendMsg(String[] userCommand) {
        String user = userCommand[1];
        String msgToSend = userCommand[2];
        for (clientMsgHandler clientMsgHandler : clientMsgHandlerList) {
            clientMsgHandler.msgReceipt(user, msgToSend);
        }
        System.out.println(user + ": " + msgToSend);
    }

    private void offlineMsg(String[] status) {
        String user = status[0];
        String state = "Just logged off!";
        for (clientMsgHandler clientMsgHandler : clientMsgHandlerList) {
            clientMsgHandler.msgReceipt(user, state);
        }
    }


    void setClientMsgHandlerList(clientMsgHandler clientMsgHandler) {
        clientMsgHandlerList.add(clientMsgHandler);
    }


}
