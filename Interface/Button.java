package Interface;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Button {
    private JButton button;

    public Button(String label, String font) {
        button = new JButton(label);
        button.setFont(new Font(font, Font.PLAIN, 24));
        button.setPreferredSize(new Dimension(200, 65));
    }

    public JButton get() {
        return button;
    }
}
