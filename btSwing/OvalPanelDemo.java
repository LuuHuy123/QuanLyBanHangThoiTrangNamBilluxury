package bt;

import java.awt.*;
import javax.swing.*;

// A customized JPanel class
class OvalPanel extends JPanel {
    private int diameter = 10;

    // draw an oval of the specified diameter
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.fillOval(10, 10, diameter, diameter);
    }

    // validate and set diameter, then repaint
    public void setDiameter(int newDiameter) {
        // if diameter invalid, default to 10
        diameter = (newDiameter >= 0 ? newDiameter : 10);
        repaint();
    }

    // used by layout manager to determine preferred size
    public Dimension getPreferredSize() {
        return new Dimension(200, 200);
    }

    // used by layout manager to determine minimum size
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }
}

// Class chạy thử
public class OvalPanelDemo extends JFrame {

    public OvalPanelDemo() {
        super("Oval Panel Demo");

        OvalPanel panel = new OvalPanel();

        // ví dụ set đường kính
        panel.setDiameter(100);

        add(panel);

        setSize(300, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new OvalPanelDemo();
    }
}
