package Interface;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.ImageIcon;

// Função que descreve uma caixa de texto ou imagem, assim como o alinhamento e fonte (sendo o caso) destes nele contidos.

public class Label {
    private JLabel label;

    public Label(String text, int align, String font, int type, int size) {
        label = new JLabel(text, align);
        label.setFont(new Font(font, type, size));
        label.setBorder(new EmptyBorder(size / 5 * 4, 0, size / 2, 0));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    public Label(String text, int align, Font font) {
        label = new JLabel(text, align);
        label.setFont(font);
        label.setBorder(new EmptyBorder(font.getSize() / 5 * 4, 0, font.getSize() / 2, 0));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    public Label(ImageIcon image, int alignment) {
        label = new JLabel(image, alignment);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    public JLabel get() {
        return label;
    }
}
