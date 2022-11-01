package Interface;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.ImageIcon;

public class Label {
    private JLabel label;

    public Label(String text, int align, String font, int type, int size) {
        label = new JLabel(text, align);
        label.setFont(new Font(font, type, size));
        label.setBorder(new EmptyBorder(size / 5 * 4, 0, size / 2, 0));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    public Label(ImageIcon image) {
        label = new JLabel(image);
        label.setSize(new Dimension(200, 200));
    }

    public JLabel get() {
        return label;
    }
}
