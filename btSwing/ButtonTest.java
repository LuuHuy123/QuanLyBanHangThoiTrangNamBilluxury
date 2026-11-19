package bt;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ButtonTest extends JFrame {
    private JButton plainButton, fancyButton;

    // set up GUI
    public ButtonTest() {
        super("Testing Buttons");

        // get content pane and set its layout
        Container container = getContentPane();
        container.setLayout(new FlowLayout());

        // create buttons
        plainButton = new JButton("Plain Button");
        container.add(plainButton);

        Icon bug1 = new ImageIcon("bug1.gif");
        Icon bug2 = new ImageIcon("bug2.gif");

        fancyButton = new JButton("Fancy Button", bug1);
        fancyButton.setRolloverIcon(bug2);
        container.add(fancyButton);

        // create handler
        ButtonHandler handler = new ButtonHandler();
        fancyButton.addActionListener(handler);
        plainButton.addActionListener(handler);

        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // inner class for button event handling
    private class ButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            JOptionPane.showMessageDialog(
                ButtonTest.this,
                "You pressed: " + event.getActionCommand()
            );
        }
    }

    public static void main(String[] args) {
        new ButtonTest();
    }
}