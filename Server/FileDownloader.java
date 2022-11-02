package Server;

import javax.swing.BoxLayout;

import Interface.Window;

public class FileDownloader {
    public static void main(Window window, FileDescriptor d) {

        Window preview = new Window("White Rabbit File Downloader", "File Downloader",
                "Are you sure you want to download " + d.getName() + " in the current folder?",
                new Dimension(600, 600));
        Container container = new Container(BoxLayout.Y_AXIS);
        if (d.getType().equalseIgnoreCase("jpg")
                || d.getType().equalseIgnoreCase("png")
                || d.getType().equalseIgnoreCase("gif")
                || d.getType().equalseIgnoreCase("jpeg")) {
            container.add(new Label(ImagePreview.fitImage(d.getData(), 400)));
        }
        Container buttons = new Container(BoxLayout.X_AXIS);
        Button yes = new Button("Yes");
        Button no = new Button("No");
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
