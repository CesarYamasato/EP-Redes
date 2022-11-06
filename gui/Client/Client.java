package Client;

import java.awt.Component;
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

// Janela inicial quando se opta por enviar arquivos. Aqui se descreve informações para contato.

public class Client {
    public static void main(Window window) {

        // Desenho da tela

        window.reset();
        window.setDescription("Provide an address to connect to");
        Container container = new Container(new Dimension(300, 190), 3, 1, 5);
        TextField ip = new TextField("Contact's IP:", "localhost", window.getFont());
        TextField port = new TextField("Port to use:", "1234", window.getFont());
        container.add(ip);
        container.add(port);
        Button connect = new Button("Connect", window.getFont().getName());
        Button cancel = new Button("Cancel", window.getFont().getName());
        Container buttonContainer = new Container(new Dimension(300, 75), 1, 2, 10);
        buttonContainer.add(connect);
        buttonContainer.add(cancel);
        container.add(buttonContainer);
        window.add(container);

        // Ações disparadas pelo pressionamento dos botões

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