import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.Container;
import java.awt.Font;
import java.io.IOException;
import java.net.ServerSocket;

import Interface.*;

public class Server {
    static ArrayList<FileDescriptor> fileDescriptors = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        int fileID = 0;

        Window window = new Window("White Rabbit Server", "White Rabbit", "Choose a file to download");

        Container scrollPaneContainer = new Container(BoxLayout.Y_AXIS);
        JScrollPane scrollPane = new JScrollPane(spContainer);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        window.add(scrollPane);
        window.draw();

        ServerSocket ss = new ServerSocket(25565);

        while (true) {
            try {

                // Receive files, if available.
                DataInputStream input = new DataInputStream(ss.accept().getInputStream());
                int fileNameLength = input.readInt();

                // if files were received, list it.
                if (fileNameLength > 0) {
                    byte[] fileNameBytes = new byte[fileNameLength];
                    input.readFully(fileNameBytes, 0, fileNameBytes.length);
                    String fileName = new String(fileNameBytes);

                    int fileContentLength = input.readInt();

                    if (fileContentLength > 0) {
                        byte[] fileContentBytes = new byte[fileContentLength];
                        input.readFully(fileContentBytes, 0, fileContentLength);
                        Container fileRow = new Container(BoxLayout.Y_AXIS);
                        Label entry = new Label(fileName, "Sans", Font.PLAIN, 20);
                        fileRow.get().setName(String.valueOf(fileID));
                        fileRow.get().addMouseListener(getMouseListener());
                        fileRow.get().add(entry);
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
                "Are you sure you want to download " + fileName);

        Label fileContent = (fileExtension.equalsIgnoreCase("txt"))
                ? new Label("<html>" + new String(fileData) + "</html>", "Sans", Font.PLAIN, 12)
                : new Label(new ImageIcon(fileData));
        Container buttons = new Container(BoxLayout.X_AXIS);
        Interface.Button yes = new Interface.Button("Yes");
        Interface.Button no = new Interface.Button("No");
        buttons.add(yes);
        buttons.add(no);

        yes.addActionListener(new addActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFile(fileName, fileData);
                popUp.close();
            }
        });
        no.addActionListener(new addActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                popUp.close();
            }
        });
        popUp.add(fileContent);
        popUp.add(buttons);
        return popUp;
    }

    public static MouseListener getMouseListener() {
        return new Mouselistener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JPanel panel = (JPanel) e.getSource();
                int fileID = Integer.parseInt(panel.getname());

                for (FileDescriptor d : fileDescriptors) {
                    if (d.getID() == fileID) {
                        Window preview = PopUp(d.getName(), d.getType(), d.getType());
                        preview.draw();
                    }
                }
            }
        };
    }

    public static String getExtension(String fileName) {
        return (fileName.lastIndexOf('.') > 0)
                ? fileName.substring(i + 1)
                : "No extension found";
    }

    public static saveFile(String fileName, byte [] fileData) {
        File file = new File(fileName);
        try {
            FileOutputStream output = new FileOutputStream(file);
            FileOutputStream.write(fileData);
            FileOutputStream.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
