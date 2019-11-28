package assignment6;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class userLogonInterface extends JFrame {

    private JTextField userName = new JTextField();
    private JPasswordField userPassword = new JPasswordField();
    JButton button = new JButton("Log On");


    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, 150);
    }

    public userLogonInterface() {
        super("Please Log On!");
        JLabel jLabel1 = new JLabel("Enter Username: ");
        JLabel jLabel2 = new JLabel("Enter Password: ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
        jPanel.add(jLabel1);
        jPanel.add(userName);
        jPanel.add(jLabel2);
        jPanel.add(userPassword);
        jPanel.add(button);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkUserCreds();
            }
        });
        getContentPane().add(jPanel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void checkUserCreds() {
        String user = userName.getText();
        String password = userPassword.getText();
        TheClient client = new TheClient("localhost", 6666);
        clientGUI clientGUI = new clientGUI(client);
        JFrame frame = new JFrame("Users");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 500);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().add(clientGUI, BorderLayout.CENTER);
        client.establishConnection();
        if (client.tryLogon(user, password)) {
            setVisible(false);
            frame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Incorrect Username or Password!");
        }

    }

    public static void main(String[] args) {
        userLogonInterface userLogonInterface = new userLogonInterface();
       userLogonInterface.setVisible(true);
    }


}
