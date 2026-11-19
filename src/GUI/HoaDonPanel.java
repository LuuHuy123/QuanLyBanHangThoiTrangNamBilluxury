package GUI;

import Human.HoaDon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Map;

public class HoaDonPanel extends JPanel {

    private JTextField txtMaKhachHang;
    private JTextField txtMaNhanVien;
    private JTextField txtMaSanPham;
    private JTextField txtSoLuong;
    private JTextField txtDonGia;
    private JTextField txtGiamGia;
    private JTextField txtGhiChu;
    private JTextField txtTimKiem;

    private JTable tableHoaDon;
    private DefaultTableModel tableModel;

    private List<HoaDon> danhSachHoaDon;
    private String maHoaDonDaChon = null;

    private String currentVaiTro = "ADMIN";

    private boolean laAdmin() {
        return "ADMIN".equalsIgnoreCase(currentVaiTro);
    }

    private boolean khongDuQuyen(String hanhDong) {
        if (!laAdmin()) {
            AppDialog.showWarning(
                    this,
                    "Không đủ quyền",
                    "Tài khoản nhân viên được tạo và thanh toán hóa đơn, nhưng không được " + hanhDong + "."
            );
            return true;
        }
        return false;
    }

    private static final Color BG_PANEL = new Color(28, 25, 22);
    private static final Color CARD_DARK = new Color(39, 37, 34);
    private static final Color INPUT_DARK = new Color(48, 43, 38);
    private static final Color TABLE_DARK = new Color(35, 32, 29);
    private static final Color TABLE_DARK_2 = new Color(42, 39, 35);
    private static final Color GOLD_LIGHT = new Color(223, 196, 162);
    private static final Color GOLD_DARK = new Color(124, 88, 49);
    private static final Color TEXT_LIGHT = new Color(245, 239, 230);
    private static final Color BORDER = new Color(84, 72, 56);

    public HoaDonPanel() {
        this("ADMIN");
    }

