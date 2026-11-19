package bt;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class CheckBox extends JFrame implements ItemListener {
    private JTextField field;
    private JCheckBox bold, italic;

    // set up GUI
    public CheckBox() {
        super("JCheckBox Test");

        // get content pane and set its layout
        Container container = getContentPane();
        container.setLayout(new FlowLayout());

        // set up JTextField and set its font
        field = new JTextField("Watch the font style change", 20);
        field.setFont(new Font("Serif", Font.PLAIN, 14));
        container.add(field);

        // create checkbox objects
        bold = new JCheckBox("Bold");
        container.add(bold);

        italic = new JCheckBox("Italic");
        container.add(italic);

        // register listeners for JCheckBoxes
        bold.addItemListener(this);
        italic.addItemListener(this);
    }

    // respond to checkbox events
    public void itemStateChanged(ItemEvent event) {
        int valBold = Font.PLAIN;
        int valItalic = Font.PLAIN;

        // process bold checkbox events
        if (event.getSource() == bold)
            valBold = bold.isSelected() ? Font.BOLD : Font.PLAIN;

        // process italic checkbox events
        if (event.getSource() == italic)
            valItalic = italic.isSelected() ? Font.ITALIC : Font.PLAIN;

        // set text field font
        field.setFont(new Font("Serif", valBold + valItalic, 14));
    }
    public static void main(String[] args) {
    CheckBox app = new CheckBox();
    app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    app.setSize(300, 100);
    app.setVisible(true);
    }
}
