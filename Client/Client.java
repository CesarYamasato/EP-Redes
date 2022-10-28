import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import Interface.*;

public class Client {
    public static void main(String[] args) {
        final File[] file = new File[1];
        Window window = new Window("White Rabbit Client", "White Rabbit", "Choose a file to send");

        // Button placement
        JPanel buttonContainer = new JPanel();
        buttonContainer.setBorder(new EmptyBorder(21, 0, 10, 0));
        Button choose = new Button("Choose file");
        Button send = new Button("Send file");
        Button cancel = new Button("Cancel");
        buttonContainer.add(choose.get());

        choose.get().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                file[0] = selectFile(window);
                System.out.println(file[0]);
                if (file[0] != null) {
                    window.setDescription(file[0].getName());
                    window.resetPanel(buttonContainer);
                    buttonContainer.add(send.get());
                    buttonContainer.add(cancel.get());
                }
            }
        });

        send.get().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (file[0] != null) {
                   sendFile(file[0]);
                }
            }
        });

        cancel.get().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.setDescription("Choose a file to send");
                window.resetPanel(buttonContainer);
                buttonContainer.add(choose.get());
            }
        });

        // Add elements to the window's frame
        window.get().add(buttonContainer);
        window.draw();
    }

    public static File selectFile(Window window) {
        JFileChooser menu = new JFileChooser();
        menu.setDialogTitle("Choose a file to send");

        return (menu.showOpenDialog(window.get()) == JFileChooser.APPROVE_OPTION) ?
        menu.getSelectedFile() : null;
    }

    public static void sendFile(File file) {
        try {
            // Take file name and its length
            String fileName = file.getName();
            byte[] fileBytes = fileName.getBytes();

            // Take file length and its contents
            byte[] fileContents = new byte[(int) file.length()];

            FileInputStream input = new FileInputStream(file);
            input.read(fileContents);

            // Create socket and data output stream
            Socket s = new Socket("localhost", 25565);
            DataOutputStream output = new DataOutputStream(s.getOutputStream());

            // Send file name and its length
            output.writeInt(fileBytes.length);
            output.write(fileBytes);

            // Send file length and its contents
            output.writeInt(fileContents.length);
            output.write(fileContents);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
