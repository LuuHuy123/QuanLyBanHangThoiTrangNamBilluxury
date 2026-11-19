package GUI;

import Human.NhaCungCap;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class NhaCungCapPanel extends JPanel {

    private JTextField txtTenCongTy;
    private JTextField txtSoDienThoai;
    private JTextField txtEmail;
    private JTextField txtDiaChi;
    private JTextField txtNguoiLienHe;
    private JTextField txtMoTa;
    private JTextField txtTimKiem;

    private JTable tableNhaCungCap;
    private DefaultTableModel tableModel;

    private List<NhaCungCap> danhSachNCC;
    private String maNCCDaChon = null;

    private static final Color BG_PANEL = new Color(28, 25, 22);
    private static final Color CARD_DARK = new Color(39, 37, 34);
    private static final Color INPUT_DARK = new Color(48, 43, 38);
    private static final Color TABLE_DARK = new Color(35, 32, 29);
    private static final Color TABLE_DARK_2 = new Color(42, 39, 35);
    private static final Color GOLD_LIGHT = new Color(223, 196, 162);
    private static final Color GOLD_DARK = new Color(124, 88, 49);
    private static final Color TEXT_LIGHT = new Color(245, 239, 230);
    private static final Color BORDER = new Color(84, 72, 56);

    public NhaCungCapPanel() {
        danhSachNCC = NhaCungCap.docTuSQL();

        setLayout(new BorderLayout(10, 10));
        setBackground(BG_PANEL);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        taoGiaoDien();
        ganSuKien();
        capNhatBang();
    }

    private void taoGiaoDien() {
        JPanel pnlTimKiem = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnlTimKiem.setBackground(CARD_DARK);
        pnlTimKiem.setBorder(taoTitledBorder("Tìm kiếm nhà cung cấp"));

        pnlTimKiem.add(taoLabel("Tìm kiếm Mã/Tên/SĐT/Email/Người liên hệ:"));
        txtTimKiem = taoTextField(25);

        JButton btnTimKiem = taoNut("Tìm Kiếm", new Color(218, 174, 88));
        JButton btnHienTatCa = taoNut("Hiển Thị Tất Cả", new Color(120, 105, 85));

        pnlTimKiem.add(txtTimKiem);
        pnlTimKiem.add(btnTimKiem);
        pnlTimKiem.add(btnHienTatCa);

        JPanel pnlInput = new JPanel(new GridLayout(6, 2, 10, 10));
        pnlInput.setBackground(CARD_DARK);
        pnlInput.setBorder(taoTitledBorder("Thông tin nhà cung cấp"));

        pnlInput.add(taoLabel("Tên công ty:"));
        txtTenCongTy = taoTextField();
        pnlInput.add(txtTenCongTy);

        pnlInput.add(taoLabel("Số điện thoại:"));
        txtSoDienThoai = taoTextField();
        pnlInput.add(txtSoDienThoai);

        pnlInput.add(taoLabel("Email:"));
        txtEmail = taoTextField();
        pnlInput.add(txtEmail);

        pnlInput.add(taoLabel("Địa chỉ:"));
        txtDiaChi = taoTextField();
        pnlInput.add(txtDiaChi);

        pnlInput.add(taoLabel("Người liên hệ:"));
        txtNguoiLienHe = taoTextField();
        pnlInput.add(txtNguoiLienHe);

        pnlInput.add(taoLabel("Mô tả:"));
        txtMoTa = taoTextField();
        pnlInput.add(txtMoTa);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pnlButtons.setBackground(CARD_DARK);

        JButton btnThem = taoNut("Thêm Nhà Cung Cấp", new Color(70, 145, 210));
        JButton btnSua = taoNut("Sửa Thông Tin", new Color(218, 174, 88));
        JButton btnXoa = taoNut("Xóa Nhà Cung Cấp", new Color(210, 65, 55));
        JButton btnClear = taoNut("Làm Mới Form", new Color(120, 105, 85));

        pnlButtons.add(btnThem);
        pnlButtons.add(btnSua);
        pnlButtons.add(btnXoa);
        pnlButtons.add(btnClear);

        JPanel pnlTop = new JPanel(new BorderLayout(10, 10));
        pnlTop.setBackground(BG_PANEL);
        pnlTop.add(pnlTimKiem, BorderLayout.NORTH);
        pnlTop.add(pnlInput, BorderLayout.CENTER);
        pnlTop.add(pnlButtons, BorderLayout.SOUTH);

        add(pnlTop, BorderLayout.NORTH);

        String[] columnNames = {
                "Mã NCC", "Tên Công Ty", "SĐT", "Email",
                "Địa Chỉ", "Người Liên Hệ", "Mô Tả"
        };

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableNhaCungCap = new JTable(tableModel);
        tableNhaCungCap.setRowHeight(28);
        tableNhaCungCap.setBackground(TABLE_DARK);
        tableNhaCungCap.setForeground(TEXT_LIGHT);
        tableNhaCungCap.setGridColor(BORDER);
        tableNhaCungCap.setSelectionBackground(GOLD_DARK);
        tableNhaCungCap.setSelectionForeground(Color.WHITE);
        tableNhaCungCap.setShowVerticalLines(true);
        tableNhaCungCap.setShowHorizontalLines(true);
        tableNhaCungCap.getTableHeader().setBackground(CARD_DARK);
        tableNhaCungCap.getTableHeader().setForeground(GOLD_LIGHT);
        tableNhaCungCap.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tableNhaCungCap.getTableHeader().setReorderingAllowed(false);
        canGiuaHeaderVaFixKhoangTrangBang(tableNhaCungCap);
        tableNhaCungCap.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, col);
                setHorizontalAlignment(SwingConstants.CENTER);
                if (isSelected) {
                    setBackground(GOLD_DARK);
                    setForeground(Color.WHITE);
                } else {
                    setBackground(row % 2 == 0 ? TABLE_DARK : TABLE_DARK_2);
                    setForeground(TEXT_LIGHT);
                }
                setBorder(javax.swing.BorderFactory.createEmptyBorder(3, 8, 3, 8));
                return this;
            }
        });
        tableNhaCungCap.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = taoScrollPane(tableNhaCungCap, "Danh sách nhà cung cấp");
        add(scrollPane, BorderLayout.CENTER);

        btnTimKiem.addActionListener(e -> xuLyTimKiem());

        btnHienTatCa.addActionListener(e -> {
            txtTimKiem.setText("");
            danhSachNCC = NhaCungCap.docTuSQL();
            capNhatBang();
            xoaRongForm();
        });

        btnThem.addActionListener(this::xuLyThem);
        btnSua.addActionListener(this::xuLySua);
        btnXoa.addActionListener(this::xuLyXoa);
        btnClear.addActionListener(e -> xoaRongForm());
    }

    private void ganSuKien() {
        tableNhaCungCap.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tableNhaCungCap.getSelectedRow();

                if (selectedRow >= 0) {
                    maNCCDaChon = tableModel.getValueAt(selectedRow, 0).toString();

                    for (NhaCungCap ncc : danhSachNCC) {
                        if (ncc.getMaNCC().equals(maNCCDaChon)) {
                            txtTenCongTy.setText(ncc.getTenCongTy() != null ? ncc.getTenCongTy() : "");
                            txtSoDienThoai.setText(ncc.getSoDienThoai() != null ? ncc.getSoDienThoai() : "");
                            txtEmail.setText(ncc.getEmail() != null ? ncc.getEmail() : "");
                            txtDiaChi.setText(ncc.getDiaChi() != null ? ncc.getDiaChi() : "");
                            txtNguoiLienHe.setText(ncc.getNguoiLienHe() != null ? ncc.getNguoiLienHe() : "");
                            txtMoTa.setText(ncc.getMoTa() != null ? ncc.getMoTa() : "");
                            break;
                        }
                    }
                }
            }
        });
    }


    private JLabel taoLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.LEFT);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT_LIGHT);
        label.setOpaque(false);
        label.setBorder(new EmptyBorder(0, 16, 0, 8));
        return label;
    }

    private JTextField taoTextField() {
        return taoTextField(0, "");
    }

    private JTextField taoTextField(int columns) {
        return taoTextField(columns, "");
    }

    private JTextField taoTextField(String text) {
        return taoTextField(0, text);
    }

    private JTextField taoTextField(int columns, String text) {
        JTextField txt = columns > 0 ? new JTextField(columns) : new JTextField();
        txt.setText(text);
        txt.setHorizontalAlignment(SwingConstants.LEFT);
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txt.setBackground(INPUT_DARK);
        txt.setForeground(TEXT_LIGHT);
        txt.setCaretColor(GOLD_LIGHT);
        txt.setOpaque(true);
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GOLD_DARK, 1),
                new EmptyBorder(5, 8, 5, 8)
        ));
        return txt;
    }

    private TitledBorder taoTitledBorder(String title) {
        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                title,
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 13),
                GOLD_LIGHT
        );
        return border;
    }

    private void canGiuaHeaderVaFixKhoangTrangBang(JTable table) {
        table.setFillsViewportHeight(true);
        table.setOpaque(true);
        table.getTableHeader().setOpaque(true);
        table.getTableHeader().setPreferredSize(new Dimension(0, 36));
        table.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.CENTER);
                setBackground(CARD_DARK);
                setForeground(GOLD_LIGHT);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                setBorder(BorderFactory.createLineBorder(BORDER, 1));
                return this;
            }
        });
    }

    private JScrollPane taoScrollPane(JTable table, String title) {
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setOpaque(true);
        scrollPane.setBackground(BG_PANEL);
        scrollPane.getViewport().setOpaque(true);
        scrollPane.getViewport().setBackground(TABLE_DARK);
        scrollPane.setBorder(taoTitledBorder(title));
        scrollPane.getVerticalScrollBar().setBackground(TABLE_DARK);
        scrollPane.getHorizontalScrollBar().setBackground(TABLE_DARK);
        return scrollPane;
    }

    private JButton taoNut(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setHorizontalAlignment(SwingConstants.CENTER);
        btn.setBackground(color);
        boolean isDark = color.getRed() < 200 || (color.getRed() < 230 && color.getGreen() < 150);
        btn.setForeground(isDark ? Color.WHITE : Color.BLACK);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(true);
        btn.setMargin(new Insets(7, 14, 7, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createLineBorder(new Color(84, 72, 56), 1));
        return btn;
    }

    private void xuLyTimKiem() {
        String keyword = txtTimKiem.getText().trim();

        if (keyword.isEmpty()) {
            danhSachNCC = NhaCungCap.docTuSQL();
        } else {
            danhSachNCC = NhaCungCap.timKiemSQL(keyword);
        }

        capNhatBang();
    }

    private void xuLyThem(ActionEvent e) {
        try {
            NhaCungCap ncc = new NhaCungCap();

            ncc.setTenCongTy(txtTenCongTy.getText());
            ncc.setSoDienThoai(txtSoDienThoai.getText());
            ncc.setEmail(txtEmail.getText());
            ncc.setDiaChi(txtDiaChi.getText());
            ncc.setNguoiLienHe(txtNguoiLienHe.getText());
            ncc.setMoTa(txtMoTa.getText());

            ncc.luuVaoSQL();

            danhSachNCC = NhaCungCap.docTuSQL();
            capNhatBang();
            xoaRongForm();

            AppDialog.showSuccess(this, "Thông báo", "Thêm nhà cung cấp thành công!");

        } catch (IllegalArgumentException ex) {
            AppDialog.showError(this, "Lỗi nhập liệu", ex.getMessage());

        } catch (Exception ex) {
            AppDialog.showError(this, "Lỗi", "Lỗi: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void xuLySua(ActionEvent e) {
        if (maNCCDaChon == null) {
            AppDialog.showWarning(this, "Thông báo", "Vui lòng chọn nhà cung cấp cần sửa!");
            return;
        }

        try {
            NhaCungCap ncc = null;

            for (NhaCungCap item : danhSachNCC) {
                if (item.getMaNCC().equals(maNCCDaChon)) {
                    ncc = item;
                    break;
                }
            }

            if (ncc == null) {
                AppDialog.showError(this, "Lỗi", "Không tìm thấy nhà cung cấp đã chọn!");
                return;
            }

            ncc.setTenCongTy(txtTenCongTy.getText());
            ncc.setSoDienThoai(txtSoDienThoai.getText());
            ncc.setEmail(txtEmail.getText());
            ncc.setDiaChi(txtDiaChi.getText());
            ncc.setNguoiLienHe(txtNguoiLienHe.getText());
            ncc.setMoTa(txtMoTa.getText());

            ncc.capNhatSQL();

            danhSachNCC = NhaCungCap.docTuSQL();
            capNhatBang();
            xoaRongForm();

            AppDialog.showSuccess(this, "Thông báo", "Sửa nhà cung cấp thành công!");

        } catch (IllegalArgumentException ex) {
            AppDialog.showError(this, "Lỗi nhập liệu", ex.getMessage());

        } catch (Exception ex) {
            AppDialog.showError(this, "Lỗi", "Lỗi: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void xuLyXoa(ActionEvent e) {
        if (maNCCDaChon == null) {
            AppDialog.showWarning(this, "Thông báo", "Vui lòng chọn nhà cung cấp cần xóa!");
            return;
        }

        boolean xacNhan = AppDialog.showConfirm(
                this,
                "Xác nhận xóa",
                "Bạn có chắc chắn muốn xóa nhà cung cấp " + maNCCDaChon + "?"
        );

        if (xacNhan) {
            try {
                NhaCungCap.xoaKhoiSQL(maNCCDaChon);

                danhSachNCC = NhaCungCap.docTuSQL();
                capNhatBang();
                xoaRongForm();

                AppDialog.showSuccess(this, "Thông báo", "Xóa nhà cung cấp thành công!");

            } catch (Exception ex) {
                AppDialog.showError(this, "Lỗi", "Không thể xóa nhà cung cấp. Nhà cung cấp có thể đang được sử dụng trong sản phẩm.");
            }
        }
    }

    private void xoaRongForm() {
        maNCCDaChon = null;
        tableNhaCungCap.clearSelection();

        txtTenCongTy.setText("");
        txtSoDienThoai.setText("");
        txtEmail.setText("");
        txtDiaChi.setText("");
        txtNguoiLienHe.setText("");
        txtMoTa.setText("");

        txtTenCongTy.requestFocus();
    }

    private void capNhatBang() {
        tableModel.setRowCount(0);

        for (NhaCungCap ncc : danhSachNCC) {
            Object[] row = {
                    ncc.getMaNCC(),
                    ncc.getTenCongTy(),
                    ncc.getSoDienThoai(),
                    ncc.getEmail(),
                    ncc.getDiaChi(),
                    ncc.getNguoiLienHe(),
                    ncc.getMoTa()
            };

            tableModel.addRow(row);
        }
    }
}