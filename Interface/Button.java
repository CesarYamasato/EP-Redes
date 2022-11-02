package Interface;

import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

public class Button {
    private JButton button;

    public Button(String label, String font) {
        button = new JButton(label);

        button.setPreferredSize(new Dimension(200, 48));
        button.setFont(new Font(font, Font.PLAIN, 24));
        button.setBorder(new EmptyBorder(3, 3, 3, 3));
    }

    public JButton get() {
        return button;
    }
}
