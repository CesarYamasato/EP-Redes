package Interface;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

public class Container {
    JPanel container = new JPanel();

    public Container(float alignment) {
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setAlignmentX(alignment);
    }

    public Container(Dimension size, int rows, int columns, int gap) {
        container.setMaximumSize(size);
        container.setLayout(new GridLayout(rows, columns, gap, gap));
        container.setBorder(new EmptyBorder(gap, gap, gap, gap));
        container.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    public void setDimension(Dimension size) {
        container.setPreferredSize(size);
    }

    public void add(Button b) {
        container.add(b.get());
    }

    public void add(Label l) {
        container.add(l.get());
    }

    public void add(Container c) {
        container.add(c.get());
    }

    public void add(TextField t) {
        container.add(t.get());
    }

    public void add(Component c) {
        container.add(c);
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
