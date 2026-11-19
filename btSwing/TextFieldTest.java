package bt;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TextFieldTest extends JFrame implements ActionListener {
    
    private JTextField textField1, textField2, textField3;
    private JPasswordField passwordField;

    public TextFieldTest() {
        super("Testing JTextField and JPasswordField");

        Container container = getContentPane();
        container.setLayout(new FlowLayout());

        textField1 = new JTextField(10);
        container.add(textField1);

        textField2 = new JTextField("Enter text here");
        container.add(textField2);

        textField3 = new JTextField("Uneditable text field", 20);
        textField3.setEditable(false);
        container.add(textField3);

        passwordField = new JPasswordField("Hidden text");
        container.add(passwordField);

        textField1.addActionListener(this);
        textField2.addActionListener(this);
        textField3.addActionListener(this);
        passwordField.addActionListener(this);

        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // xử lý sự kiện
    public void actionPerformed(ActionEvent event) {
        String string = "";

        if (event.getSource() == textField1)
            string = "textField1: " + event.getActionCommand();

        else if (event.getSource() == textField2)
            string = "textField2: " + event.getActionCommand();

        else if (event.getSource() == textField3)
            string = "textField3: " + event.getActionCommand();

        else if (event.getSource() == passwordField)
            string = "passwordField: " + new String(passwordField.getPassword());

        JOptionPane.showMessageDialog(this, string);
    }

    public static void main(String[] args) {
        new TextFieldTest();
    }
}