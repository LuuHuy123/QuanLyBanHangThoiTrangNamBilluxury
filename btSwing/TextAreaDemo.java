package bt;

import java.awt.*;
import javax.swing.*;

public class TextAreaDemo extends JFrame {
    private JTextArea textArea1, textArea2;

    // set up GUI
    public TextAreaDemo() {
        super("TextArea Demo");

        Box box = Box.createHorizontalBox();

        String string = "This is a demo string to\n"
                + "illustrate copying text\nfrom one textarea to \n"
                + "another textarea using an\nexternal event\n";

        // set up textarea1
        textArea1 = new JTextArea(string, 10, 15);
        box.add(new JScrollPane(textArea1));

        // set up textarea2 (thêm để hoàn chỉnh giao diện)
        textArea2 = new JTextArea(10, 15);
        box.add(new JScrollPane(textArea2));

        // add box to content pane
        Container container = getContentPane();
        container.add(box);

        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new TextAreaDemo();
    }
}