import Interface.*;
import Server.*;
import Client.*;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

public class WhiteRabbit {
    public static void main(String[] args) {
        String title = "WhiteRabbit", font = "Sans";
        Dimension minSize = new Dimension(400, 400);
        Window window = new Window(title, title, "An application to send or receive files over a TCP connection", font,
                new Dimension(400, 400));
        window.add(new Options());
        window.draw();
    }

    public Container Options() {
        Container container = new Container(BoxLayout.Y_AXIS);
        Button send = new Button("Send files", font);
        Button recieve = new Button("Recieve files", font);
        buttonContainer.add(send);
        buttonContainer.add(recieve);

        send.get().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Client.main(window);
            }
        });

        receive.get().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Server.main(window);
            }
        });
        return container;
    }

}
