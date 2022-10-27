import java.util.ArrayList
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.Font;
import Interface.*;

public class Server {
    static ArrayList<FileDescriptor> fd = new ArrayList<>();

    public static void main(String[] args) {
        int fileID = 0;

        Window window = new Window("White Rabbit Server", "White Rabbit", "Choose a file to download");

        JPanel spContainer = new JPanel();
        JPanel.setLayout(new BoxLayout(apContainer, BoxLayout.Y_AXIS));

        JScrollPane sp = new JScrollPane(spContainer);
        JScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        window.get().add(sp)
        window.draw(true);

        ServerSocket ss = new ServerSocket(25565);

        while (true) {
            try {
                Socket s = serverSocket.accept();
                DataInputStream input = new DataInputStream(socket.getInputStream());
                int fileNameLength = DataInputStream.readInt();

                if (fileNameLength > 0) {
                    byte[] fileNameBytes = new byte[fileNameLength];
                    input.readFully(fileNameBytes, 0, fileNameBytes.length);
                    String fileName = new String(fileNameBytes);

                    int fileContentLength = input.readInt();

                    if (fileContentLength > 0) {
                        byte[] fileContentBytes = new byte[fileContentLength];
                        input.readFully(fileContentBytes, 0, fileContentLength);

                        JPanel fileRow = new JPanel();
                        fileRow.setLayout(newBoxLayout(fileRow, BoxLayout.Y_AXIS));
                        Label filename = new Label(filename, "Sans", Font.PLAIN, 20);

                        if (getExtension(filename).equalsIgnoreCase("txt")) {
                            fileRow.setName(String.valueOf(fileID));
                            fileRow.addMouseListener(getMouseListener());
                            fileRow.add(filename);
                            spContainer.add()
                        }
                    }
                }
                
            }
        }
    }
 
}

public static String getExtension(String filename) {
    int i = filename.lastIndexOf('.');
    return (i > 0) ? filename.substring(i + 1) : "No extension found";
}
