package bt;

// Demonstrating FlowLayout alignments.
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class FlowLayoutDemo extends JFrame {
    private JButton leftButton, centerButton, rightButton;
    private Container container;
    private FlowLayout layout;

    // set up GUI and register button listeners
    public FlowLayoutDemo() {
        super("FlowLayout Demo");

        layout = new FlowLayout(); // mặc định CENTER

        // get content pane and set its layout
        container = getContentPane();
        container.setLayout(layout);

        // set up buttons
        leftButton = new JButton("Left");
        centerButton = new JButton("Center");
        rightButton = new JButton("Right");

        container.add(leftButton);
        container.add(centerButton);
        container.add(rightButton);

        // event: căn trái
        leftButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                layout.setAlignment(FlowLayout.LEFT);
                layout.layoutContainer(container);
            }
        });

        // event: căn giữa
        centerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                layout.setAlignment(FlowLayout.CENTER);
                layout.layoutContainer(container);
            }
        });

        // event: căn phải
        rightButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                layout.setAlignment(FlowLayout.RIGHT);
                layout.layoutContainer(container);
            }
        });

        setSize(300, 100);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new FlowLayoutDemo();
    }
}
