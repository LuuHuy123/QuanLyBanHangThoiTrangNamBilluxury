package bt;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ComboBoxTest extends JFrame {
    private JComboBox<String> imagesComboBox;
    private JLabel label;

    private String names[] = {"bug1.gif", "bug2.gif", "bug3.gif", "bug4.gif"};

    private Icon icons[] = {
        new ImageIcon(names[0]),
        new ImageIcon(names[1]),
        new ImageIcon(names[2]),
        new ImageIcon(names[3])
    };

    // constructor
    public ComboBoxTest() {
        super("Testing JComboBox");

        Container container = getContentPane();
        container.setLayout(new FlowLayout());

        // JLabel hiển thị ảnh
        label = new JLabel(icons[0]);
        container.add(label);

        // ====== ĐẶT JComboBox VÀO TRONG CONSTRUCTOR ======
        imagesComboBox = new JComboBox<>(names);
        imagesComboBox.setMaximumRowCount(3);

        imagesComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent event) {
                if (event.getStateChange() == ItemEvent.SELECTED) {
                    label.setIcon(icons[imagesComboBox.getSelectedIndex()]);
                }
            }
        });

        container.add(imagesComboBox);
    }

    // main để chạy
    public static void main(String[] args) {
        ComboBoxTest app = new ComboBoxTest();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setSize(300, 200);
        app.setVisible(true);
    }
}