package GUI;

import database.DBConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.*;

public class LoginFrame extends JFrame {

    private JTextField txtTaiKhoan;
    private JPasswordField txtMatKhau;
    private JCheckBox chkHienMatKhau;

    private String vaiTroDaChon = "";       // ADMIN hoặc STAFF
    private String tenVaiTroHienThi = "";   // QUẢN LÝ hoặc NHÂN VIÊN

    private final Color BG_DARK = new Color(9, 10, 12);
    private final Color PANEL_DARK = new Color(25, 28, 34);
    private final Color INPUT_DARK = new Color(43, 45, 55);
    private final Color GOLD = new Color(192, 149, 94);
    private final Color GOLD_LIGHT = new Color(223, 196, 162);
    private final Color GOLD_DARK = new Color(124, 88, 49);
    private final Color TEXT_LIGHT = new Color(245, 239, 230);
    private final Color TEXT_MUTED = new Color(185, 175, 160);

    public LoginFrame() {
        setTitle("Đăng nhập hệ thống BiLuxury");
        setSize(760, 620);
        setMinimumSize(new Dimension(720, 580));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
    }

    private void initUI() {
        hienThiChonVaiTro();
    }

    private JPanel taoKhungChung(String titleText, String subtitleText) {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_DARK);
        root.setBorder(new EmptyBorder(26, 44, 26, 44));

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel titleBrand = new JLabel("BILUXURY", SwingConstants.CENTER);
        titleBrand.setFont(new Font("Segoe UI", Font.BOLD, 40));
        titleBrand.setForeground(GOLD_LIGHT);
        titleBrand.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel logo = createLogoLabel(92, 92);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel slogan = new JLabel("MEN'S WEAR MANAGEMENT", SwingConstants.CENTER);
        slogan.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        slogan.setForeground(GOLD_LIGHT);
        slogan.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel(titleText, SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel(subtitleText, SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(TEXT_MUTED);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        content.add(titleBrand);
        content.add(Box.createVerticalStrut(8));
        content.add(logo);
        content.add(Box.createVerticalStrut(8));
        content.add(slogan);
        content.add(Box.createVerticalStrut(26));
        content.add(title);
        content.add(Box.createVerticalStrut(5));
        content.add(subtitle);
        content.add(Box.createVerticalStrut(22));

        root.add(content, BorderLayout.CENTER);
        return root;
    }

    private void hienThiChonVaiTro() {
        setTitle("Chọn vai trò đăng nhập");
        setSize(760, 620);
        setLocationRelativeTo(null);

        JPanel root = taoKhungChung(
                "CHỌN VAI TRÒ ĐĂNG NHẬP",
                "Chọn quyền sử dụng trước khi nhập tài khoản"
        );

        JPanel content = (JPanel) root.getComponent(0);

        JPanel rolePanel = new JPanel(new GridLayout(1, 2, 24, 0));
        rolePanel.setOpaque(false);
        rolePanel.setMaximumSize(new Dimension(640, 210));
        rolePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnQuanLy = taoNutVaiTro("QUẢN LÝ", "Toàn quyền hệ thống", true);
        JButton btnNhanVien = taoNutVaiTro("NHÂN VIÊN", "Quyền nghiệp vụ cơ bản", false);

        btnQuanLy.addActionListener(e -> {
            vaiTroDaChon = "ADMIN";
            tenVaiTroHienThi = "QUẢN LÝ";
            hienThiFormDangNhap();
        });

        btnNhanVien.addActionListener(e -> {
            vaiTroDaChon = "STAFF";
            tenVaiTroHienThi = "NHÂN VIÊN";
            hienThiFormDangNhap();
        });

        rolePanel.add(btnQuanLy);
        rolePanel.add(btnNhanVien);
        content.add(rolePanel);

        setContentPane(root);
        revalidate();
        repaint();
    }

    private JButton taoNutVaiTro(String role, String subText, boolean admin) {
        JButton btn = new JButton("<html><center>" + role
                + "<br><span style='font-size:12px; font-weight:normal;'>" + subText
                + "</span></center></html>");
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btn.setFont(new Font("Segoe UI", Font.BOLD, 22));
        btn.setForeground(admin ? GOLD_LIGHT : TEXT_LIGHT);
        btn.setBackground(admin ? new Color(45, 38, 30) : PANEL_DARK);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(admin ? GOLD_LIGHT : GOLD_DARK, 2),
                new EmptyBorder(28, 18, 28, 18)
        ));
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(60, 49, 36));
                btn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(GOLD_LIGHT, 2),
                        new EmptyBorder(28, 18, 28, 18)
                ));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(admin ? new Color(45, 38, 30) : PANEL_DARK);
                btn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(admin ? GOLD_LIGHT : GOLD_DARK, 2),
                        new EmptyBorder(28, 18, 28, 18)
                ));
            }
        });
        return btn;
    }

    private void hienThiFormDangNhap() {
        setTitle("Đăng nhập - " + tenVaiTroHienThi);
        setSize(760, 700);
        setLocationRelativeTo(null);

        JPanel root = taoKhungChung(
                "ĐĂNG NHẬP - " + tenVaiTroHienThi,
                "Bạn đang đăng nhập với quyền " + tenVaiTroHienThi.toLowerCase()
        );

        JPanel content = (JPanel) root.getComponent(0);
        JPanel form = createFormPanel();
        content.add(form);

        setContentPane(root);
        revalidate();
        repaint();
        txtTaiKhoan.requestFocus();
    }

    private JPanel createFormPanel() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(PANEL_DARK);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GOLD_DARK, 1),
                new EmptyBorder(34, 44, 28, 44)
        ));
        form.setMaximumSize(new Dimension(650, 390));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 8, 10, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTaiKhoan = createLabel("Tên đăng nhập:");
        JLabel lblMatKhau = createLabel("Mật khẩu:");

        txtTaiKhoan = createTextField();
        txtMatKhau = createPasswordField();

        chkHienMatKhau = new JCheckBox("Hiện mật khẩu");
        chkHienMatKhau.setOpaque(false);
        chkHienMatKhau.setForeground(TEXT_MUTED);
        chkHienMatKhau.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        chkHienMatKhau.setFocusPainted(false);
        chkHienMatKhau.setCursor(new Cursor(Cursor.HAND_CURSOR));
        chkHienMatKhau.addActionListener(e -> txtMatKhau.setEchoChar(chkHienMatKhau.isSelected() ? (char) 0 : '•'));

        JButton btnQuayLai = createForgotButton("QUAY LẠI");
        btnQuayLai.setPreferredSize(new Dimension(130, 42));
        btnQuayLai.setBorderPainted(true);
        btnQuayLai.setBorder(BorderFactory.createLineBorder(GOLD_DARK, 1));
        btnQuayLai.addActionListener(e -> hienThiChonVaiTro());

        JButton btnDangNhap = createGoldButton("ĐĂNG NHẬP");
        btnDangNhap.setPreferredSize(new Dimension(190, 42));
        btnDangNhap.addActionListener(e -> dangNhap());

        JButton btnQuenMatKhau = createForgotButton("QUÊN MẬT KHẨU?");
        btnQuenMatKhau.addActionListener(e -> moQuenMatKhau());

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.25;
        form.add(lblTaiKhoan, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.75;
        form.add(txtTaiKhoan, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.25;
        form.add(lblMatKhau, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.75;
        form.add(txtMatKhau, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        form.add(chkHienMatKhau, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(btnQuayLai);
        buttonPanel.add(btnDangNhap);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(22, 8, 10, 8);
        form.add(buttonPanel, gbc);

        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(4, 8, 0, 8);
        form.add(btnQuenMatKhau, gbc);

        txtTaiKhoan.addActionListener(e -> txtMatKhau.requestFocus());
        txtMatKhau.addActionListener(e -> btnDangNhap.doClick());
        getRootPane().setDefaultButton(btnDangNhap);

        return form;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 17));
        label.setForeground(Color.WHITE);
        return label;
    }

    private JTextField createTextField() {
        JTextField txt = new JTextField();
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txt.setForeground(TEXT_LIGHT);
        txt.setBackground(INPUT_DARK);
        txt.setCaretColor(GOLD_LIGHT);
        txt.setPreferredSize(new Dimension(380, 48));
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GOLD_DARK, 1),
                new EmptyBorder(8, 12, 8, 12)
        ));
        return txt;
    }

    private JPasswordField createPasswordField() {
        JPasswordField txt = new JPasswordField();
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txt.setForeground(TEXT_LIGHT);
        txt.setBackground(INPUT_DARK);
        txt.setCaretColor(GOLD_LIGHT);
        txt.setEchoChar('•');
        txt.setPreferredSize(new Dimension(380, 48));
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GOLD_DARK, 1),
                new EmptyBorder(8, 12, 8, 12)
        ));
        return txt;
    }

    private JButton createGoldButton(String text) {
        JButton btn = new JButton(text);
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setBackground(GOLD_LIGHT);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createLineBorder(GOLD, 2));
        return btn;
    }

    private JButton createForgotButton(String text) {
        JButton btn = new JButton(text);
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(PANEL_DARK);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(165, 28));
        return btn;
    }

    private JLabel createLogoLabel(int w, int h) {
        JLabel label = new JLabel();
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setOpaque(false);

        Image img = loadLogoImage(w, h);
        if (img != null) {
            label.setIcon(new ImageIcon(img));
        }

        return label;
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

            BufferedImage transparent = removeLogoBackground(source);

            return transparent.getScaledInstance(w, h, Image.SCALE_SMOOTH);

        } catch (Exception e) {
            return null;
        }
    }

    private BufferedImage removeLogoBackground(BufferedImage src) {
        BufferedImage out = new BufferedImage(
                src.getWidth(),
                src.getHeight(),
                BufferedImage.TYPE_INT_ARGB
        );

        for (int y = 0; y < src.getHeight(); y++) {
            for (int x = 0; x < src.getWidth(); x++) {
                int argb = src.getRGB(x, y);

                int a = (argb >> 24) & 0xff;
                int r = (argb >> 16) & 0xff;
                int g = (argb >> 8) & 0xff;
                int b = argb & 0xff;

                boolean darkBg = r < 65 && g < 65 && b < 65;
                boolean brownBg = r > 55 && r < 170 && g > 30 && g < 130 && b > 10 && b < 105;
                boolean lowSaturationBg = Math.abs(r - g) < 20 && Math.abs(g - b) < 20 && r < 110;

                boolean isGoldLogo = r > 120 && g > 80 && b < 80;

                if (!isGoldLogo && (darkBg || brownBg || lowSaturationBg)) {
                    out.setRGB(x, y, 0x00000000);
                } else {
                    out.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
                }
            }
        }

        return out;
    }

    private void dangNhap() {
        if (vaiTroDaChon == null || vaiTroDaChon.isBlank()) {
            AppDialog.showWarning(this, "Thông báo", "Vui lòng chọn vai trò đăng nhập trước!");
            hienThiChonVaiTro();
            return;
        }

        String taiKhoan = txtTaiKhoan.getText().trim();
        String matKhau = new String(txtMatKhau.getPassword()).trim();

        if (taiKhoan.isEmpty() || matKhau.isEmpty()) {
            AppDialog.showWarning(this, "Thông báo", "Vui lòng nhập đầy đủ tài khoản và mật khẩu!");
            return;
        }

        try {
            LoginResult result = xacThucTaiKhoan(taiKhoan, matKhau);

            if (result == null) {
                AppDialog.showError(this, "Đăng nhập thất bại", "Sai tài khoản hoặc mật khẩu!");
                txtMatKhau.setText("");
                txtMatKhau.requestFocus();
                return;
            }

            String roleTrongDB = chuanHoaVaiTro(result.vaiTro);

            // Luồng logic phân quyền:
            // - Tài khoản STAFF chỉ được vào màn NHÂN VIÊN.
            // - Tài khoản ADMIN được phép chọn QUẢN LÝ hoặc NHÂN VIÊN để test giao diện phân quyền.
            if ("ADMIN".equals(vaiTroDaChon) && !"ADMIN".equals(roleTrongDB)) {
                AppDialog.showWarning(
                        this,
                        "Không đủ quyền",
                        "Tài khoản này không có quyền đăng nhập với vai trò QUẢN LÝ."
                );
                return;
            }

            String vaiTroChayHeThong = "STAFF".equals(vaiTroDaChon) ? "STAFF" : "ADMIN";
            String maNhanVien = result.maNhanVien != null ? result.maNhanVien : "";

            ghiLichSu(result.taiKhoan, vaiTroChayHeThong, "Đăng nhập hệ thống");

            dispose();
            MainFrame mainFrame = new MainFrame(result.taiKhoan, vaiTroChayHeThong, maNhanVien);
            mainFrame.setLocationRelativeTo(null);
            mainFrame.setVisible(true);

        } catch (Exception e) {
            AppDialog.showError(this, "Lỗi", "Lỗi đăng nhập: " + e.getMessage());
        }
    }

    private LoginResult xacThucTaiKhoan(String taiKhoan, String matKhau) throws Exception {
        LoginResult oldSchema = dangNhapBangSchemaCu(taiKhoan, matKhau);
        if (oldSchema != null) {
            return oldSchema;
        }

        LoginResult newSchema = dangNhapBangSchemaMoi(taiKhoan, matKhau);
        if (newSchema != null) {
            return newSchema;
        }

        return null;
    }

    private LoginResult dangNhapBangSchemaCu(String taiKhoan, String matKhau) {
        String sql = """
                SELECT taiKhoan, matKhau, vaiTro, maNhanVien
                FROM TaiKhoan
                WHERE taiKhoan = ? AND matKhau = ?
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, taiKhoan);
            ps.setString(2, matKhau);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new LoginResult(
                            rs.getString("taiKhoan"),
                            rs.getString("vaiTro"),
                            rs.getString("maNhanVien")
                    );
                }
            }
        } catch (Exception ignored) {
            // Bỏ qua để thử tiếp schema mới.
        }

        return null;
    }

    private LoginResult dangNhapBangSchemaMoi(String taiKhoan, String matKhau) {
        String sql = """
                SELECT maTaiKhoan, tenDangNhap, vaiTro
                FROM TaiKhoan
                WHERE tenDangNhap = ? AND matKhau = ? AND trangThai = 1
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, taiKhoan.toLowerCase());
            ps.setString(2, hashSHA256(matKhau));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new LoginResult(
                            rs.getString("tenDangNhap"),
                            rs.getString("vaiTro"),
                            rs.getString("maTaiKhoan")
                    );
                }
            }
        } catch (Exception ignored) {
            // Nếu project không dùng schema này thì bỏ qua.
        }

        return null;
    }

    private String chuanHoaVaiTro(String vaiTro) {
        if (vaiTro == null) {
            return "STAFF";
        }

        String v = vaiTro.trim().toUpperCase();
        if (v.equals("ADMIN") || v.equals("QUẢN LÝ") || v.equals("QUAN LY") || v.equals("QL")) {
            return "ADMIN";
        }
        return "STAFF";
    }

    private String hashSHA256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi mã hóa mật khẩu: " + e.getMessage());
        }
    }

    private static class LoginResult {
        String taiKhoan;
        String vaiTro;
        String maNhanVien;

        LoginResult(String taiKhoan, String vaiTro, String maNhanVien) {
            this.taiKhoan = taiKhoan;
            this.vaiTro = vaiTro;
            this.maNhanVien = maNhanVien;
        }
    }

    private void moQuenMatKhau() {
        JDialog dialog = new JDialog(this, "Quên mật khẩu", true);
        dialog.setSize(660, 500);
        dialog.setLocationRelativeTo(this);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_DARK);
        root.setBorder(new EmptyBorder(28, 35, 24, 35));

        JLabel title = new JLabel("KHÔI PHỤC MẬT KHẨU", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(GOLD_LIGHT);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.setBorder(new EmptyBorder(0, 0, 18, 0));
        titlePanel.add(title, BorderLayout.CENTER);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(PANEL_DARK);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GOLD_DARK, 1),
                new EmptyBorder(34, 34, 30, 34)
        ));

        JTextField txtTK = createTextFieldSmall();
        JTextField txtMaDinhDanh = createTextFieldSmall();
        JTextField txtSDT = createTextFieldSmall();
        JTextField txtCCCD = createTextFieldSmall();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 8, 10, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addForgotRow(form, gbc, 0, "Tài khoản:", txtTK);
        addForgotRow(form, gbc, 1, "Mã định danh:", txtMaDinhDanh);
        addForgotRow(form, gbc, 2, "Số điện thoại:", txtSDT);
        addForgotRow(form, gbc, 3, "CCCD:", txtCCCD);

        JButton btnLay = createGoldButton("LẤY MẬT KHẨU");
        btnLay.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnLay.setPreferredSize(new Dimension(200, 48));

        JButton btnDong = createGoldButton("ĐÓNG");
        btnDong.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnDong.setPreferredSize(new Dimension(150, 48));

        btnLay.addActionListener(e -> {
            String tk = txtTK.getText().trim();
            String maDinhDanh = txtMaDinhDanh.getText().trim();
            String sdt = txtSDT.getText().trim();
            String cccd = txtCCCD.getText().trim();

            String mk = layMatKhau(tk, maDinhDanh, sdt, cccd);

            if (mk != null) {
                showPasswordResultDialog(dialog, mk);
            } else {
                AppDialog.showError(dialog, "Khôi phục thất bại", "Thông tin không đúng!");
            }
        });

        btnDong.addActionListener(e -> dialog.dispose());

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 22, 0));
        bottom.setOpaque(false);
        bottom.setBorder(new EmptyBorder(22, 0, 0, 0));
        bottom.add(btnLay);
        bottom.add(btnDong);

        root.add(titlePanel, BorderLayout.NORTH);
        root.add(form, BorderLayout.CENTER);
        root.add(bottom, BorderLayout.SOUTH);

        dialog.setContentPane(root);
        dialog.setVisible(true);
    }

    private void showPasswordResultDialog(JDialog parent, String matKhau) {
        JDialog resultDialog = new JDialog(parent, "Mật khẩu khôi phục", true);
        resultDialog.setSize(500, 280);
        resultDialog.setLocationRelativeTo(parent);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_DARK);
        root.setBorder(new EmptyBorder(28, 34, 30, 34));

        JLabel title = new JLabel("MẬT KHẨU CỦA BẠN", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(GOLD_LIGHT);
        title.setBorder(new EmptyBorder(0, 0, 20, 0));

        JTextField txtMatKhauHienThi = new JTextField(matKhau);
        txtMatKhauHienThi.setFont(new Font("Segoe UI", Font.BOLD, 20));
        txtMatKhauHienThi.setForeground(TEXT_LIGHT);
        txtMatKhauHienThi.setBackground(INPUT_DARK);
        txtMatKhauHienThi.setCaretColor(GOLD_LIGHT);
        txtMatKhauHienThi.setHorizontalAlignment(SwingConstants.CENTER);
        txtMatKhauHienThi.setEditable(false);
        txtMatKhauHienThi.setPreferredSize(new Dimension(400, 56));
        txtMatKhauHienThi.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GOLD_DARK, 1),
                new EmptyBorder(10, 14, 10, 14)
        ));

        JButton btnDong = createGoldButton("ĐÓNG");
        btnDong.setFont(new Font("Segoe UI", Font.BOLD, 17));
        btnDong.setPreferredSize(new Dimension(150, 46));
        btnDong.addActionListener(e -> resultDialog.dispose());

        JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        center.setOpaque(false);
        center.add(txtMatKhauHienThi);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        bottom.setOpaque(false);
        bottom.setBorder(new EmptyBorder(30, 0, 0, 0));
        bottom.add(btnDong);

        root.add(title, BorderLayout.NORTH);
        root.add(center, BorderLayout.CENTER);
        root.add(bottom, BorderLayout.SOUTH);

        resultDialog.setContentPane(root);
        resultDialog.setVisible(true);
    }

    private JTextField createTextFieldSmall() {
        JTextField txt = new JTextField();
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setForeground(TEXT_LIGHT);
        txt.setBackground(INPUT_DARK);
        txt.setCaretColor(GOLD_LIGHT);
        txt.setPreferredSize(new Dimension(300, 40));
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GOLD_DARK, 1),
                new EmptyBorder(7, 12, 7, 12)
        ));
        return txt;
    }

    private void addForgotRow(JPanel form, GridBagConstraints gbc, int row, String text, JTextField txt) {
        JLabel label = createLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 15));

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.35;
        form.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.65;
        form.add(txt, gbc);
    }

    private String layMatKhau(String taiKhoan, String maDinhDanh, String sdt, String cccd) {
        if (taiKhoan.isEmpty() || maDinhDanh.isEmpty() || sdt.isEmpty() || cccd.isEmpty()) {
            return null;
        }

        if ("admin".equalsIgnoreCase(taiKhoan)) {
            String sql = """
                    SELECT tk.matKhau
                    FROM TaiKhoan tk
                    JOIN ThongTinAdmin ad ON tk.taiKhoan = ad.taiKhoan
                    WHERE tk.taiKhoan = ?
                      AND tk.vaiTro = 'ADMIN'
                      AND ad.maAdmin = ?
                      AND ad.soDienThoai = ?
                      AND ad.cccd = ?
                    """;

            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, taiKhoan);
                ps.setString(2, maDinhDanh);
                ps.setString(3, sdt);
                ps.setString(4, cccd);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("matKhau");
                    }
                }

            } catch (Exception e) {
                AppDialog.showError(this, "Lỗi", "Lỗi khôi phục mật khẩu admin: " + e.getMessage());
            }

            return null;
        }

        String sql = """
                SELECT tk.matKhau
                FROM TaiKhoan tk
                JOIN NhanVien nv ON tk.maNhanVien = nv.maDinhDanh
                WHERE tk.taiKhoan = ?
                  AND nv.maDinhDanh = ?
                  AND nv.soDienThoai = ?
                  AND nv.cccd = ?
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, taiKhoan);
            ps.setString(2, maDinhDanh);
            ps.setString(3, sdt);
            ps.setString(4, cccd);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("matKhau");
                }
            }

        } catch (Exception e) {
            AppDialog.showError(this, "Lỗi", "Lỗi khôi phục mật khẩu: " + e.getMessage());
        }

        return null;
    }

    private void ghiLichSu(String taiKhoan, String vaiTro, String hanhDong) {
        String sql = """
                INSERT INTO LichSuHoatDong
                (taiKhoan, vaiTro, hanhDong)
                VALUES (?, ?, ?)
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, taiKhoan);
            ps.setString(2, vaiTro);
            ps.setString(3, hanhDong);
            ps.executeUpdate();

        } catch (Exception ignored) {
        }
    }
}