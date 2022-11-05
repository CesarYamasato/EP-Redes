package main;

import Interface.*;
import Server.*;
import Client.*;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;

public class WhiteRabbit {

    // Desenho da janela
    public static void main(String[] args) {
        String title = "WhiteRabbit", font = "Sans";
        Window window = new Window(title, title, "An application to send or receive files over a TCP connection", font,
                new Dimension(600, 600));
        window.add(Options(window));
        window.draw();
    }

    // Por alguma razão eu dividi métodos relacioandos ao desenho da janela entre a
    // janela em si e os elementos que esta contém, talvez estivesse em busca do
    // encapsulamento P E R F E I T O.
    public static Container Options(Window window) {
        Container container = new Container(new Dimension(200, 100), 2, 1, 10);
        Button client = new Button("Start a connection", window.getFont().getName());
        Button server = new Button("Listen for a connection", window.getFont().getName());
        container.add(client);
        container.add(server);

        // Ações disparadas pelo pressionamento dos botões. Basicamente o usuário
        // escolhe entre enviar ou receber arquivos.
        client.get().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Client.main(window);
            }
        });

        server.get().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Server.main(window);
            }
        });
        return container;
    }

}
