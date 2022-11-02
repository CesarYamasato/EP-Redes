package Server;

import Interface.*;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

public class Server {
    static ArrayList<FileDescriptor> fileDescriptors = new ArrayList<>();

    public static void main(Window window) {
        window.reset();
        window.setDescription("Select a port to await a connection");
        Container container = new Container(BoxLayout.Y_AXIS);
        TextField port = new TextField("Port to use:", "1234", 15, font);
        Button connect = new Button("Connect", font);
        Button cancel = new Button("Cancel", font);
        Container buttonContainer = new Container(BoxLayout.X_AXIS);
        buttonContainer.add(connect);
        buttonContainer.add(cancel);
        container.add(port);
        window.add(container);

        connect.get().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileListing.main(window, new ServerSocket(Integer.parseInt(port.getText())));
            }
        });

        cancel.get().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.close();
                WhiteRabbit.main();
            }
        });
        window.draw();
    }
}
