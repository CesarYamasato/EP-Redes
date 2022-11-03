package main;

import Interface.*;
import Server.*;
import Client.*;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;

public class WhiteRabbit {
    public static void main(String[] args) {
        String title = "WhiteRabbit", font = "Sans";
        Window window = new Window(title, title, "An application to send or receive files over a TCP connection", font,
                new Dimension(600, 600));
        window.add(Options(window));
        window.draw();
    }

    public static Container Options(Window window) {
        Container container = new Container(new Dimension(200, 100), 2, 1, 10);
        Button send = new Button("Send files", window.getFont().getName());
        Button recieve = new Button("Recieve files", window.getFont().getName());
        container.add(send);
        container.add(recieve);

        send.get().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Client.main(window);
            }
        });

        recieve.get().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Server.main(window);
            }
        });
        return container;
    }

}
