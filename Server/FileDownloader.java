package Server;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.SwingConstants;

import Interface.Container;
import Interface.Button;
import Interface.Window;
import Interface.ImagePreview;
import Interface.Label;

public class FileDownloader {
    public static void main(Window window, FileDescriptor d) {

        Window preview = new Window("White Rabbit File Downloader", "File Downloader",
                "Are you sure you want to download " + d.getName() + " in the current folder?",
                window.getFont().getName(), new Dimension(600, 600));
        Container container = new Container(BoxLayout.Y_AXIS);
        if (d.getType().equalsIgnoreCase("jpg")
                || d.getType().equalsIgnoreCase("png")
                || d.getType().equalsIgnoreCase("gif")
                || d.getType().equalsIgnoreCase("jpeg")) {
            container.add(new Label(ImagePreview.fitImage(d.getData(), 400), SwingConstants.CENTER));
        }
        Container buttons = new Container(BoxLayout.X_AXIS);
        Button yes = new Button("Yes", window.getFont().getName());
        Button no = new Button("No", window.getFont().getName());
        buttons.add(yes);
        buttons.add(no);
        preview.add(buttons);

        yes.get().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFile(d.getName(), d.getData());
                preview.close();
            }
        });
        no.get().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                preview.close();
            }
        });
        window.draw();
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
