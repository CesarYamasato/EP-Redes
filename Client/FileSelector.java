package Client;

import javax.swing.JFileChooser;

public class FileSelector {
    public static void main(Window window, String ip, String port) {
        File file = null;
        window.reset();
        window.setDescription("Select a file to send to " + ip + " at port " + port);
        Container buttonContainer = new Container(BoxLayout.Y_AXIS);
        Button choose = new Button("Choose file", font);
        Button cancel = new Button("Cancel", font);
        buttonContainer.add(choose);
        buttonContainer.add(cancel);
        window.add(buttonContainer);

        choose.get().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((file = selectFile(window)) != null) {
                    FileSender.main(window, file, ip, port);
                }
            }
        });

        cancel.get().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Client.main(window);
            }
        });
        window.draw();
    }

    private static File selectFile(Window window) {
        JFileChooser menu = new JFileChooser();
        menu.setDialogTitle("Choose a file to send");

        return (menu.showOpenDialog(window.get()) == JFileChooser.APPROVE_OPTION) ? menu.getSelectedFile() : null;
    }
}
