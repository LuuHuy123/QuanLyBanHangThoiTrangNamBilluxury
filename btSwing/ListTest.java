package bt;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

public class ListTest extends JFrame {
    private JList<String> colorList;
    private final String colorNames[] = {"Red", "Blue", "Green"};
    private final Color colors[] = {Color.RED, Color.BLUE, Color.GREEN};

    // set up GUI
    public ListTest() {
        super("List Test");

        // get content pane and set its layout
        Container container = getContentPane();
        container.setLayout(new FlowLayout());

        // create a list with items in colorNames array
        colorList = new JList<>(colorNames);
        colorList.setVisibleRowCount(5);

        // do not allow multiple selections
        colorList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // add a JScrollPane containing JList to content pane
        container.add(new JScrollPane(colorList));

        colorList.addListSelectionListener(
            new ListSelectionListener() { // anonymous inner class
                // handle list selection events
                public void valueChanged(ListSelectionEvent event) {
                    container.setBackground(
                        colors[colorList.getSelectedIndex()]
                    );
                }
            } // end anonymous inner class
        ); // end call to addListSelectionListener
    }

    // main để chạy
    public static void main(String[] args) {
        ListTest app = new ListTest();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setSize(300, 200);
        app.setVisible(true);
    }
}
