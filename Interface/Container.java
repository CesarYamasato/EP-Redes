package Interface;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Container {
    JPanel container = new JPanel();

    public Container(int orientation) {
        if (orientation == BoxLayout.Y_AXIS) {
            container.setLayout(new BoxLayout(spContainer, BoxLayout.Y_AXIS));
        } else {
            container.setBorder(new EmptyBorder(21, 0, 10, 0));
        }
    }

    public void add(Interface.Button b) {
        container.add(b.get());
    }

    public void add(Label l) {
        container.add(l.get());
    }

    public JPanel get() {
        return container;
    }

    public void reset() {
        container.removeAll();
        container.revalidate();
        container.repaint();
    }
}
