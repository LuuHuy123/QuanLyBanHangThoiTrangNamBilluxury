package bt;

import java.awt.*;
import javax.swing.*;

public class LabelTest extends JFrame {
    private JLabel label1, label2, label3;

    public LabelTest() {
        super("Testing JLabel");

        // get content pane and set layout
        Container container = getContentPane();
        container.setLayout(new FlowLayout());

        // JLabel 1: chỉ có text
        label1 = new JLabel("Label with text");
        label1.setToolTipText("This is label1");
        container.add(label1);

        // Tạo icon
        Icon bug = new ImageIcon("bug1.gif");

        // JLabel 2: text + icon
        label2 = new JLabel("Label with text and icon", bug, SwingConstants.LEFT);
        label2.setToolTipText("This is label2");
        container.add(label2);

        // JLabel 3: icon + text (text ở dưới)
        label3 = new JLabel();
        label3.setText("Label with icon and text at bottom");
        label3.setIcon(bug);
        label3.setHorizontalTextPosition(SwingConstants.CENTER);
        label3.setVerticalTextPosition(SwingConstants.BOTTOM);
        label3.setToolTipText("This is label3");
        container.add(label3);

        // Cài đặt frame
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new LabelTest();
    }
}