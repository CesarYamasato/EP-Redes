package Interface;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import javax.swing.BoxLayout;
import java.awt.Font;

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

    public void draw(boolean b) {
        window.setVisible(b);
    }
}
