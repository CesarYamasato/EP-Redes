package Server;

import java.net.ServerSocket;

import Interface.Window;

public class FileListing {
    private static ArrayList<FileDescriptor> fileDescriptors = new ArrayList<>();
    private static boolean alive;

    public static void main(Window window, ServerSocket ss) {
        alive = true;
        window.reset();
        window.setDescription("Waiting for a connection");
        Container container = new Container(BoxLayout.Y_AXIS);
        JScrollPane scrollPane = new JScrollPane(scrollPaneContainer.get());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        Button cancel = new Button("Cancel", font);
        container.add(scrollPane);
        container.add(cancel);
        window.draw();

        cancel.get().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                alive = false;
                Server.main(window);
            }
        });

        while (alive)
            RecieveFiles(window, ss);
    }

    private void ReceiveFiles(Window window, ServerSocket ss) {
        int fileID = 0;
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
                    Label entry = new Label(fileName, SwingConstants.LEFT, window.getFont(), Font.PLAIN, 20);
                    fileRow.get().setName(String.valueOf(fileID));
                    fileRow.get().addMouseListener(new MouseAdapter() {
                        public void mouseClicked(MouseEvent e) {
                            JPanel panel = (JPanel) e.getSource();
                            int fileID = Integer.parseInt(panel.getName());

                            for (FileDescriptor d : fileDescriptors) {
                                if (d.getId() == fileID)
                                    FileDownloader.main(window, d);
                                {
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
