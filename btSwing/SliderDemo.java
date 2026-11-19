package bt;

// Using JSliders to size an oval.
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

public class SliderDemo extends JFrame {
    private JSlider diameterSlider;
    private OvalPanel myPanel;

    // set up GUI
    public SliderDemo() {
        super("Slider Demo");

        // set up OvalPanel
        myPanel = new OvalPanel();
        myPanel.setBackground(Color.YELLOW);

        // set up JSlider to control diameter value
        diameterSlider = new JSlider(SwingConstants.HORIZONTAL, 0, 200, 10);
        diameterSlider.setMajorTickSpacing(10);
        diameterSlider.setPaintTicks(true);

        // register JSlider event listener
        diameterSlider.addChangeListener(
            new ChangeListener() { // anonymous inner class
                // handle change in slider value
                public void stateChanged(ChangeEvent e) {
                    myPanel.setDiameter(diameterSlider.getValue());
                }
            }
        ); // end call to addChangeListener

        // attach components to content pane
        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        container.add(diameterSlider, BorderLayout.SOUTH);
        container.add(myPanel, BorderLayout.CENTER);

        setSize(220, 270);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // main method
    public static void main(String[] args) {
        new SliderDemo();
    }
}

// class để vẽ hình oval
class OvalPanel extends JPanel {
    private int diameter = 10;

    public void setDiameter(int newDiameter) {
        diameter = newDiameter;
        repaint(); // vẽ lại khi thay đổi
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.fillOval(10, 10, diameter, diameter);
    }
}