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
import main.WhiteRabbit;

public class Server {
    static ArrayList<FileDescriptor> fileDescriptors = new ArrayList<>();

    public static void main(Window window) {

        // Desenhar a tela
        window.reset();
        window.setDescription("Select a port to await a connection");
        Container container = new Container(new Dimension(300, 155), 2, 1, 5);
        TextField port = new TextField("Port to use:", "1234", window.getFont());
        container.add(port);
        Container buttonContainer = new Container(new Dimension(300, 75), 1, 2, 10);
        Button connect = new Button("Connect", window.getFont().getName());
        Button cancel = new Button("Cancel", window.getFont().getName());
        buttonContainer.add(connect);
        buttonContainer.add(cancel);
        container.add(buttonContainer);
        window.add(container);

        // Ações disparadas pelo pressionamento dos botões
        connect.get().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    FileListing.main(window, new ServerSocket(Integer.parseInt(port.getText())));
                } catch (Exception exception) {
                    window.setDescription("Server socket already in use, choose another port.");
                    exception.printStackTrace();
                }
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
