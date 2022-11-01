package Server;

import Interface.*;
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

    public static void main(String[] args) throws IOException {
        int fileID = 0;

        Window window = new Window("White Rabbit Server", "White Rabbit", "Waiting for a connection");

        Container scrollPaneContainer = new Container(BoxLayout.Y_AXIS);
        JScrollPane scrollPane = new JScrollPane(scrollPaneContainer.get());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        window.add(scrollPane);
        window.draw();

        ServerSocket ss = new ServerSocket(25565);

        while (true) {
            try {
                // Receive files, if available.
                DataInputStream input = new DataInputStream(ss.accept().getInputStream());
                int fileNameLength = input.readInt();

                // if files were received, list them.
                if (fileNameLength > 0) {
                    window.setDescription("Choose a file to download");
                    byte[] fileNameBytes = new byte[fileNameLength];
                    input.readFully(fileNameBytes, 0, fileNameBytes.length);
                    String fileName = new String(fileNameBytes);
                    int fileContentLength = input.readInt();
                    byte[] fileContentBytes = new byte[fileContentLength];

                    if (fileContentLength > 0) {
                        input.readFully(fileContentBytes, 0, fileContentLength);
                        Container fileRow = new Container(BoxLayout.Y_AXIS);
                        Label entry = new Label(fileName, SwingConstants.LEFT, "Sans", Font.PLAIN, 20);
                        fileRow.get().setName(String.valueOf(fileID));
                        fileRow.get().addMouseListener(new MouseAdapter() {
                            public void mouseClicked(MouseEvent e) {
                                JPanel panel = (JPanel) e.getSource();
                                int fileID = Integer.parseInt(panel.getName());

                                for (FileDescriptor d : fileDescriptors) {
                                    if (d.getId() == fileID) {
                                        Window preview = PopUp(d.getName(), d.getType(), d.getData());
                                        preview.draw();
                                    }
                                }
                            }
                        });
                        fileRow.add(entry);
                        scrollPaneContainer.add(fileRow);
                        window.get().revalidate();
                    }
                    fileDescriptors.add(new FileDescriptor(fileID, fileName, fileContentBytes, getExtension(fileName)));
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    public static Window PopUp(String fileName, String fileExtension, byte[] fileData) {
        Window popUp = new Window("White Rabbit File Downloader", "File Downloader",
                "Are you sure you want to download " + fileName + "?\nFiles will be downloaded at the current folder.");
        Label fileContent;
        if (fileExtension.equalsIgnoreCase("txt")) {
            fileContent = new Label("<html>" + new String(fileData) + "</html>", SwingConstants.LEFT, "Sans",
                    Font.PLAIN, 12);
        } else {
            ImagePreview preview = new ImagePreview(fileData, 400);
            System.out.println("Preview:" + preview.get());
            fileContent = new Label(preview.get());
        }
        Container buttons = new Container(BoxLayout.X_AXIS);
        Button yes = new Button("Yes");
        Button no = new Button("No");
        buttons.add(yes);
        buttons.add(no);

        yes.get().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFile(fileName, fileData);
                popUp.close();
            }
        });
        no.get().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                popUp.close();
            }
        });
        popUp.add(fileContent);
        popUp.add(buttons);
        return popUp;
    }

    public static String getExtension(String fileName) {
        int i = fileName.lastIndexOf('.');
        return (i > 0) ? fileName.substring(i + 1) : "No extension found";
    }

    public static void saveFile(String fileName, byte[] fileData) {
        try {
            FileOutputStream output = new FileOutputStream(new File(fileName));
            output.write(fileData);
            output.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
