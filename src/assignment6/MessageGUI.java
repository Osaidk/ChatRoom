package assignment6;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MessageGUI extends JPanel implements clientMsgHandler {
    private final String user;
    private final TheClient theClient;
    private DefaultListModel<String> defaultListModel = new DefaultListModel<>();
    private JList<String> messages = new JList<>(defaultListModel);
    private JTextField textField = new JTextField();


    public MessageGUI(TheClient theClient, String user) {
        this.theClient = theClient;
        this.user = user;
        theClient.setClientMsgHandlerList(this);
        setLayout(new BorderLayout());
        add(new JScrollPane(messages), BorderLayout.CENTER);
        add(textField, BorderLayout.SOUTH);

        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = textField.getText();
                theClient.sendMsgToUser(user, message);
                defaultListModel.addElement("Me: " + message);
                textField.setText("");
            }
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
