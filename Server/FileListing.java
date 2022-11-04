package Server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.IOException;

import Interface.Container;
import Interface.Label;
import Interface.Window;
import Interface.Button;
import Server.FileDownloader;

public class FileListing extends Thread {
    private static ArrayList<FileDescriptor> fileDescriptors = new ArrayList<>();
    private static boolean alive;
    private static int fileID;

    public static void main (Window window, ServerSocket ss) {
        FileListing listing = new FileListing();
        alive = true;
        fileID = 0;
        window.reset();
        window.setDescription("Waiting for a connection");
        Container container = new Container(Component.CENTER_ALIGNMENT);
        Container list = new Container(Component.CENTER_ALIGNMENT);
        JScrollPane scrollPane = new JScrollPane(list.get());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        Button cancel = new Button("Cancel", window.getFont().getName());
        container.add(scrollPane);
        container.add(Box.createVerticalStrut(10));
        container.add(cancel);
        container.add(Box.createVerticalStrut(10));
        window.add(container);
        window.draw();
        listing.start();

        cancel.get().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                alive = false;
                try {
                    ss.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
                Server.main(window);
            }
        });
    }

    public void run(Window window, ServerSocket ss, Container container) {
        while (alive) {
            try {
                DataInputStream input = new DataInputStream(ss.accept().getInputStream());
                int fileNameLength = input.readInt();
                if (fileNameLength <= 0)
                    continue;

                window.setDescription("Choose a file to download");
                byte[] fileNameBytes = new byte[fileNameLength];
                input.readFully(fileNameBytes, 0, fileNameBytes.length);
                String fileName = new String(fileNameBytes);
                int fileContentLength = input.readInt();
                byte[] fileContentBytes = new byte[fileContentLength];
                if (fileContentLength <= 0)
                    continue;

                input.readFully(fileContentBytes, 0, fileContentLength);
                Container fileRow = new Container(Component.LEFT_ALIGNMENT);
                Label entry = new Label(fileName, SwingConstants.LEFT, window.getFont());
                fileRow.get().setName(String.valueOf(fileID));
                fileRow.get().addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        JPanel panel = (JPanel) e.getSource();
                        int fileID = Integer.parseInt(panel.getName());

                        for (FileDescriptor d : fileDescriptors) {
                            if (d.getId() == fileID)
                                FileDownloader.main(window, d);
                        }
                    }
                });
                fileDescriptors.add(new FileDescriptor(fileID++, fileName, fileContentBytes, getExtension(fileName)));
                fileRow.add(entry);
                container.add(fileRow.get());
                window.get().revalidate();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getExtension(String fileName) {
        int i = fileName.lastIndexOf('.');
        return (i > 0) ? fileName.substring(i + 1) : "No extension found";
    }
}
