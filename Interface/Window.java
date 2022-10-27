package Interface;

import java.awt.Font;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class Window {

    private JFrame window;
    private Label header, description;

    public Window(String name, String title, String text) {
        window = new JFrame(name);

        // Define window properties
        window.setSize(400, 400);
        window.setLayout(new BoxLayout(window.getContentPane(), BoxLayout.Y_AXIS));
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Title placement
        header = new Label(title, "Sans", Font.BOLD, 24);
        description = new Label(text, "Sans", Font.PLAIN, 12);
        window.add(header.get());
        window.add(description.get());
    }

    public void setDescription(String text) {
        description.get().setText(text);
    }

    public JFrame get() {
        return window;
    }

    public void draw() {
        window.setVisible(true);
    }

    public static void resetPanel(JPanel panel) {
        panel.removeAll();
        panel.revalidate();
        panel.repaint();
    }
}
