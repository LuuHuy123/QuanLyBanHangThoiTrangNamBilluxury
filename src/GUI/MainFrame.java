package GUI;

import database.DBConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel contentPanel;

    private SidebarItem btnDashboard;
    private SidebarItem btnNhanVien;
    private SidebarItem btnKhachHang;
    private SidebarItem btnHoaDon;
    private SidebarItem btnNhaCungCap;
    private SidebarItem btnHangHoa;
    private SidebarItem btnDoanhThu;

    private JLabel lblCurrentPage;
    private JLabel lblCurrentPageSub;
    private JLabel lblUserName;
    private JLabel lblUserRole;
    private JLabel lblDateTime;

    private Timer refreshTimer;

    private String currentTaiKhoan;
    private String currentVaiTro;
    private String currentMaNhanVien;

    private final DecimalFormat moneyFormat = new DecimalFormat("#,###");

    private final Color BG_DARK = new Color(14, 15, 17);
    private final Color BG_DARK_2 = new Color(22, 23, 26);
    private final Color SIDEBAR_DARK = new Color(11, 12, 14);
    private final Color CARD_DARK = new Color(32, 33, 36);
    private final Color CARD_DARK_2 = new Color(42, 39, 35);
    private final Color GOLD = new Color(192, 149, 94);
    private final Color GOLD_LIGHT = new Color(223, 196, 162);
    private final Color GOLD_DARK = new Color(124, 88, 49);
    private final Color TEXT_LIGHT = new Color(245, 239, 230);
    private final Color TEXT_MUTED = new Color(170, 163, 153);
    private final Color BORDER = new Color(84, 72, 56);

    private final Font FONT_NORMAL = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 14);

    public MainFrame() {
        this(
                System.getProperty("APP_TAI_KHOAN", "admin"),
                System.getProperty("APP_VAI_TRO", "ADMIN"),
                System.getProperty("APP_MA_NHAN_VIEN", "")
        );
    }

    public MainFrame(String vaiTro) {
        this("admin", vaiTro, "");
    }

    public MainFrame(String taiKhoan, String vaiTro, String maNhanVien) {
        this.currentTaiKhoan = taiKhoan == null || taiKhoan.isBlank() ? "admin" : taiKhoan;
        this.currentVaiTro = chuanHoaVaiTro(vaiTro);
        this.currentMaNhanVien = maNhanVien == null ? "" : maNhanVien;

        setTitle("BILLUXURY FASHION - Quản lý cửa hàng");
        setSize(1240, 760);
        setMinimumSize(new Dimension(1080, 680));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initLookAndFeel();
        initUI();
        startAutoRefresh();
    }

    private static String chuanHoaVaiTro(String vaiTro) {
        if (vaiTro == null || vaiTro.isBlank()) {
            return "ADMIN";
        }

        String v = vaiTro.trim().toUpperCase();
        if (v.equals("ADMIN") || v.equals("QUẢN LÝ") || v.equals("QUAN LY") || v.equals("QL")) {
            return "ADMIN";
        }

        return "STAFF";
    }

    private boolean laAdmin() {
        return "ADMIN".equalsIgnoreCase(currentVaiTro);
    }

    private void initLookAndFeel() {
        UIManager.put("Label.font", FONT_NORMAL);
        UIManager.put("Button.font", FONT_BOLD);
        UIManager.put("TextField.font", FONT_NORMAL);
        UIManager.put("PasswordField.font", FONT_NORMAL);
        UIManager.put("Table.font", FONT_NORMAL);
        UIManager.put("Table.rowHeight", 30);
        UIManager.put("TableHeader.font", FONT_BOLD);
        UIManager.put("OptionPane.messageFont", FONT_NORMAL);
        UIManager.put("OptionPane.buttonFont", FONT_BOLD);
    }

    private void initUI() {
        JPanel root = new BackgroundPanel();
        root.setLayout(new BorderLayout());

        root.add(createSidebar(), BorderLayout.WEST);
        root.add(createMainArea(), BorderLayout.CENTER);

        setContentPane(root);
        showPage("Dashboard");
    }

    private Image loadLogoImage(int w, int h) {
        try {
            java.net.URL url = getClass().getResource("/image/logo.png");

            ImageIcon icon;
            if (url != null) {
                icon = new ImageIcon(url);
            } else {
                File file = new File("src/image/logo.png");
                if (!file.exists()) return null;
                icon = new ImageIcon(file.getAbsolutePath());
            }

            Image raw = icon.getImage();
            BufferedImage source = new BufferedImage(
                    raw.getWidth(null),
                    raw.getHeight(null),
                    BufferedImage.TYPE_INT_ARGB
            );

            Graphics2D g = source.createGraphics();
            g.drawImage(raw, 0, 0, null);
            g.dispose();

            BufferedImage transparent = removeDarkLogoBackground(source);

            return transparent.getScaledInstance(w, h, Image.SCALE_SMOOTH);

        } catch (Exception e) {
            return null;
        }
    }

    private BufferedImage removeDarkLogoBackground(BufferedImage src) {
        BufferedImage out = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < src.getHeight(); y++) {
            for (int x = 0; x < src.getWidth(); x++) {
                int argb = src.getRGB(x, y);

                int a = (argb >> 24) & 0xff;
                int r = (argb >> 16) & 0xff;
                int g = (argb >> 8) & 0xff;
                int b = argb & 0xff;

                boolean darkBg = r < 45 && g < 45 && b < 45;
                boolean brownBg = r > 70 && r < 150 && g > 45 && g < 105 && b > 20 && b < 75;
                boolean edgeDark = r < 65 && g < 55 && b < 45;

                if (darkBg || brownBg || edgeDark) {
                    out.setRGB(x, y, 0x00000000);
                } else {
                    out.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
                }
            }
        }

        return out;
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(290, 0));
        sidebar.setBackground(SIDEBAR_DARK);
        sidebar.setBorder(new EmptyBorder(26, 22, 26, 22));

        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));

        top.add(createLogoBlock());
        top.add(Box.createVerticalStrut(30));

        btnDashboard = new SidebarItem("TỔNG QUAN HỆ THỐNG", "Dashboard");
        btnKhachHang = new SidebarItem("QUẢN LÝ KHÁCH HÀNG", "KhachHang");
        btnHoaDon = new SidebarItem("QUẢN LÝ HÓA ĐƠN", "HoaDon");
        btnHangHoa = new SidebarItem("QUẢN LÝ SẢN PHẨM", "HangHoa");
        btnDoanhThu = new SidebarItem("QUẢN LÝ DOANH THU", "DoanhThu");

        top.add(btnDashboard);
        top.add(Box.createVerticalStrut(22));
        top.add(createSectionLabel("CHỨC NĂNG CHÍNH"));

        if (laAdmin()) {
            btnNhanVien = new SidebarItem("QUẢN LÝ NHÂN VIÊN", "NhanVien");
            top.add(btnNhanVien);
            top.add(Box.createVerticalStrut(10));
        }

        top.add(btnKhachHang);
        top.add(Box.createVerticalStrut(10));
        top.add(btnHoaDon);
        top.add(Box.createVerticalStrut(10));
        top.add(btnHangHoa);
        top.add(Box.createVerticalStrut(10));
        top.add(btnDoanhThu);

        if (laAdmin()) {
            top.add(Box.createVerticalStrut(22));
            top.add(createSectionLabel("NGUỒN LỰC"));
            btnNhaCungCap = new SidebarItem("QUẢN LÝ NHÀ CUNG CẤP", "NhaCungCap");
            top.add(btnNhaCungCap);
        }

        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));

        JLabel badge = new JLabel(laAdmin() ? "  ADMIN MODE  " : "  STAFF MODE  ");
        badge.setOpaque(true);
        badge.setBackground(new Color(192, 149, 94, 55));
        badge.setForeground(GOLD_LIGHT);
        badge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        badge.setBorder(new EmptyBorder(5, 8, 5, 8));
        badge.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel footer = new JLabel("© 2025 BiLuxury Fashion");
        footer.setFont(new Font("Segoe UI", Font.BOLD, 11));
        footer.setForeground(TEXT_MUTED);
        footer.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel footer2 = new JLabel("Premium Admin v2.0");
        footer2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        footer2.setForeground(new Color(115, 109, 101));
        footer2.setAlignmentX(Component.LEFT_ALIGNMENT);

        bottom.add(badge);
        bottom.add(Box.createVerticalStrut(10));
        bottom.add(footer);
        bottom.add(Box.createVerticalStrut(3));
        bottom.add(footer2);

        sidebar.add(top, BorderLayout.NORTH);
        sidebar.add(bottom, BorderLayout.SOUTH);

        return sidebar;
    }

    private JPanel createLogoBlock() {
        JPanel block = new JPanel(new BorderLayout(14, 0));
        block.setOpaque(false);
        block.setMaximumSize(new Dimension(245, 82));
        block.setAlignmentX(Component.LEFT_ALIGNMENT);

        LogoPanel logo = new LogoPanel();
        logo.setPreferredSize(new Dimension(64, 70));

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));

        JLabel line1 = new JLabel("BILLUXURY");
        line1.setFont(new Font("Segoe UI", Font.BOLD, 25));
        line1.setForeground(Color.WHITE);

        JLabel line2 = new JLabel("FASHION");
        line2.setFont(new Font("Segoe UI", Font.BOLD, 15));
        line2.setForeground(GOLD_LIGHT);

        JLabel line3 = new JLabel("MEN'S WEAR MANAGEMENT");
        line3.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        line3.setForeground(TEXT_MUTED);

        text.add(Box.createVerticalStrut(6));
        text.add(line1);
        text.add(Box.createVerticalStrut(2));
        text.add(line2);
        text.add(Box.createVerticalStrut(3));
        text.add(line3);

        block.add(logo, BorderLayout.WEST);
        block.add(text, BorderLayout.CENTER);

        return block;
    }

    private JLabel createSectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 10));
        label.setForeground(new Color(145, 137, 126));
        label.setBorder(new EmptyBorder(4, 10, 10, 0));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setMaximumSize(new Dimension(245, 28));
        return label;
    }

    private JPanel createMainArea() {
        JPanel main = new JPanel(new BorderLayout());
        main.setOpaque(false);

        main.add(createTopBar(), BorderLayout.NORTH);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(20, 26, 8, 26));

        contentPanel.add(createDashboardPage(), "Dashboard");

        if (laAdmin()) {
            contentPanel.add(wrapContentPanel(new NhanVienPanel()), "NhanVien");
            contentPanel.add(wrapContentPanel(new NhaCungCapPanel()), "NhaCungCap");
        }

        contentPanel.add(wrapContentPanel(new KhachHangPanel(currentVaiTro)), "KhachHang");
        contentPanel.add(wrapContentPanel(new HoaDonPanel(currentVaiTro)), "HoaDon");
        contentPanel.add(wrapContentPanel(new SanPhamPanel(currentVaiTro)), "HangHoa");
        contentPanel.add(wrapContentPanel(new DoanhThuPanel()), "DoanhThu");

        main.add(contentPanel, BorderLayout.CENTER);
        main.add(createBottomClockBar(), BorderLayout.SOUTH);

        return main;
    }

    private JPanel createTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.setPreferredSize(new Dimension(0, 76));
        topBar.setBorder(new EmptyBorder(14, 28, 8, 28));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        lblCurrentPage = new JLabel("TRUNG TÂM HỆ THỐNG");
        lblCurrentPage.setFont(new Font("Segoe UI", Font.BOLD, 21));
        lblCurrentPage.setForeground(Color.WHITE);

        lblCurrentPageSub = new JLabel("THEO DÕI VÀ QUẢN LÝ TOÀN BỘ HỆ THỐNG");
        lblCurrentPageSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblCurrentPageSub.setForeground(TEXT_MUTED);

        left.add(lblCurrentPage);
        left.add(Box.createVerticalStrut(3));
        left.add(lblCurrentPageSub);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        right.setOpaque(false);

        JButton btnBell = new BellButton();
        btnBell.setPreferredSize(new Dimension(42, 42));
        btnBell.addActionListener(e -> hienThiThongBao());

        JPanel avatar = new AvatarPanel();
        avatar.setPreferredSize(new Dimension(42, 42));

        JPanel userBox = new JPanel();
        userBox.setOpaque(false);
        userBox.setLayout(new BoxLayout(userBox, BoxLayout.Y_AXIS));

        lblUserName = new JLabel(laAdmin() ? "Admin" : currentTaiKhoan);
        lblUserName.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblUserName.setForeground(Color.WHITE);

        lblUserRole = new JLabel(laAdmin() ? "Quản trị viên" : "Nhân viên");
        lblUserRole.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblUserRole.setForeground(TEXT_MUTED);

        userBox.add(lblUserName);
        userBox.add(lblUserRole);

        JButton logout = createLogoutButton();
        logout.addActionListener(e -> {
            if (refreshTimer != null) refreshTimer.stop();
            ghiLichSu("Đăng xuất hệ thống");
            dispose();
            new LoginFrame().setVisible(true);
        });

        right.add(btnBell);
        right.add(createVerticalDivider());
        right.add(avatar);
        right.add(userBox);
        right.add(Box.createHorizontalStrut(4));
        right.add(logout);

        topBar.add(left, BorderLayout.WEST);
        topBar.add(right, BorderLayout.EAST);

        return topBar;
    }

    private JPanel createBottomClockBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setPreferredSize(new Dimension(0, 44));
        bar.setBackground(new Color(22, 20, 19));
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(70, 50, 32)),
                new EmptyBorder(0, 26, 0, 26)
        ));

        lblDateTime = new JLabel();
        lblDateTime.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblDateTime.setForeground(GOLD_LIGHT);

        JLabel welcome = new JLabel("BiLuxury Fashion Management System");
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 12));
        welcome.setForeground(TEXT_MUTED);

        bar.add(lblDateTime, BorderLayout.WEST);
        bar.add(welcome, BorderLayout.EAST);

        updateDateTime();

        return bar;
    }

    private void updateDateTime() {
        if (lblDateTime == null) return;
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy  |  HH:mm:ss");
        lblDateTime.setText(now.format(fmt));
    }

    private JPanel createVerticalDivider() {
        JPanel sep = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(255, 255, 255, 28));
                g2.fillRect(0, 8, 1, getHeight() - 16);
                g2.dispose();
            }
        };
        sep.setOpaque(false);
        sep.setPreferredSize(new Dimension(1, 44));
        return sep;
    }

    private JButton createLogoutButton() {
        JButton btn = new JButton("ĐĂNG XUẤT");
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btn.setPreferredSize(new Dimension(128, 40));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(new Color(39, 37, 34));
        btn.setForeground(GOLD_LIGHT);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(true);
        btn.setMargin(new Insets(7, 14, 7, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createLineBorder(GOLD_DARK, 1));
        return btn;
    }

    private JPanel wrapContentPanel(JPanel panel) {
        styleDarkPanel(panel);

        JPanel outer = new JPanel(new BorderLayout());
        outer.setOpaque(false);

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(28, 25, 22));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                new EmptyBorder(16, 18, 18, 18)
        ));

        card.add(panel, BorderLayout.CENTER);
        outer.add(card, BorderLayout.CENTER);

        // Re-apply style sau khi UI được hiển thị (fix Windows LnF override)
        SwingUtilities.invokeLater(() -> styleDarkPanel(panel));

        return outer;
    }

    private void styleDarkPanel(Component comp) {
        // === JPanel ===
        if (comp instanceof JPanel panel) {
            panel.setBackground(new Color(28, 25, 22));
            panel.setForeground(TEXT_LIGHT);
        }

        // === JLabel ===
        if (comp instanceof JLabel label) {
            label.setForeground(TEXT_LIGHT);
            label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            // Không ép căn giữa toàn bộ label để form nhập liệu nhìn tự nhiên hơn.
            // Các label cần căn giữa như tiêu đề bảng/card đã tự set riêng ở nơi tạo.
        }

        // === JTextField ===
        if (comp instanceof JTextField txt && !(comp instanceof JPasswordField)) {
            txt.setBackground(new Color(48, 43, 38));
            txt.setForeground(TEXT_LIGHT);
            txt.setCaretColor(GOLD_LIGHT);
            txt.setOpaque(true);
            txt.setHorizontalAlignment(SwingConstants.LEFT);
            txt.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(GOLD_DARK, 1),
                    new EmptyBorder(5, 8, 5, 8)
            ));
        }

        // === JPasswordField ===
        if (comp instanceof JPasswordField pwd) {
            pwd.setBackground(new Color(48, 43, 38));
            pwd.setForeground(TEXT_LIGHT);
            pwd.setCaretColor(GOLD_LIGHT);
            pwd.setOpaque(true);
            pwd.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(GOLD_DARK, 1),
                    new EmptyBorder(5, 8, 5, 8)
            ));
        }

        // === JTextArea ===
        if (comp instanceof JTextArea area) {
            area.setBackground(new Color(48, 43, 38));
            area.setForeground(TEXT_LIGHT);
            area.setCaretColor(GOLD_LIGHT);
            area.setOpaque(true);
        }

        // === JComboBox ===
        if (comp instanceof JComboBox<?> combo) {
            combo.setUI(new javax.swing.plaf.basic.BasicComboBoxUI());
            combo.setBackground(new Color(48, 43, 38));
            combo.setForeground(TEXT_LIGHT);
            combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            combo.setOpaque(true);
            combo.setFocusable(false);
            combo.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                              boolean isSelected, boolean cellHasFocus) {
                    Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    c.setBackground(isSelected ? new Color(124, 88, 49) : new Color(48, 43, 38));
                    c.setForeground(TEXT_LIGHT);
                    if (c instanceof JLabel l) {
                        l.setHorizontalAlignment(SwingConstants.CENTER);
                    }
                    return c;
                }
            });
            combo.setBorder(BorderFactory.createLineBorder(GOLD_DARK, 1));
        }

        // === JCheckBox ===
        if (comp instanceof JCheckBox chk) {
            chk.setBackground(new Color(28, 25, 22));
            chk.setForeground(TEXT_LIGHT);
            chk.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        }

        // === JButton ===
        if (comp instanceof JButton btn) {
            String text = btn.getText() == null ? "" : btn.getText().toLowerCase();
            btn.setUI(new javax.swing.plaf.basic.BasicButtonUI());
            btn.setOpaque(true);
            btn.setContentAreaFilled(true);
            btn.setBorderPainted(true);
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
                btn.setMargin(new Insets(7, 14, 7, 14));

            if (text.contains("xóa") || text.contains("hủy")) {
                btn.setBackground(new Color(210, 65, 55));
                btn.setForeground(Color.WHITE);
            } else if (text.contains("sửa")) {
                btn.setBackground(new Color(70, 145, 210));
                btn.setForeground(Color.WHITE);
            } else if (text.contains("tìm") || text.contains("kiếm")) {
                btn.setBackground(new Color(192, 149, 94));
                btn.setForeground(new Color(20, 15, 10));
            } else if (text.contains("làm mới") || text.contains("hiển thị") || text.contains("tất cả")) {
                btn.setBackground(new Color(80, 72, 62));
                btn.setForeground(TEXT_LIGHT);
            } else if (text.contains("sắp hết")) {
                btn.setBackground(new Color(160, 80, 40));
                btn.setForeground(Color.WHITE);
            } else {
                btn.setBackground(new Color(192, 149, 94));
                btn.setForeground(new Color(20, 15, 10));
            }
            btn.setBorder(BorderFactory.createLineBorder(GOLD_DARK, 1));
        }

        // === JTable ===
        if (comp instanceof JTable table) {
            table.setBackground(new Color(35, 32, 29));
            table.setForeground(TEXT_LIGHT);
            table.setGridColor(new Color(90, 70, 45));
            table.setSelectionBackground(new Color(124, 88, 49));
            table.setSelectionForeground(Color.WHITE);
            table.setShowVerticalLines(true);
            table.setShowHorizontalLines(true);
            table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            table.setRowHeight(30);
            table.setFillsViewportHeight(true);

            table.getTableHeader().setBackground(new Color(45, 38, 31));
            table.getTableHeader().setForeground(GOLD_LIGHT);
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
            table.getTableHeader().setReorderingAllowed(false);
            table.getTableHeader().setPreferredSize(new Dimension(0, 36));
            ((javax.swing.table.DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

            table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable t, Object value,
                        boolean isSelected, boolean hasFocus, int row, int column) {
                    super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, column);
                    setHorizontalAlignment(SwingConstants.CENTER);
                    if (isSelected) {
                        setBackground(new Color(124, 88, 49));
                        setForeground(Color.WHITE);
                    } else {
                        setBackground(row % 2 == 0 ? new Color(35, 32, 29) : new Color(42, 39, 35));
                        setForeground(TEXT_LIGHT);
                    }
                    setBorder(new EmptyBorder(4, 8, 4, 8));
                    setFont(new Font("Segoe UI", Font.PLAIN, 13));
                    return this;
                }
            });
        }

        // === JScrollPane ===
        if (comp instanceof JScrollPane scroll) {
            scroll.setBackground(new Color(28, 25, 22));
            scroll.getViewport().setBackground(new Color(28, 25, 22));
            scroll.setBorder(BorderFactory.createLineBorder(BORDER, 1));
            // Style scrollbar
            scroll.getVerticalScrollBar().setBackground(new Color(35, 32, 29));
            scroll.getHorizontalScrollBar().setBackground(new Color(35, 32, 29));
        }

        // === TitledBorder ===
        if (comp instanceof JComponent jc) {
            if (jc.getBorder() instanceof TitledBorder tb) {
                tb.setTitleColor(GOLD_LIGHT);
                tb.setTitleFont(new Font("Segoe UI", Font.BOLD, 13));
            }
        }

        // === Recurse vào children ===
        if (comp instanceof Container container) {
            for (Component child : container.getComponents()) {
                styleDarkPanel(child);
            }
        }
    }

    private JPanel createDashboardPage() {
        JPanel page = new JPanel(new GridBagLayout());
        page.setOpaque(false);

        JPanel body = new JPanel(new BorderLayout(0, 12));
        body.setOpaque(false);
        body.setPreferredSize(new Dimension(1180, laAdmin() ? 640 : 610));
        body.setMaximumSize(new Dimension(1180, 650));

        JPanel hero = createHeroBanner();
        hero.setPreferredSize(new Dimension(0, laAdmin() ? 190 : 175));

        JLabel quickTitle = new JLabel("TRUY CẬP NHANH", SwingConstants.CENTER);
        quickTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        quickTitle.setForeground(TEXT_LIGHT);
        quickTitle.setBorder(new EmptyBorder(0, 0, 2, 0));

        JPanel titleWrap = new JPanel(new BorderLayout());
        titleWrap.setOpaque(false);
        titleWrap.add(quickTitle, BorderLayout.CENTER);

        JPanel quickGrid = createQuickAccessGrid();

        JPanel center = new JPanel(new BorderLayout(0, 10));
        center.setOpaque(false);
        center.add(titleWrap, BorderLayout.NORTH);
        center.add(quickGrid, BorderLayout.CENTER);

        body.add(hero, BorderLayout.NORTH);
        body.add(center, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.NORTH;
        page.add(body, gbc);

        return page;
    }

    private JPanel createHeroBanner() {
        JPanel hero = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(30, 26, 23),
                        getWidth(), getHeight(), new Color(10, 10, 12)
                );
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 26, 26);

                g2.setColor(new Color(192, 149, 94, 70));
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 26, 26);

                g2.setColor(new Color(192, 149, 94, 30));
                g2.fillOval(getWidth() - 230, -90, 330, 330);

                g2.dispose();
            }
        };

        hero.setOpaque(false);
        hero.setBorder(new EmptyBorder(22, 34, 22, 34));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("BILLUXURY FASHION");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(Color.WHITE);

        JLabel sub = new JLabel("Premium Men’s Wear Management System");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        sub.setForeground(GOLD_LIGHT);

        JLabel desc = new JLabel("WEAR THE LUXURY!");
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        desc.setForeground(TEXT_MUTED);

        JLabel role = new JLabel("Đang đăng nhập: " + (laAdmin() ? "ADMIN" : "NHÂN VIÊN") + "  •  " + currentTaiKhoan);
        role.setFont(new Font("Segoe UI", Font.BOLD, 12));
        role.setForeground(GOLD_LIGHT);
        role.setBorder(new EmptyBorder(10, 0, 0, 0));

        left.add(Box.createVerticalGlue());
        left.add(title);
        left.add(Box.createVerticalStrut(5));
        left.add(sub);
        left.add(Box.createVerticalStrut(10));
        left.add(desc);
        left.add(role);
        left.add(Box.createVerticalGlue());

        LogoLargePanel logo = new LogoLargePanel();
        logo.setPreferredSize(new Dimension(190, 150));

        hero.add(left, BorderLayout.CENTER);
        hero.add(logo, BorderLayout.EAST);

        return hero;
    }

    private JPanel createQuickAccessGrid() {
        JPanel grid = new JPanel(new GridLayout(laAdmin() ? 2 : 2, laAdmin() ? 3 : 2, 16, 16));
        grid.setOpaque(false);
        grid.setPreferredSize(new Dimension(0, laAdmin() ? 330 : 310));

        if (laAdmin()) {
            grid.add(new ActionCard("QUẢN LÝ NHÂN VIÊN", "Theo dõi hồ sơ, thông tin làm việc và quyền nhân sự.", "NhanVien", "👨"));
        }

        grid.add(new ActionCard("QUẢN LÝ KHÁCH HÀNG", "Tra cứu, cập nhật thông tin và điểm tích lũy khách hàng.", "KhachHang", "👥"));
        grid.add(new ActionCard("QUẢN LÝ HÓA ĐƠN", "Tạo hóa đơn, xác nhận giao dịch và theo dõi trạng thái.", "HoaDon", "🧾"));
        grid.add(new ActionCard("QUẢN LÝ SẢN PHẨM", "Xem sản phẩm, tồn kho, giá nhập và giá bán.", "HangHoa", "📦"));
        grid.add(new ActionCard("QUẢN LÝ DOANH THU", "Xem doanh thu theo ngày, tháng, năm và tìm kiếm theo ngày.", "DoanhThu", "📊"));

        if (laAdmin()) {
            grid.add(new ActionCard("QUẢN LÝ NHÀ CUNG CẤP", "Quản lý nguồn hàng, đơn vị cung cấp và người liên hệ.", "NhaCungCap", "🚚"));
        }

        return grid;
    }

    private static final String[][] PAGE_META = {
            {"Dashboard", "TRUNG TÂM HỆ THỐNG", "THEO DÕI VÀ QUẢN LÝ TOÀN BỘ HỆ THỐNG"},
            {"NhanVien", "QUẢN LÝ NHÂN VIÊN", "Quản lý hồ sơ và thông tin nhân viên"},
            {"KhachHang", "QUẢN LÝ KHÁCH HÀNG", "Tra cứu và quản lý thông tin khách hàng"},
            {"HoaDon", "QUẢN LÝ HÓA ĐƠN", "Tạo hóa đơn và quản lý giao dịch"},
            {"NhaCungCap", "QUẢN LÝ NHÀ CUNG CẤP", "Quản lý nhà cung cấp và nguồn hàng"},
            {"HangHoa", "QUẢN LÝ SẢN PHẨM", "Theo dõi sản phẩm và tồn kho"},
            {"DoanhThu", "QUẢN LÝ DOANH THU", "Thống kê doanh thu theo ngày, tháng, năm"}
    };

    private void showPage(String pageName) {
        if (!laAdmin() && ("NhanVien".equals(pageName) || "NhaCungCap".equals(pageName))) {
            AppDialog.showWarning(this, "Không có quyền", "Tài khoản nhân viên không có quyền truy cập chức năng này!");
            return;
        }

        cardLayout.show(contentPanel, pageName);
        setSelectedMenu(pageName);
        updatePageTitle(pageName);
    }

    private void setSelectedMenu(String pageName) {
        if (btnDashboard != null) btnDashboard.setSelectedState("Dashboard".equals(pageName));
        if (btnNhanVien != null) btnNhanVien.setSelectedState("NhanVien".equals(pageName));
        if (btnKhachHang != null) btnKhachHang.setSelectedState("KhachHang".equals(pageName));
        if (btnHoaDon != null) btnHoaDon.setSelectedState("HoaDon".equals(pageName));
        if (btnNhaCungCap != null) btnNhaCungCap.setSelectedState("NhaCungCap".equals(pageName));
        if (btnHangHoa != null) btnHangHoa.setSelectedState("HangHoa".equals(pageName));
        if (btnDoanhThu != null) btnDoanhThu.setSelectedState("DoanhThu".equals(pageName));
    }

    private void updatePageTitle(String pageName) {
        for (String[] meta : PAGE_META) {
            if (meta[0].equals(pageName)) {
                lblCurrentPage.setText(meta[1]);
                lblCurrentPageSub.setText(meta[2]);
                return;
            }
        }
    }

    private void startAutoRefresh() {
        refreshTimer = new Timer(1000, e -> updateDateTime());
        refreshTimer.start();
    }

    private void hienThiThongBao() {
        JDialog dialog = new JDialog(this, "Thông báo hoạt động", true);
        dialog.setSize(520, 430);
        dialog.setLocationRelativeTo(this);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_DARK);
        root.setBorder(new EmptyBorder(20, 22, 20, 22));

        JLabel title = new JLabel("THÔNG BÁO HOẠT ĐỘNG");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(GOLD_LIGHT);
        title.setBorder(new EmptyBorder(0, 0, 14, 0));

        DefaultListModel<String> model = new DefaultListModel<>();
        napThongBao(model);

        JList<String> list = new JList<>(model);
        list.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        list.setBackground(CARD_DARK);
        list.setForeground(TEXT_LIGHT);
        list.setSelectionBackground(new Color(70, 58, 42));
        list.setSelectionForeground(Color.WHITE);
        list.setFixedCellHeight(38);
        list.setBorder(new EmptyBorder(8, 8, 8, 8));

        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER, 1));

        JButton close = new JButton("ĐÓNG");
        close.setFont(FONT_BOLD);
        close.setBackground(GOLD_LIGHT);
        close.setForeground(Color.BLACK);
        close.setFocusPainted(false);
        close.setOpaque(true);
        close.setContentAreaFilled(true);
        close.setPreferredSize(new Dimension(100, 36));
        close.addActionListener(e -> dialog.dispose());

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setOpaque(false);
        bottom.add(close);

        root.add(title, BorderLayout.NORTH);
        root.add(scroll, BorderLayout.CENTER);
        root.add(bottom, BorderLayout.SOUTH);

        dialog.add(root);
        dialog.setVisible(true);
    }

    private void napThongBao(DefaultListModel<String> model) {
        String sql = """
                SELECT TOP 20 taiKhoan, hanhDong, thoiGian
                FROM LichSuHoatDong
                ORDER BY thoiGian DESC
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String tk = rs.getString("taiKhoan");
                String hd = rs.getString("hanhDong");
                Timestamp ts = rs.getTimestamp("thoiGian");

                String timeText = "";
                if (ts != null) {
                    timeText = " - " + tinhThoiGian(ts.toLocalDateTime());
                }

                model.addElement(tk + ": " + hd + timeText);
            }

            if (model.isEmpty()) {
                model.addElement("Chưa có thông báo hoạt động nào.");
            }

        } catch (Exception e) {
            model.addElement("Không đọc được thông báo: " + e.getMessage());
        }
    }

    private String tinhThoiGian(LocalDateTime time) {
        Duration d = Duration.between(time, LocalDateTime.now());

        long minutes = d.toMinutes();
        long hours = d.toHours();
        long days = d.toDays();

        if (minutes < 1) return "vừa xong";
        if (minutes < 60) return minutes + " phút trước";
        if (hours < 24) return hours + " giờ trước";
        return days + " ngày trước";
    }

    private void ghiLichSu(String hanhDong) {
        String sql = """
                INSERT INTO LichSuHoatDong
                (taiKhoan, vaiTro, hanhDong)
                VALUES (?, ?, ?)
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, currentTaiKhoan);
            ps.setString(2, currentVaiTro);
            ps.setString(3, hanhDong);
            ps.executeUpdate();

        } catch (Exception ignored) {
        }
    }

    private class SidebarItem extends JButton {
        private boolean selected = false;

        SidebarItem(String text, String pageName) {
            super(text);

            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setForeground(TEXT_MUTED);
            setBackground(SIDEBAR_DARK);
            setHorizontalAlignment(SwingConstants.CENTER);
            setMaximumSize(new Dimension(245, 48));
            setPreferredSize(new Dimension(245, 48));
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBorder(new EmptyBorder(0, 16, 0, 16));
            setContentAreaFilled(false);
            setOpaque(false);

            addActionListener(e -> showPage(pageName));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!selected) setForeground(Color.WHITE);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (!selected) setForeground(TEXT_MUTED);
                }
            });
        }

        void setSelectedState(boolean selected) {
            this.selected = selected;
            setForeground(selected ? Color.WHITE : TEXT_MUTED);
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (selected) {
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(192, 149, 94, 180),
                        getWidth(), 0, new Color(124, 88, 49, 120)
                );
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
            } else {
                g2.setColor(new Color(255, 255, 255, 8));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
            }

            g2.dispose();
            super.paintComponent(g);
        }
    }

    private class ActionCard extends JPanel {

        ActionCard(String title, String desc, String pageName, String iconText) {
            setOpaque(false);
            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(20, 22, 18, 22));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            SimpleIcon icon = new SimpleIcon(iconText);
            icon.setPreferredSize(new Dimension(74, 74));

            JLabel lblTitle = new JLabel("<html><div style='text-align:left;'>" + title + "</div></html>", SwingConstants.LEFT);
            lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
            lblTitle.setForeground(Color.WHITE);
            lblTitle.setHorizontalAlignment(SwingConstants.LEFT);
            lblTitle.setVerticalAlignment(SwingConstants.CENTER);
            lblTitle.setBorder(new EmptyBorder(0, 14, 0, 0));

            JPanel topRow = new JPanel(new BorderLayout(14, 0));
            topRow.setOpaque(false);
            topRow.add(icon, BorderLayout.WEST);
            topRow.add(lblTitle, BorderLayout.CENTER);

            JTextArea lblDesc = new JTextArea(desc);
            lblDesc.setLineWrap(true);
            lblDesc.setWrapStyleWord(true);
            lblDesc.setEditable(false);
            lblDesc.setOpaque(false);
            lblDesc.setFocusable(false);
            lblDesc.setHighlighter(null);
            lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            lblDesc.setForeground(TEXT_MUTED);
            lblDesc.setBorder(new EmptyBorder(16, 0, 0, 0));

            JLabel go = new JLabel("MỞ CHỨC NĂNG →", SwingConstants.LEFT);
            go.setFont(new Font("Segoe UI", Font.BOLD, 14));
            go.setForeground(GOLD_LIGHT);
            go.setHorizontalAlignment(SwingConstants.LEFT);
            go.setBorder(new EmptyBorder(12, 0, 0, 0));

            JPanel centerPanel = new JPanel(new BorderLayout());
            centerPanel.setOpaque(false);
            centerPanel.add(topRow, BorderLayout.NORTH);
            centerPanel.add(lblDesc, BorderLayout.CENTER);
            centerPanel.add(go, BorderLayout.SOUTH);

            add(centerPanel, BorderLayout.CENTER);

            MouseAdapter openAction = new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    showPage(pageName);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    repaint();
                }
            };

            addMouseListener(openAction);
            icon.addMouseListener(openAction);
            centerPanel.addMouseListener(openAction);
            lblTitle.addMouseListener(openAction);
            lblDesc.addMouseListener(openAction);
            go.addMouseListener(openAction);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            GradientPaint gp = new GradientPaint(
                    0, 0, CARD_DARK,
                    getWidth(), getHeight(), CARD_DARK_2
            );
            g2.setPaint(gp);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);

            g2.setColor(new Color(192, 149, 94, 95));
            g2.setStroke(new BasicStroke(1.4f));
            g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 22, 22);

            g2.setColor(new Color(192, 149, 94, 30));
            g2.fillOval(getWidth() - 92, -38, 128, 128);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    private class SimpleIcon extends JPanel {
        private final String text;

        SimpleIcon(String text) {
            this.text = text;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int size = Math.min(getWidth(), getHeight()) - 8;
            int x = (getWidth() - size) / 2;
            int y = (getHeight() - size) / 2;

            GradientPaint gp = new GradientPaint(
                    x, y, GOLD_LIGHT,
                    x + size, y + size, GOLD_DARK
            );
            g2.setPaint(gp);
            g2.fillOval(x, y, size, size);

            g2.setColor(new Color(255, 255, 255, 70));
            g2.setStroke(new BasicStroke(2f));
            g2.drawOval(x + 4, y + 4, size - 8, size - 8);

            g2.setColor(new Color(30, 24, 20));
            g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
            FontMetrics fm = g2.getFontMetrics();

            int tx = (getWidth() - fm.stringWidth(text)) / 2;
            int ty = (getHeight() + fm.getAscent()) / 2 - 4;

            g2.drawString(text, tx, ty);
            g2.dispose();
        }
    }

    private class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            GradientPaint gp = new GradientPaint(
                    0, 0, BG_DARK,
                    getWidth(), getHeight(), BG_DARK_2
            );
            g2.setPaint(gp);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
        }
    }

    private class LogoPanel extends JPanel {
        private final Image logoImage;

        LogoPanel() {
            setOpaque(false);
            logoImage = loadLogoImage(64, 64);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (logoImage != null) {
                int x = (getWidth() - logoImage.getWidth(this)) / 2;
                int y = (getHeight() - logoImage.getHeight(this)) / 2;
                g.drawImage(logoImage, x, y, this);
            }
        }
    }

    private class LogoLargePanel extends JPanel {
        private final Image logoImage;

        LogoLargePanel() {
            setOpaque(false);
            logoImage = loadLogoImage(150, 150);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (logoImage != null) {
                int x = (getWidth() - logoImage.getWidth(this)) / 2;
                int y = (getHeight() - logoImage.getHeight(this)) / 2;
                g.drawImage(logoImage, x, y, this);
            }
        }
    }

    private Image loadNhanVienImage(int w, int h) {
        try {
            java.net.URL url = getClass().getResource("/image/nhanvien.png");

            ImageIcon icon;
            if (url != null) {
                icon = new ImageIcon(url);
            } else {
                File file = new File("src/image/nhanvien.png");
                if (!file.exists()) return null;
                icon = new ImageIcon(file.getAbsolutePath());
            }

            Image raw = icon.getImage();
            if (raw == null || raw.getWidth(null) <= 0 || raw.getHeight(null) <= 0) {
                return null;
            }

            return raw.getScaledInstance(w, h, Image.SCALE_SMOOTH);

        } catch (Exception e) {
            return null;
        }
    }

    private class AvatarPanel extends JPanel {
        private final Image avatarImage;

        AvatarPanel() {
            setOpaque(false);
            avatarImage = loadNhanVienImage(34, 34);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int size = Math.min(getWidth(), getHeight());
            int x = (getWidth() - size) / 2;
            int y = (getHeight() - size) / 2;

            g2.setColor(new Color(192, 149, 94, 60));
            g2.fillOval(x, y, size, size);

            g2.setColor(GOLD);
            g2.setStroke(new BasicStroke(2f));
            g2.drawOval(x + 1, y + 1, size - 3, size - 3);

            if (avatarImage != null) {
                int imgSize = size - 8;
                int imgX = x + (size - imgSize) / 2;
                int imgY = y + (size - imgSize) / 2;

                Shape oldClip = g2.getClip();
                g2.setClip(new java.awt.geom.Ellipse2D.Double(x + 3, y + 3, size - 6, size - 6));
                g2.drawImage(avatarImage, imgX, imgY, imgSize, imgSize, this);
                g2.setClip(oldClip);
            } else {
                veIconNguoi(g2, x, y, size);
            }

            g2.dispose();
        }

        private void veIconNguoi(Graphics2D g2, int x, int y, int size) {
            int head = Math.max(9, size / 4);
            int headX = x + (size - head) / 2;
            int headY = y + size / 5;

            g2.setColor(new Color(30, 24, 20));
            g2.fillOval(headX, headY, head, head);

            int bodyW = size / 2;
            int bodyH = size / 3;
            int bodyX = x + (size - bodyW) / 2;
            int bodyY = y + size / 2;
            g2.fillRoundRect(bodyX, bodyY, bodyW, bodyH, bodyH, bodyH);
        }
    }

    private class BellButton extends JButton {
        BellButton() {
            setUI(new javax.swing.plaf.basic.BasicButtonUI());
            setText("🔔");
            setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
            setBackground(new Color(39, 37, 34));
            setForeground(GOLD_LIGHT);
            setFocusPainted(false);
            setOpaque(true);
            setContentAreaFilled(true);
            setBorderPainted(true);
            setMargin(new Insets(5, 5, 5, 5));
            setBorder(BorderFactory.createLineBorder(GOLD_DARK, 1));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
    }

    private class DoanhThuPanel extends JPanel {

        private JComboBox<String> cboKieuXem;
        private JTextField txtNgay;
        private JLabel lblKetQua;
        private JTable table;
        private DefaultTableModel model;

        private final DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        DoanhThuPanel() {
            setLayout(new BorderLayout(12, 12));
            setBackground(new Color(28, 25, 22));
            setBorder(new EmptyBorder(10, 10, 10, 10));

            createUI();
            loadTheoNgay();
        }

        private void createUI() {
            JPanel top = new JPanel(new BorderLayout(12, 12));
            top.setOpaque(false);

            JPanel filter = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
            filter.setBackground(new Color(39, 37, 34));
            filter.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER, 1),
                    new EmptyBorder(8, 10, 8, 10)
            ));

            JLabel lbl1 = createDarkLabel("Kiểu xem:");
            cboKieuXem = new DarkComboBox(new String[]{"Theo ngày", "Theo tháng", "Theo năm"});
            styleCombo(cboKieuXem);

            JLabel lbl2 = createDarkLabel("Tìm theo ngày:");
            txtNgay = new JTextField(12);
            txtNgay.setText(LocalDateTime.now().format(dateFmt));
            styleTextField(txtNgay);

            JButton btnXem = createGoldButton("Xem");
            JButton btnTimNgay = createGoldButton("Tìm ngày");

            filter.add(lbl1);
            filter.add(cboKieuXem);
            filter.add(lbl2);
            filter.add(txtNgay);
            filter.add(btnXem);
            filter.add(btnTimNgay);

            lblKetQua = new JLabel("Doanh thu: 0 VNĐ");
            lblKetQua.setFont(new Font("Segoe UI", Font.BOLD, 24));
            lblKetQua.setForeground(GOLD_LIGHT);
            lblKetQua.setBorder(new EmptyBorder(10, 4, 10, 4));

            top.add(filter, BorderLayout.NORTH);
            top.add(lblKetQua, BorderLayout.SOUTH);

            model = new DefaultTableModel(new String[]{"Thời gian", "Doanh thu"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            table = new JTable(model);
            table.setRowHeight(28);
            table.setBackground(new Color(35, 32, 29));
            table.setForeground(TEXT_LIGHT);
            table.setGridColor(new Color(90, 70, 45));
            table.setSelectionBackground(new Color(124, 88, 49));
            table.setSelectionForeground(Color.WHITE);
            table.getTableHeader().setBackground(new Color(45, 38, 31));
            table.getTableHeader().setForeground(GOLD_LIGHT);
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
            table.getTableHeader().setPreferredSize(new Dimension(0, 36));
            table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            table.setFillsViewportHeight(true);
            table.getTableHeader().setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable t, Object value, boolean isSelected,
                                                               boolean hasFocus, int row, int column) {
                    super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, column);
                    setHorizontalAlignment(SwingConstants.CENTER);
                    setBackground(new Color(45, 38, 31));
                    setForeground(GOLD_LIGHT);
                    setFont(new Font("Segoe UI", Font.BOLD, 13));
                    setBorder(BorderFactory.createLineBorder(BORDER, 1));
                    return this;
                }
            });
            table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable t, Object value, boolean selected, boolean focus, int row, int column) {
                    super.getTableCellRendererComponent(t, value, selected, focus, row, column);
                    setHorizontalAlignment(SwingConstants.CENTER);
                    setBackground(selected ? GOLD_DARK : (row % 2 == 0 ? new Color(35, 32, 29) : CARD_DARK_2));
                    setForeground(selected ? Color.WHITE : TEXT_LIGHT);
                    setBorder(new EmptyBorder(4, 8, 4, 8));
                    return this;
                }
            });

            JScrollPane scroll = new JScrollPane(table);
            scroll.getViewport().setBackground(new Color(28, 25, 22));
            scroll.setBorder(BorderFactory.createLineBorder(BORDER, 1));

            add(top, BorderLayout.NORTH);
            add(scroll, BorderLayout.CENTER);

            btnXem.addActionListener(e -> {
                int index = cboKieuXem.getSelectedIndex();

                if (index == 0) {
                    loadTheoNgay();
                } else if (index == 1) {
                    loadTheoThang();
                } else {
                    loadTheoNam();
                }
            });

            btnTimNgay.addActionListener(e -> timTheoNgay());
        }

        private JLabel createDarkLabel(String text) {
            JLabel label = new JLabel(text);
            label.setFont(new Font("Segoe UI", Font.BOLD, 13));
            label.setForeground(TEXT_LIGHT);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            return label;
        }

        private void styleTextField(JTextField txt) {
            txt.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            txt.setHorizontalAlignment(SwingConstants.CENTER);
            txt.setBackground(new Color(55, 51, 47));
            txt.setForeground(TEXT_LIGHT);
            txt.setCaretColor(GOLD_LIGHT);
            txt.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(GOLD_DARK, 1),
                    new EmptyBorder(5, 8, 5, 8)
            ));
        }


        private class DarkComboBox extends JComboBox<String> {
            private final Color bg = new Color(55, 51, 47);

            DarkComboBox(String[] items) {
                super(items);
                setOpaque(false);
                setFocusable(false);
                setFont(new Font("Segoe UI", Font.PLAIN, 13));
                setBackground(bg);
                setForeground(TEXT_LIGHT);
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(bg);
                g2.fillRect(0, 0, getWidth(), getHeight());

                Object selected = getSelectedItem();
                String text = selected == null ? "" : selected.toString();

                g2.setFont(getFont());
                g2.setColor(TEXT_LIGHT);

                FontMetrics fm = g2.getFontMetrics();
                int arrowWidth = 28;
                int textAreaWidth = Math.max(0, getWidth() - arrowWidth);
                int x = Math.max(6, (textAreaWidth - fm.stringWidth(text)) / 2);
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;

                g2.drawString(text, x, y);
                g2.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {
                g.setColor(GOLD_DARK);
                g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
            }
        }

        private void styleCombo(JComboBox<String> combo) {
            final Color comboBg = new Color(55, 51, 47);
            final Color comboPopupBg = new Color(39, 37, 34);
            final Color comboBorder = GOLD_DARK;

            combo.setEditable(false);
            combo.setOpaque(false);
            combo.setFocusable(false);
            combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            combo.setBackground(comboBg);
            combo.setForeground(TEXT_LIGHT);
            combo.setBorder(BorderFactory.createLineBorder(comboBorder, 1));
            combo.setMaximumRowCount(6);

            combo.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                              boolean isSelected, boolean cellHasFocus) {
                    JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    label.setOpaque(true);
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    label.setBackground(comboPopupBg);
                    label.setForeground(TEXT_LIGHT);
                    label.setBorder(new EmptyBorder(4, 8, 4, 8));

                    list.setBackground(comboPopupBg);
                    list.setForeground(TEXT_LIGHT);
                    list.setSelectionBackground(comboPopupBg);
                    list.setSelectionForeground(TEXT_LIGHT);
                    list.setFixedCellHeight(26);

                    return label;
                }
            });

            combo.setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
                @Override
                protected JButton createArrowButton() {
                    JButton button = new JButton("▼");
                    button.setUI(new javax.swing.plaf.basic.BasicButtonUI());
                    button.setFont(new Font("Segoe UI", Font.BOLD, 10));
                    button.setBackground(comboBg);
                    button.setForeground(GOLD_LIGHT);
                    button.setOpaque(true);
                    button.setContentAreaFilled(true);
                    button.setFocusPainted(false);
                    button.setBorderPainted(true);
                    button.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, comboBorder));
                    return button;
                }

                @Override
                public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
                    g.setColor(comboBg);
                    g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
                }

                @Override
                public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus) {
                    g.setColor(comboBg);
                    g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);

                    Object selected = comboBox.getSelectedItem();
                    String text = selected == null ? "" : selected.toString();

                    g.setFont(comboBox.getFont());
                    g.setColor(TEXT_LIGHT);

                    FontMetrics fm = g.getFontMetrics();
                    int x = bounds.x + Math.max(6, (bounds.width - fm.stringWidth(text)) / 2);
                    int y = bounds.y + (bounds.height + fm.getAscent() - fm.getDescent()) / 2;

                    g.drawString(text, x, y);
                }
            });
        }

        private JButton createGoldButton(String text) {
            JButton btn = new JButton(text);
            btn.setUI(new javax.swing.plaf.basic.BasicButtonUI());
            btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
            btn.setBackground(GOLD_LIGHT);
            btn.setForeground(Color.BLACK);
                btn.setFocusPainted(false);
            btn.setOpaque(true);
            btn.setContentAreaFilled(true);
            btn.setBorderPainted(true);
            btn.setMargin(new Insets(7, 14, 7, 14));
            btn.setBorder(BorderFactory.createLineBorder(GOLD_DARK, 1));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            return btn;
        }

        private void loadTheoNgay() {
            String sql = """
                    SELECT CAST(ngayLap AS DATE) AS thoiGian, SUM(thanhToan) AS doanhThu
                    FROM HoaDon
                    WHERE trangThai = N'Đã thanh toán'
                    GROUP BY CAST(ngayLap AS DATE)
                    ORDER BY thoiGian DESC
                    """;

            loadData(sql, "ngày");
        }

        private void loadTheoThang() {
            String sql = """
                    SELECT CONCAT(MONTH(ngayLap), '/', YEAR(ngayLap)) AS thoiGian,
                           SUM(thanhToan) AS doanhThu
                    FROM HoaDon
                    WHERE trangThai = N'Đã thanh toán'
                    GROUP BY YEAR(ngayLap), MONTH(ngayLap)
                    ORDER BY YEAR(ngayLap) DESC, MONTH(ngayLap) DESC
                    """;

            loadData(sql, "tháng");
        }

        private void loadTheoNam() {
            String sql = """
                    SELECT CAST(YEAR(ngayLap) AS NVARCHAR(10)) AS thoiGian,
                           SUM(thanhToan) AS doanhThu
                    FROM HoaDon
                    WHERE trangThai = N'Đã thanh toán'
                    GROUP BY YEAR(ngayLap)
                    ORDER BY YEAR(ngayLap) DESC
                    """;

            loadData(sql, "năm");
        }

        private void loadData(String sql, String label) {
            model.setRowCount(0);
            double tong = 0;

            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    String thoiGian = rs.getString("thoiGian");
                    double doanhThu = rs.getDouble("doanhThu");
                    tong += doanhThu;

                    model.addRow(new Object[]{
                            thoiGian,
                            moneyFormat.format(doanhThu) + " VNĐ"
                    });
                }

                lblKetQua.setText("Tổng doanh thu theo " + label + ": " + moneyFormat.format(tong) + " VNĐ");

            } catch (Exception e) {
                AppDialog.showError(this, "Lỗi", "Lỗi đọc doanh thu: " + e.getMessage());
            }
        }

        private void timTheoNgay() {
            String input = txtNgay.getText().trim();

            if (input.isEmpty()) {
                AppDialog.showWarning(this, "Thông báo", "Vui lòng nhập ngày dạng dd/MM/yyyy!");
                return;
            }

            java.time.LocalDate date;

            try {
                date = java.time.LocalDate.parse(input, dateFmt);
            } catch (Exception e) {
                AppDialog.showError(this, "Lỗi nhập liệu", "Ngày phải đúng định dạng dd/MM/yyyy!");
                return;
            }

            String sql = """
                    SELECT SUM(thanhToan) AS doanhThu
                    FROM HoaDon
                    WHERE trangThai = N'Đã thanh toán'
                      AND CAST(ngayLap AS DATE) = ?
                    """;

            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setDate(1, java.sql.Date.valueOf(date));

                try (ResultSet rs = ps.executeQuery()) {
                    double doanhThu = 0;

                    if (rs.next()) {
                        doanhThu = rs.getDouble("doanhThu");
                    }

                    model.setRowCount(0);
                    model.addRow(new Object[]{
                            input,
                            moneyFormat.format(doanhThu) + " VNĐ"
                    });

                    lblKetQua.setText("Doanh thu ngày " + input + ": " + moneyFormat.format(doanhThu) + " VNĐ");
                }

            } catch (Exception e) {
                AppDialog.showError(this, "Lỗi", "Lỗi tìm doanh thu: " + e.getMessage());
            }
        }
    }
}