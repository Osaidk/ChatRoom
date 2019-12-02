package assignment6;


/**
 * Main server class
 * class initiates all aspects of server
 * server is independent from user connectivity
 * runs even if no user is connected
 */
public class ServerMain {
    public static void main(String[] args) {
        final int portNum = 6666;
        ServerInterface serverInterface = new ServerInterface(portNum);
        serverInterface.start();
    }
}
