package Client;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import Interface.*;
import main.WhiteRabbit;

public class Client {
    public static void main(Window window) {
        window.reset();
        window.setDescription("Provide an address to connect to");

        Container container = new Container(BoxLayout.Y_AXIS);
        TextField ip = new TextField("Contact's IP:", "localhost", 15, window.getFont());
        TextField port = new TextField("Port to use:", "1234", 15, window.getFont());
        Button connect = new Button("Connect", window.getFont().getName());
        Button cancel = new Button("Cancel", window.getFont().getName());
        Container buttonContainer = new Container(BoxLayout.X_AXIS);
        buttonContainer.add(connect);
        buttonContainer.add(cancel);
        container.add(ip);
        container.add(port);
        container.add(buttonContainer);
        window.add(container);

        connect.get().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileSelector.main(window, ip.getText(), port.getText());
            }
        });

        cancel.get().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.close();
                WhiteRabbit.main(null);
            }
        });
        window.draw();
    }
}
