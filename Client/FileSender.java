package Client;

import java.io.File;

public class FileSender {
    public static void main(Window window, File file, String ip, String port) {
        window.reset();
        window.setDescription("Send " + file.getName() + "?");
        Container buttonContainer = new Container(BoxLayout.Y_AXIS);
        Button send = new Button("Send", font);
        Button cancel = new Button("Cancel", font);
        window.add(buttonContainer);

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

            FileInputStream input = new FileInputStream(file);
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
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
