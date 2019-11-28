package assignment6;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class clientGUI extends JPanel implements clientStatus {
    private JList<String> usersList;
    private DefaultListModel<String> userListModel;

    clientGUI(TheClient theClient) {
        theClient.includeClientStatus(this);
        userListModel = new DefaultListModel<>();
        usersList = new JList<>(userListModel);
        setLayout(new BorderLayout());
        add(new JScrollPane(usersList), BorderLayout.CENTER);
        userListModel.addElement("Online Users: \n");
        usersList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() > 1) {
                    String user = usersList.getSelectedValue();
                    MessageGUI messageGUI = new MessageGUI(theClient, user);
                    JFrame jFrame = new JFrame("Instant Message: " + user);
                    jFrame.setDefaultCloseOperation(jFrame.DISPOSE_ON_CLOSE);
                    jFrame.setSize(300, 400);
                    jFrame.getContentPane().add(messageGUI, BorderLayout.CENTER);
                    jFrame.setVisible(true);
                }
            }
        });
    }


    @Override
    public void loggedOn(String username) {
        userListModel.addElement(username);
    }

    @Override
    public void loggedOff(String username){
        userListModel.removeElement(username);
    }
}
