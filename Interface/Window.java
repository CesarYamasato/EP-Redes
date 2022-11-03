package Interface;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.ScrollPane;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

public class Window {

    private JFrame window;
    private Label header, description;
    private Container container;

    public Window(String name, String title, String text, String font, Dimension size) {
        window = new JFrame(name);

        // Define window properties
        window.setLayout(new BoxLayout(window.getContentPane(), BoxLayout.Y_AXIS));
        window.setMinimumSize(size);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Title placement
        header = new Label(title, SwingConstants.CENTER, font, Font.BOLD, 32);
        description = new Label(text, SwingConstants.CENTER, font, Font.PLAIN, 16);
        window.add(header.get());
        window.add(description.get());
    }

    public Window(String name, String title, String text, Font font, Dimension size) {
        window = new JFrame(name);

        // Define window properties
        window.setLayout(new BoxLayout(window.getContentPane(), BoxLayout.Y_AXIS));
        window.setMinimumSize(size);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Title placement
        header = new Label(title, SwingConstants.CENTER, font);
        description = new Label(text, SwingConstants.CENTER, font);
        window.add(header.get());
        window.add(description.get());
    }

    public void add(Container container) {
        this.container = container;
        window.add(container.get());
    }

    public void reset() {
        window.remove(container.get());
    }

    public void setDescription(String text) {
        description.get().setText(text);
    }

    public JFrame get() {
        return window;
    }

    public String getTitle() {
        return header.get().getText();
    }

    public Font getFont() {
        return description.get().getFont();
    }

    public void draw() {
        window.setVisible(true);
    }

    public void close() {
        window.dispose();
    }
}
