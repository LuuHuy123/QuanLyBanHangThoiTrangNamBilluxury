package bt;

// Demonstrating BorderLayout.
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class BorderLayoutDemo extends JFrame implements ActionListener {
    private JButton buttons[];
    private final String names[] = {
        "Hide North", "Hide South",
        "Hide East", "Hide West", "Hide Center"
    };
    private BorderLayout layout;

    // set up GUI
    public BorderLayoutDemo() {
        super("BorderLayout Demo");

        layout = new BorderLayout(5, 5); // 5 pixel gaps

        // get content pane and set its layout
        Container container = getContentPane();
        container.setLayout(layout);

        // instantiate button objects
        buttons = new JButton[names.length];

        for (int count = 0; count < names.length; count++) {
            buttons[count] = new JButton(names[count]);
            buttons[count].addActionListener(this);
        }

        // place buttons in BorderLayout; order not important
        container.add(buttons[0], BorderLayout.NORTH);
        container.add(buttons[1], BorderLayout.SOUTH);
        container.add(buttons[2], BorderLayout.EAST);
        container.add(buttons[3], BorderLayout.WEST);
        container.add(buttons[4], BorderLayout.CENTER);

        setSize(300, 200);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // xử lý khi bấm button (ẩn/hiện vùng)
    public void actionPerformed(ActionEvent event) {
        for (int i = 0; i < buttons.length; i++) {
            if (event.getSource() == buttons[i]) {
                buttons[i].setVisible(false); // ẩn vùng đó
            } else {
                buttons[i].setVisible(true);  // hiện lại các vùng khác
            }
        }
        layout.layoutContainer(getContentPane()); // cập nhật layout
    }

    public static void main(String args[]) {
        new BorderLayoutDemo();
    }
}