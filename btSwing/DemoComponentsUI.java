package bt;

import javax.swing.*;
import java.awt.*;

public class DemoComponentsUI extends JFrame {

    public DemoComponentsUI() {
        //JFRAME cửa sổ chính
        setTitle("Giao diện Test Tổng hợp Components");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        //MENU
        JMenuBar menuBar = new JMenuBar();
        JMenu menuFile = new JMenu("File");
        JMenuItem itemExit = new JMenuItem("Thoát");
        itemExit.addActionListener(e -> System.exit(0));
        menuFile.add(itemExit);
        menuBar.add(menuFile);
        setJMenuBar(menuBar);

        //PANEL TRÊN CÙNG: Chứa Label, Text Field và Button
        JPanel topPanel = new JPanel(new FlowLayout());
        JLabel label = new JLabel("Nhập tên của bạn:");
        JTextField textField = new JTextField(15);
        JButton button = new JButton("Gửi");
        
        topPanel.add(label);
        topPanel.add(textField);
        topPanel.add(button);
        add(topPanel, BorderLayout.NORTH);

        //PANEL BÊN TRÁI: Chứa List và Scrollbar rời
        JPanel leftPanel = new JPanel(new BorderLayout());
        String[] listItems = {"Mục 1", "Mục 2", "Mục 3", "Mục 4", "Mục 5"};
        JList<String> list = new JList<>(listItems);
        
        //JScrollBar (Thanh cuộn rời)
        JScrollBar scrollBar = new JScrollBar(JScrollBar.VERTICAL);
        
        leftPanel.add(new JLabel("Danh sách (JList)"), BorderLayout.NORTH);
        leftPanel.add(list, BorderLayout.CENTER);
        leftPanel.add(scrollBar, BorderLayout.EAST);
        leftPanel.setPreferredSize(new Dimension(150, 0));
        add(leftPanel, BorderLayout.WEST);

        //PANEL Ở GIỮA: Chứa Table 
        String[] columnNames = {"ID", "Tên", "Tuổi"};
        Object[][] data = {
                {1, "Nguyễn Văn A", 25},
                {2, "Trần Thị B", 22},
                {3, "Lê Văn C", 28},
                {4, "Phạm Thị D", 24}
        };
        JTable table = new JTable(data, columnNames);
        // Bảng thường được đặt trong JScrollPane để có thanh cuộn tự động
        JScrollPane tableScroll = new JScrollPane(table); 
        add(tableScroll, BorderLayout.CENTER);

        //PANEL DƯỚI CÙNG: Chứa Checkbox, ComboBox và Slider
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        //Checkbox
        JCheckBox checkBox = new JCheckBox("Đồng ý điều khoản");
        
        //Combo Box (Danh sách thả xuống)
        String[] comboOptions = {"Lựa chọn 1", "Lựa chọn 2", "Lựa chọn 3"};
        JComboBox<String> comboBox = new JComboBox<>(comboOptions);
        
        // Slider (Thanh trượt) từ 0 đến 100, giá trị mặc định là 50
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        slider.setMajorTickSpacing(20);
        slider.setMinorTickSpacing(5);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        bottomPanel.add(checkBox);
        bottomPanel.add(new JLabel("Combo:"));
        bottomPanel.add(comboBox);
        bottomPanel.add(new JLabel("Âm lượng:"));
        bottomPanel.add(slider);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    //Hàm Main để chạy thử trực tiếp file này
    public static void main(String[] args) {
        //SwingUtilities.invokeLater đảm bảo an toàn luồng (thread-safe) cho UI
        SwingUtilities.invokeLater(() -> {
            DemoComponentsUI demo = new DemoComponentsUI();
            demo.setVisible(true);
        });
    }
}

