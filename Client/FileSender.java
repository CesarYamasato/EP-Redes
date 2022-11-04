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

public class FileSender {
    public static void main(Window window, File file, String ip, String port) {
        window.reset();
        window.setDescription("Send " + file.getName() + "?");
        Container container = new Container(new Dimension(200, 100), 2, 1, 10);
        Button send = new Button("Send", window.getFont().getName());
        Button cancel = new Button("Cancel", window.getFont().getName());
        container.add(send);
        container.add(cancel);
        window.add(container);

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

    private static void sendFile(File file, String ip, String port) {
        try {
            // Take file name and its length
            String fileName = file.getName();
            byte[] fileBytes = fileName.getBytes();

            // Take file length and its contents
            byte[] fileContents = new byte[(int) file.length()];

            FileInputStream input = new FileInputStream(file.getAbsolutePath());
            input.read(fileContents);

            // Create socket and data output stream
            Socket s = new Socket(ip, Integer.parseInt(port));
            DataOutputStream output = new DataOutputStream(s.getOutputStream());

            // Send file name and its length
            output.writeInt(fileBytes.length);
            output.write(fileBytes);

            // Send file length and its contents
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
