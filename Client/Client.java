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
        final File[] f = new File[1];
        Window window = new Window("White Rabbit Client", "White Rabbit", "Choose a file to send");

        // Button placement
        JPanel buttons = new JPanel();
        buttons.setBorder(new EmptyBorder(21, 0, 10, 0));
        Button choose = new Button("Choose file");
        Button send = new Button("Send file");
        Button cancel = new Button("Cancel");
        buttons.add(choose.get());

        choose.get().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser menu = new JFileChooser();
                menu.setDialogTitle("Choose a file to send");

                if (menu.showOpenDialog(window.get()) == JFileChooser.APPROVE_OPTION) {
                    f[0] = menu.getSelectedFile();
                    window.setDescription(f[0].getName());
                    window.resetPanel(buttons);
                    buttons.add(send.get());
                    buttons.add(cancel.get());
                }
        }
        });

        send.get().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (f[0] != null) {
                    try {
                        // Take file name and its length
                        String fileName = f[0].getName();
                        byte[] fileBytes = fileName.getBytes();

                        // Take file length and its contents
                        byte[] fileContents = new byte[(int) f[0].length()];

                        FileInputStream input = new FileInputStream(f[0]);
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
        });

        cancel.get().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.setDescription("Choose a file to send");
                window.resetPanel(buttons);
                buttons.add(choose.get());
            }
        });

        // Add elements to the window's frame
        window.get().add(buttons);
        window.draw();
    }
}
