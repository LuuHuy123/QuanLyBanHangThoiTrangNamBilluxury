package bt;

// Demonstrating mouse events.
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MouseTracker extends JFrame
        implements MouseListener, MouseMotionListener {

    private JLabel statusBar;

    // constructor
    public MouseTracker() {
        super("Demonstrating Mouse Events");

        statusBar = new JLabel();
        getContentPane().add(statusBar, BorderLayout.SOUTH);

        addMouseListener(this);         
        addMouseMotionListener(this);  

        setSize(275, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // thêm dòng này
        setVisible(true);
    }

    // handle event when mouse clicked
    public void mouseClicked(MouseEvent event) {
        statusBar.setText("Clicked at [" + event.getX() +
                ", " + event.getY() + "]");
    }

    public void mousePressed(MouseEvent event) {
        statusBar.setText("Pressed at [" + event.getX() +
                ", " + event.getY() + "]");
    }

    public void mouseReleased(MouseEvent event) {
        statusBar.setText("Released at [" + event.getX() +
                ", " + event.getY() + "]");
    }

    public void mouseEntered(MouseEvent event) {
        statusBar.setText("Mouse entered at [" + event.getX() +
                ", " + event.getY() + "]");
        getContentPane().setBackground(Color.GREEN);
    }

    public void mouseExited(MouseEvent event) {
        statusBar.setText("Mouse outside window");
        getContentPane().setBackground(Color.WHITE);
    }

    public void mouseDragged(MouseEvent event) {
        statusBar.setText("Dragged at [" + event.getX() +
                ", " + event.getY() + "]");
    }

    public void mouseMoved(MouseEvent event) {
        statusBar.setText("Moved at [" + event.getX() +
                ", " + event.getY() + "]");
    }

    // ====== MAIN ======
    public static void main(String[] args) {
        new MouseTracker();
    }
}