    public HoaDonPanel(String vaiTro) {
        this.currentVaiTro = vaiTro == null || vaiTro.isBlank() ? "STAFF" : vaiTro.trim().toUpperCase();

        danhSachHoaDon = HoaDon.docTuSQL();

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
        pnlTimKiem.setBorder(taoTitledBorder("Tìm kiếm hóa đơn"));

        pnlTimKiem.add(taoLabel("Tìm kiếm Mã HĐ/Mã KH/Mã NV/Trạng thái:"));
        txtTimKiem = taoTextField(25);

        JButton btnTimKiem = taoNut("Tìm Kiếm", new Color(52, 152, 219));
        JButton btnHienTatCa = taoNut("Hiển Thị Tất Cả", new Color(108, 117, 125));

        pnlTimKiem.add(txtTimKiem);
        pnlTimKiem.add(btnTimKiem);
        pnlTimKiem.add(btnHienTatCa);

        JPanel pnlInput = new JPanel(new GridLayout(7, 2, 10, 10));
        pnlInput.setBackground(CARD_DARK);
        pnlInput.setBorder(taoTitledBorder("Tạo hóa đơn"));

        pnlInput.add(taoLabel("Mã khách hàng:"));
        txtMaKhachHang = taoTextField();
        pnlInput.add(txtMaKhachHang);

        pnlInput.add(taoLabel("Mã nhân viên:"));
        txtMaNhanVien = taoTextField();
        pnlInput.add(txtMaNhanVien);

        pnlInput.add(taoLabel("Mã sản phẩm:"));
        txtMaSanPham = taoTextField();
        pnlInput.add(txtMaSanPham);

        pnlInput.add(taoLabel("Số lượng:"));
        txtSoLuong = taoTextField("1");
        pnlInput.add(txtSoLuong);

        pnlInput.add(taoLabel("Đơn giá:"));
        txtDonGia = taoTextField("0");
        pnlInput.add(txtDonGia);

        pnlInput.add(taoLabel("Giảm giá:"));
        txtGiamGia = taoTextField("0");
        pnlInput.add(txtGiamGia);

        pnlInput.add(taoLabel("Ghi chú:"));
        txtGhiChu = taoTextField();
        pnlInput.add(txtGhiChu);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pnlButtons.setBackground(CARD_DARK);

        JButton btnThem = taoNut("Tạo Hóa Đơn", new Color(46, 204, 113));
        JButton btnThanhToan = taoNut("Xác Nhận Thanh Toán", new Color(52, 152, 219));
        JButton btnHuy = taoNut("Hủy Hóa Đơn", new Color(241, 196, 15));
        JButton btnXoa = taoNut("Xóa Hóa Đơn", new Color(231, 76, 60));
        JButton btnClear = taoNut("Làm Mới Form", new Color(108, 117, 125));

        pnlButtons.add(btnThem);
        pnlButtons.add(btnThanhToan);
        pnlButtons.add(btnHuy);
        pnlButtons.add(btnXoa);
        pnlButtons.add(btnClear);

        JPanel pnlTop = new JPanel(new BorderLayout(10, 10));
        pnlTop.setBackground(BG_PANEL);
        pnlTop.add(pnlTimKiem, BorderLayout.NORTH);
        pnlTop.add(pnlInput, BorderLayout.CENTER);
        pnlTop.add(pnlButtons, BorderLayout.SOUTH);

        add(pnlTop, BorderLayout.NORTH);

        String[] columnNames = {
                "Mã HĐ", "Mã KH", "Mã NV", "Ngày Lập",
                "Tổng Tiền", "Giảm Giá", "Thanh Toán",
                "Trạng Thái", "Ghi Chú", "Chi Tiết"
        };

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableHoaDon = new JTable(tableModel);
        tableHoaDon.setRowHeight(28);
        tableHoaDon.setBackground(TABLE_DARK);
        tableHoaDon.setForeground(TEXT_LIGHT);
        tableHoaDon.setGridColor(BORDER);
        tableHoaDon.setSelectionBackground(GOLD_DARK);
        tableHoaDon.setSelectionForeground(Color.WHITE);
        tableHoaDon.setShowVerticalLines(true);
        tableHoaDon.setShowHorizontalLines(true);
        tableHoaDon.getTableHeader().setBackground(CARD_DARK);
        tableHoaDon.getTableHeader().setForeground(GOLD_LIGHT);
        tableHoaDon.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tableHoaDon.getTableHeader().setReorderingAllowed(false);
        canGiuaHeaderVaFixKhoangTrangBang(tableHoaDon);
        tableHoaDon.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableHoaDon.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
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
                setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
                return this;
            }
        });
        tableHoaDon.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = taoScrollPane(tableHoaDon, "Danh sách hóa đơn");
        add(scrollPane, BorderLayout.CENTER);

        btnTimKiem.addActionListener(e -> xuLyTimKiem());

        btnHienTatCa.addActionListener(e -> {
            txtTimKiem.setText("");
            danhSachHoaDon = HoaDon.docTuSQL();
            capNhatBang();
            xoaRongForm();
        });

        btnThem.addActionListener(this::xuLyThem);
        btnThanhToan.addActionListener(e -> xuLyThanhToan());
        btnHuy.addActionListener(e -> xuLyHuyHoaDon());
        btnXoa.addActionListener(e -> xuLyXoa());
        btnClear.addActionListener(e -> xoaRongForm());
    }

    private void ganSuKien() {
        tableHoaDon.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = tableHoaDon.getSelectedRow();

            if (!e.getValueIsAdjusting() && selectedRow >= 0) {
                maHoaDonDaChon = tableModel.getValueAt(selectedRow, 0).toString();

                HoaDon hd = layHoaDonDangChon();

                if (hd != null) {
                    txtMaKhachHang.setText(hd.getMaKhachHang());
                    txtMaNhanVien.setText(hd.getMaNhanVien());
                    txtGiamGia.setText(String.valueOf(hd.getGiamGia()));
                    txtGhiChu.setText(hd.getGhiChu());

                    hienThiChiTietHoaDon(hd);
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
        btn.setBackground(color);
        boolean isDark = color.getRed() < 200 || (color.getRed() < 230 && color.getGreen() < 150);
        btn.setForeground(isDark ? Color.WHITE : Color.BLACK);
        btn.setHorizontalAlignment(SwingConstants.CENTER);
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
            danhSachHoaDon = HoaDon.docTuSQL();
        } else {
            danhSachHoaDon = HoaDon.timKiemSQL(keyword);
        }

        capNhatBang();
    }

    private void xuLyThem(ActionEvent e) {
        try {
            HoaDon hd = new HoaDon();

            hd.setMaKhachHang(txtMaKhachHang.getText());
            hd.setMaNhanVien(txtMaNhanVien.getText());

            hd.themHang(
                    txtMaSanPham.getText(),
                    docSoLuong(),
                    docDonGia()
            );

            hd.setGiamGia(docGiamGia());
            hd.setGhiChu(txtGhiChu.getText());

            hd.luuVaoSQL();

            danhSachHoaDon = HoaDon.docTuSQL();
            capNhatBang();
            xoaRongForm();

            AppDialog.showSuccess(this, "Thông báo", "Tạo hóa đơn thành công!");

        } catch (NumberFormatException ex) {
            AppDialog.showError(this, "Lỗi nhập liệu", "Số lượng, đơn giá và giảm giá phải là số hợp lệ!");

        } catch (IllegalArgumentException ex) {
            AppDialog.showError(this, "Lỗi nhập liệu", ex.getMessage());

        } catch (Exception ex) {
            AppDialog.showError(this, "Lỗi", "Lỗi: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void xuLyThanhToan() {
        HoaDon hd = layHoaDonDangChon();

        if (hd == null) {
            AppDialog.showWarning(this, "Thông báo", "Vui lòng chọn hóa đơn cần thanh toán!");
            return;
        }

        try {
            hd.xacNhanThanhToan();
            hd.capNhatTrangThaiSQL();

            danhSachHoaDon = HoaDon.docTuSQL();
            capNhatBang();
            xoaRongForm();

            AppDialog.showSuccess(this, "Thông báo", "Xác nhận thanh toán thành công!");

        } catch (Exception ex) {
            AppDialog.showError(this, "Lỗi", ex.getMessage());
        }
    }

    private void xuLyHuyHoaDon() {
        if (khongDuQuyen("hủy hóa đơn")) return;

        HoaDon hd = layHoaDonDangChon();

        if (hd == null) {
            AppDialog.showWarning(this, "Thông báo", "Vui lòng chọn hóa đơn cần hủy!");
            return;
        }

        boolean xacNhan = AppDialog.showConfirm(
                this,
                "Xác nhận hủy",
                "Bạn có chắc chắn muốn hủy hóa đơn " + hd.getMaHoaDon() + "?"
        );

        if (xacNhan) {
            try {
                hd.huyHoaDon();
                hd.capNhatTrangThaiSQL();

                danhSachHoaDon = HoaDon.docTuSQL();
                capNhatBang();
                xoaRongForm();

                AppDialog.showSuccess(this, "Thông báo", "Hủy hóa đơn thành công!");

            } catch (Exception ex) {
                AppDialog.showError(this, "Lỗi", ex.getMessage());
            }
        }
    }

    private void xuLyXoa() {
        if (khongDuQuyen("xóa hóa đơn")) return;

        if (maHoaDonDaChon == null) {
            AppDialog.showWarning(this, "Thông báo", "Vui lòng chọn hóa đơn cần xóa!");
            return;
        }

        boolean xacNhan = AppDialog.showConfirm(
                this,
                "Xác nhận xóa",
                "Bạn có chắc chắn muốn xóa hóa đơn " + maHoaDonDaChon + "?"
        );

        if (xacNhan) {
            try {
                HoaDon.xoaKhoiSQL(maHoaDonDaChon);

                danhSachHoaDon = HoaDon.docTuSQL();
                capNhatBang();
                xoaRongForm();

                AppDialog.showSuccess(this, "Thông báo", "Xóa hóa đơn thành công!");

            } catch (Exception ex) {
                AppDialog.showError(this, "Lỗi", "Không thể xóa hóa đơn: " + ex.getMessage());
            }
        }
    }

    private HoaDon layHoaDonDangChon() {
        if (maHoaDonDaChon == null) {
            return null;
        }

        for (HoaDon hd : danhSachHoaDon) {
            if (hd.getMaHoaDon().equals(maHoaDonDaChon)) {
                return hd;
            }
        }

        return null;
    }

    private int docSoLuong() {
        String text = txtSoLuong.getText();

        if (text == null || text.trim().isEmpty()) {
            throw new NumberFormatException("Số lượng không được để trống.");
        }

        return Integer.parseInt(text.trim());
    }

    private double docDonGia() {
        String text = txtDonGia.getText();

        if (text == null || text.trim().isEmpty()) {
            throw new NumberFormatException("Đơn giá không được để trống.");
        }

        return Double.parseDouble(text.trim());
    }

    private double docGiamGia() {
        String text = txtGiamGia.getText();

        if (text == null || text.trim().isEmpty()) {
            return 0;
        }

        return Double.parseDouble(text.trim());
    }

    private void hienThiChiTietHoaDon(HoaDon hd) {
        if (hd == null) {
            AppDialog.showWarning(this, "Thông báo", "Vui lòng chọn hóa đơn cần xem chi tiết!");
            return;
        }

        AppDialog.showHoaDonDetails(this, hd);
    }

    private void xoaRongForm() {
        maHoaDonDaChon = null;
        tableHoaDon.clearSelection();

        txtMaKhachHang.setText("");
        txtMaNhanVien.setText("");
        txtMaSanPham.setText("");
        txtSoLuong.setText("1");
        txtDonGia.setText("0");
        txtGiamGia.setText("0");
        txtGhiChu.setText("");

        txtMaKhachHang.requestFocus();
    }

    private void capNhatBang() {
        tableModel.setRowCount(0);

        for (HoaDon hd : danhSachHoaDon) {
            Object[] row = {
                    hd.getMaHoaDon(),
                    hd.getMaKhachHang(),
                    hd.getMaNhanVien(),
                    hd.getNgayLap() != null ? hd.getNgayLap().toString() : "",
                    String.format("%,.0f", hd.getTongTien()),
                    String.format("%,.0f", hd.getGiamGia()),
                    String.format("%,.0f", hd.getThanhToan()),
                    hd.getTrangThai(),
                    hd.getGhiChu(),
                    taoChuoiChiTiet(hd)
            };

            tableModel.addRow(row);
        }
    }

    private String taoChuoiChiTiet(HoaDon hd) {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, double[]> entry : hd.getChiTiet().entrySet()) {
            if (sb.length() > 0) {
                sb.append("; ");
            }

            sb.append(entry.getKey())
                    .append(" x")
                    .append((int) entry.getValue()[0]);
        }

        return sb.toString();
    }
}