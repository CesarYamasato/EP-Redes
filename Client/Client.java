import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.Component;
import java.awt.Font;
import java.io.File;

public class Label {
    public Label (String font, int class, int size) {
        title.setFont(new Font("Sans", Font.BOLD, 24));
        title.setBorder(new EmptyBorder(21, 0, 10, 0));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
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
        File f;
        JFrame window = new JFrame("White Rabbit Client");

        // Define window properties

        window.setSize(450, 450);
        window.setLayout(new BoxLayout(window.getContentPane(), BoxLayout.Y_AXIS));
        window.setDefaultCloseOperation(window.EXIT_ON_CLOSE);

        // Title placement
        JLabel title = new JLabel("White Rabbit");
        title.setFont(new Font("Sans", Font.BOLD, 24));
        title.setBorder(new EmptyBorder(21, 0, 10, 0));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // File description placement
        JLabel filename = new JLabel("Choose a file to send");
        filename.setFont(new Font("Sans", Font.PLAIN, 12));
        filename.setBorder(new EmptyBorder(50, 0, 0, 0));
        filename.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Button placement
        JPanel button = new JPanel();
        button.setBorder(new EmptyBorder(21, 0, 10, 0));

        JButton choose = new Button("Choose file");
        send.setPreferredSize(new Dimension(150, 48));
        send.setFont(new Font("Sans", Font.PLAIN, 24));

        JButton send = new Button("Send file");
        send.setPreferredSize(new Dimension(150, 48));
        send.setFont(new Font("Sans", Font.PLAIN, 24));

        JButton send = new Button("Cancel");
        send.setPreferredSize(new Dimension(150, 48));
        send.setFont(new Font("Sans", Font.PLAIN, 24));

    }
}
