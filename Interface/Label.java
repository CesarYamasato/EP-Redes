package Interface;

import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import javax.swing.ImageIcon;

public class Label {
    private JLabel label;

    public Label(String text, String font, int type, int size) {
        label = new JLabel(text);
        label.setFont(new Font(font, type, size));
        label.setBorder(new EmptyBorder(size / 5 * 4, 0, size / 2, 0));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    public Label(ImageIcon image) {
        label = new JLabel();
        label.setIcon(image);
    }

    public JLabel get() {
        return label;
    }
}
