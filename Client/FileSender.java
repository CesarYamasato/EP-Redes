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

import Interface.Button;
import Interface.Container;
import Interface.Window;

// Tela onde ocorre a confirmação do envio
public class FileSender {
    public static void main(Window window, File file, String ip, String port) {

        // Desenho da tela
        window.reset();
        window.setDescription("Send " + file.getName() + "?");
        Container container = new Container(new Dimension(200, 100), 2, 1, 10);
        Button send = new Button("Send", window.getFont().getName());
        Button cancel = new Button("Cancel", window.getFont().getName());
        container.add(send);
        container.add(cancel);
        window.add(container);

        // Ações disparadas pelo pressionamento dos botões
        send.get().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (file != null) {
                    sendFile(file, ip, port);
                }
            }
        });

        cancel.get().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileSelector.main(window, ip, port);
            }
        });
        window.draw();
    }

    // Método privado para realizar o envio do arquivo
    private static void sendFile(File file, String ip, String port) {
        try {
            // Tome o nome do arquivo e seu comprimento
            String fileName = file.getName();
            byte[] fileBytes = fileName.getBytes();

            // Tome o comprimento do arquivo e e crie um array de bytes do conteúdo deste
            byte[] fileContents = new byte[(int) file.length()];
            FileInputStream input = new FileInputStream(file.getAbsolutePath());
            input.read(fileContents);

            // Crie um socket e o stream para o envio dos dados
            Socket s = new Socket(ip, Integer.parseInt(port));
            DataOutputStream output = new DataOutputStream(s.getOutputStream());

            // Envie o nome do arquivo e seu comprimento
            output.writeInt(fileBytes.length);
            output.write(fileBytes);

            // Envie o conteúdo e feche o socket e streams
            output.writeInt(fileContents.length);
            output.write(fileContents);
            input.close();
            output.close();
            s.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
