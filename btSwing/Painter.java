package bt;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Painter extends JFrame {

    private Point point = new Point();

    // set up GUI and register mouse event handler
    public Painter() {
        super("A simple paint program");

        // create a label and place it in SOUTH of BorderLayout
        getContentPane().add(
            new JLabel("Drag the mouse to draw"),
            BorderLayout.SOUTH
        );

        // register listener - using anonymous inner class
        addMouseMotionListener(new MouseMotionAdapter() {
            // store drag coordinates and repaint
            public void mouseDragged(MouseEvent event) {
                point = event.getPoint();
                repaint();
            }
        });

        setSize(400, 300);
    }

    // draw oval in a 4-by-4 bounding box at specified location
    public void paint(Graphics g) {
        super.paint(g); // rất quan trọng để không bị vẽ đè lỗi
        g.fillOval(point.x, point.y, 4, 4);
    }

    public static void main(String args[]) {
        Painter painter = new Painter();
        painter.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        painter.setVisible(true);
    }
}
