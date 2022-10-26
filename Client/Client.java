import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.File;

public class Label {
    private JLabel label;

    public Label(String text, String font, int type, int size) {
        label = new JLabel(text);
        label.setFont(new Font(font, type, size));
        label.setBorder(new EmptyBorder(size / 5 * 4, 0, size / 2, 0));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    public JLabel get() {
        return label;
    }
}

public class Button {
    private JButton button;

    public Button(String label) {
        button = new JButton(label);
        button.setPreferedSize(new Dimension(150, 48));
        button.setFont(new Font("Sans", Font.PLAIN, 24));
    }

    public JButton get() {
        return button;
    }
}

public class Client {
    public static void main(String[] args) {
        final File[] f = new File[1];
        JFrame window = new JFrame("White Rabbit Client");

        // Define window properties

        window.setSize(450, 450);
        window.setLayout(new BoxLayout(window.getContentPane(), BoxLayout.Y_AXIS));
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Title placement
        Label title = new Label("White Rabbit", "Sans", FONT.BOLD, 24);
        Label text = new Label("Choose a file to send", "Sans", Font.PLAIN, 12);

        // Button placement
        JPanel buttons = new JPanel();
        buttons.setBorder(new EmptyBorder(21, 0, 10, 0));

        Button choose = new Button("Choose file");
        Button send = new Button("Send file");
        Button cancel = new Button("Cancel");

        buttons.add(choose);

        choose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser menu = new JFileChooser();
                menu.setDialogTitle("Choose a file to send");

                if (menu.showOpenDialog(window) == JFileChooser.APPROVE_OPTION) {
                    f[0] = menu.getSelectedFile();
                    text.get().setText(d[0].getName());
                }
            }
        });

        send.addActionListener(new ActionListener() {
            @Override
        });
    }
}
