import java.awt.Font;
import javax.swing.JButton;

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
