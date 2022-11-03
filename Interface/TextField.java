package Interface;

import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class TextField {
    private JPanel panel;
    private JLabel label;
    private JTextField field;

    public TextField(String labelText, String defaultText, int inputLength, String font) {
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        JLabel label = new JLabel(labelText);
        label.setFont(new Font(font, Font.PLAIN, 12));
        label.setBorder(new EmptyBorder(0, 0, 0, 10));
        JTextField field = new JTextField(defaultText, inputLength);
        panel.add(label);
        panel.add(field);
    }

    public TextField(String labelText, String defaultText, int inputLength, Font font) {
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        JLabel label = new JLabel(labelText);
        label.setFont(font);
        label.setBorder(new EmptyBorder(0, 0, 0, 10));
        JTextField field = new JTextField(defaultText, inputLength);
        panel.add(label);
        panel.add(field);
    }

    public JPanel get() {
        return panel;
    }

    public String getText() {
        return field.getText();
    }
}
