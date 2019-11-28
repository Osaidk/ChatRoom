package assignment6;

import javax.swing.*;
import java.awt.*;

public class MessageGUI extends JPanel implements clientMsgHandler {
    private final String user;
    private DefaultListModel<String> defaultListModel = new DefaultListModel<>();
    private JTextField textField = new JTextField();


    MessageGUI(TheClient theClient, String user) {
        this.user = user;
        theClient.setClientMsgHandlerList(this);
        setLayout(new BorderLayout());
        JList<String> messages = new JList<>(defaultListModel);
        add(new JScrollPane(messages), BorderLayout.CENTER);
        add(textField, BorderLayout.SOUTH);

        textField.addActionListener(e -> {
            String message = textField.getText();
            theClient.sendMsgToUser(user, message);
            defaultListModel.addElement("Me: " + message);
            textField.setText("");
        });

    }

    @Override
    public void msgReceipt(String user, String msg) {
        if (user.equalsIgnoreCase(this.user)) {
            String message = user + ": " + msg;
            defaultListModel.addElement(message);
        }
    }
}
