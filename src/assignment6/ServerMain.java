package assignment6;

public class ServerMain {
    public static void main(String[] args) {
        final int portNum = 6666;
        ServerInterface serverInterface = new ServerInterface(portNum);
        serverInterface.start();
    }
}
