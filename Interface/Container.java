package Interface;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Container {
    JPanel container;

    public Container() {
        container = new JPanel();
        container.setBorder(new EmptyBorder(21, 0, 10, 0));
    }

    public void add(Interface.Button b) {
        container.add(b.get());
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
