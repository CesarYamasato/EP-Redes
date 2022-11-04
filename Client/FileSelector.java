package Client;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JFileChooser;

import Interface.Button;
import Interface.Container;
import Interface.Window;

public class FileSelector {
    private static File file = null;

    public static void main(Window window, String ip, String port) {

        // Desenho da tela
        window.reset();
        window.setDescription("Select a file to send to " + ip + " at port " + port);
        Container buttonContainer = new Container(new Dimension(200, 100), 2, 1, 10);
        Button choose = new Button("Choose file", window.getFont().getName());
        Button cancel = new Button("Cancel", window.getFont().getName());
        buttonContainer.add(choose);
        buttonContainer.add(cancel);
        window.add(buttonContainer);

        // Ações disparadas ao se pressionar os botões
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

    // Método privado que dispara a tela para seleção do arquivo
    private static File selectFile(Window window) {
        JFileChooser menu = new JFileChooser();
        menu.setDialogTitle("Choose a file to send");

        return (menu.showOpenDialog(window.get()) == JFileChooser.APPROVE_OPTION) ? menu.getSelectedFile() : null;
    }
}
