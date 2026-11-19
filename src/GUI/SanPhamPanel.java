package GUI;

import Human.SanPham;

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

public class SanPhamPanel extends JPanel {

    private JTextField txtTenSanPham;
    private JTextField txtDonVi;
    private JTextField txtGiaNhap;
    private JTextField txtGiaBan;
    private JTextField txtSoLuongTon;
    private JTextField txtLoaiSanPham;
    private JTextField txtThuongHieu;
    private JTextField txtXuatXu;
    private JTextField txtMaNCC;
    private JTextField txtMoTa;
    private JTextField txtTimKiem;

    private JTable tableSanPham;
    private DefaultTableModel tableModel;

    private List<SanPham> danhSachSanPham;
    private String maSanPhamDaChon = null;

    private String currentVaiTro = "ADMIN";

    private boolean laAdmin() {
        return "ADMIN".equalsIgnoreCase(currentVaiTro);
    }

    private boolean khongDuQuyen(String hanhDong) {
        if (!laAdmin()) {
            AppDialog.showWarning(
                    this,
                    "Không đủ quyền",
                    "Tài khoản nhân viên chỉ được xem và tìm kiếm sản phẩm, không được " + hanhDong + "."
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

    public SanPhamPanel() {
        this("ADMIN");
    }

    public SanPhamPanel(String vaiTro) {
        this.currentVaiTro = vaiTro == null || vaiTro.isBlank() ? "STAFF" : vaiTro.trim().toUpperCase();

        danhSachSanPham = SanPham.docTuSQL();

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
        pnlTimKiem.setBorder(taoTitledBorder("Tìm kiếm sản phẩm"));

        pnlTimKiem.add(taoLabel("Tìm kiếm Mã/Tên/Loại/Thương hiệu:"));
        txtTimKiem = taoTextField(25);

        JButton btnTimKiem = taoNut("Tìm Kiếm", new Color(218, 174, 88));
        JButton btnHienTatCa = taoNut("Hiển Thị Tất Cả", new Color(120, 105, 85));
        JButton btnHangSapHet = taoNut("Sản Phẩm Sắp Hết", new Color(210, 65, 55));

        pnlTimKiem.add(txtTimKiem);
        pnlTimKiem.add(btnTimKiem);
        pnlTimKiem.add(btnHienTatCa);
        pnlTimKiem.add(btnHangSapHet);

        JPanel pnlInput = new JPanel(new GridLayout(10, 2, 10, 10));
        pnlInput.setBackground(CARD_DARK);
        pnlInput.setBorder(taoTitledBorder("Thông tin sản phẩm"));

        pnlInput.add(taoLabel("Tên sản phẩm:"));
        txtTenSanPham = taoTextField();
        pnlInput.add(txtTenSanPham);

        pnlInput.add(taoLabel("Đơn vị:"));
        txtDonVi = taoTextField("cái");
        pnlInput.add(txtDonVi);

        pnlInput.add(taoLabel("Giá nhập:"));
        txtGiaNhap = taoTextField("0");
        pnlInput.add(txtGiaNhap);

        pnlInput.add(taoLabel("Giá bán:"));
        txtGiaBan = taoTextField("0");
        pnlInput.add(txtGiaBan);

        pnlInput.add(taoLabel("Số lượng tồn:"));
        txtSoLuongTon = taoTextField("0");
        pnlInput.add(txtSoLuongTon);

        pnlInput.add(taoLabel("Loại sản phẩm:"));
        txtLoaiSanPham = taoTextField();
        pnlInput.add(txtLoaiSanPham);

        pnlInput.add(taoLabel("Thương hiệu:"));
        txtThuongHieu = taoTextField();
        pnlInput.add(txtThuongHieu);

        pnlInput.add(taoLabel("Xuất xứ:"));
        txtXuatXu = taoTextField();
        pnlInput.add(txtXuatXu);

        pnlInput.add(taoLabel("Mã nhà cung cấp:"));
        txtMaNCC = taoTextField();
        pnlInput.add(txtMaNCC);

        pnlInput.add(taoLabel("Mô tả:"));
        txtMoTa = taoTextField();
        pnlInput.add(txtMoTa);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pnlButtons.setBackground(CARD_DARK);

        JButton btnThem = taoNut("Thêm Sản Phẩm", new Color(70, 145, 210));
        JButton btnSua = taoNut("Sửa Thông Tin", new Color(218, 174, 88));
        JButton btnXoa = taoNut("Xóa Sản Phẩm", new Color(210, 65, 55));
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
                "Mã SP", "Tên SP", "Đơn Vị", "Giá Nhập", "Giá Bán",
                "Tồn Kho", "Loại SP", "Thương Hiệu", "Xuất Xứ", "Mã NCC", "Mô Tả"
        };

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableSanPham = new JTable(tableModel);
        tableSanPham.setRowHeight(28);
        tableSanPham.setBackground(TABLE_DARK);
        tableSanPham.setForeground(TEXT_LIGHT);
        tableSanPham.setGridColor(BORDER);
        tableSanPham.setSelectionBackground(GOLD_DARK);
        tableSanPham.setSelectionForeground(Color.WHITE);
        tableSanPham.setShowVerticalLines(true);
        tableSanPham.setShowHorizontalLines(true);
        tableSanPham.getTableHeader().setBackground(CARD_DARK);
        tableSanPham.getTableHeader().setForeground(GOLD_LIGHT);
        tableSanPham.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tableSanPham.getTableHeader().setReorderingAllowed(false);
        canGiuaHeaderVaFixKhoangTrangBang(tableSanPham);
        tableSanPham.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
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
        tableSanPham.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = taoScrollPane(tableSanPham, "Danh sách sản phẩm");
        add(scrollPane, BorderLayout.CENTER);

        btnTimKiem.addActionListener(e -> xuLyTimKiem());

        btnHienTatCa.addActionListener(e -> {
            txtTimKiem.setText("");
            danhSachSanPham = SanPham.docTuSQL();
            capNhatBang();
            xoaRongForm();
        });

        btnHangSapHet.addActionListener(e -> xuLyHangSapHet());
        btnThem.addActionListener(this::xuLyThem);
        btnSua.addActionListener(this::xuLySua);
        btnXoa.addActionListener(this::xuLyXoa);
        btnClear.addActionListener(e -> xoaRongForm());
    }

    private void ganSuKien() {
        tableSanPham.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tableSanPham.getSelectedRow();

                if (selectedRow >= 0) {
                    maSanPhamDaChon = tableModel.getValueAt(selectedRow, 0).toString();

                    for (SanPham sp : danhSachSanPham) {
                        if (sp.getMaSanPham().equals(maSanPhamDaChon)) {
                            txtTenSanPham.setText(sp.getTenSanPham());
                            txtDonVi.setText(sp.getDonVi());
                            txtGiaNhap.setText(String.valueOf(sp.getGiaNhap()));
                            txtGiaBan.setText(String.valueOf(sp.getGiaBan()));
                            txtSoLuongTon.setText(String.valueOf(sp.getSoLuongTon()));
                            txtLoaiSanPham.setText(sp.getLoaiSanPham());
                            txtThuongHieu.setText(sp.getThuongHieu());
                            txtXuatXu.setText(sp.getXuatXu());
                            txtMaNCC.setText(sp.getMaNCC());
                            txtMoTa.setText(sp.getMoTa());
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
            danhSachSanPham = SanPham.docTuSQL();
        } else {
            danhSachSanPham = SanPham.timKiemSQL(keyword);
        }

        capNhatBang();
    }

    private void xuLyHangSapHet() {
        String input = JOptionPane.showInputDialog(this, "Nhập ngưỡng tồn kho:", "5");

        if (input == null) {
            return;
        }

        try {
            int nguong = Integer.parseInt(input.trim());

            if (nguong < 0) {
                thongBaoLoi("Lỗi nhập liệu", "Ngưỡng tồn kho không được âm!");
                return;
            }

            danhSachSanPham = SanPham.timHangSapHetSQL(nguong);
            capNhatBang();

        } catch (NumberFormatException ex) {
            thongBaoLoi("Lỗi", "Ngưỡng tồn kho phải là số nguyên!");
        }
    }

    private void xuLyThem(ActionEvent e) {
        if (khongDuQuyen("thêm sản phẩm")) return;

        try {
            SanPham sp = new SanPham();

            sp.setTenSanPham(txtTenSanPham.getText());
            sp.setDonVi(txtDonVi.getText());
            sp.setGiaNhap(Double.parseDouble(txtGiaNhap.getText().trim()));
            sp.setGiaBan(Double.parseDouble(txtGiaBan.getText().trim()));
            sp.setSoLuongTon(Integer.parseInt(txtSoLuongTon.getText().trim()));
            sp.setLoaiSanPham(txtLoaiSanPham.getText());
            sp.setThuongHieu(txtThuongHieu.getText());
            sp.setXuatXu(txtXuatXu.getText());
            sp.setMaNCC(txtMaNCC.getText());
            sp.setMoTa(txtMoTa.getText());

            sp.luuVaoSQL();

            danhSachSanPham = SanPham.docTuSQL();
            capNhatBang();
            xoaRongForm();

            thongBaoThanhCong("Thêm sản phẩm thành công!");

        } catch (NumberFormatException ex) {
            thongBaoLoi("Lỗi nhập liệu", "Giá nhập, giá bán và số lượng tồn phải là số!");

        } catch (IllegalArgumentException ex) {
            thongBaoLoi("Lỗi nhập liệu", ex.getMessage());

        } catch (Exception ex) {
            thongBaoLoi("Lỗi", "Lỗi: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void xuLySua(ActionEvent e) {
        if (khongDuQuyen("sửa sản phẩm")) return;

        if (maSanPhamDaChon == null) {
            thongBaoCanhBao("Thông báo", "Vui lòng chọn sản phẩm cần sửa!");
            return;
        }

        try {
            SanPham sp = null;

            for (SanPham item : danhSachSanPham) {
                if (item.getMaSanPham().equals(maSanPhamDaChon)) {
                    sp = item;
                    break;
                }
            }

            if (sp == null) {
                thongBaoLoi("Lỗi", "Không tìm thấy sản phẩm đã chọn!");
                return;
            }

            sp.setTenSanPham(txtTenSanPham.getText());
            sp.setDonVi(txtDonVi.getText());
            sp.setGiaNhap(Double.parseDouble(txtGiaNhap.getText().trim()));
            sp.setGiaBan(Double.parseDouble(txtGiaBan.getText().trim()));
            sp.setSoLuongTon(Integer.parseInt(txtSoLuongTon.getText().trim()));
            sp.setLoaiSanPham(txtLoaiSanPham.getText());
            sp.setThuongHieu(txtThuongHieu.getText());
            sp.setXuatXu(txtXuatXu.getText());
            sp.setMaNCC(txtMaNCC.getText());
            sp.setMoTa(txtMoTa.getText());

            sp.capNhatSQL();

            danhSachSanPham = SanPham.docTuSQL();
            capNhatBang();
            xoaRongForm();

            thongBaoThanhCong("Sửa sản phẩm thành công!");

        } catch (NumberFormatException ex) {
            thongBaoLoi("Lỗi nhập liệu", "Giá nhập, giá bán và số lượng tồn phải là số!");

        } catch (IllegalArgumentException ex) {
            thongBaoLoi("Lỗi nhập liệu", ex.getMessage());

        } catch (Exception ex) {
            thongBaoLoi("Lỗi", "Lỗi: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void xuLyXoa(ActionEvent e) {
        if (khongDuQuyen("xóa sản phẩm")) return;

        if (maSanPhamDaChon == null) {
            thongBaoCanhBao("Thông báo", "Vui lòng chọn sản phẩm cần xóa!");
            return;
        }

        boolean xacNhan = xacNhanHanhDong("Xác nhận xóa", "Bạn có chắc chắn muốn xóa sản phẩm " + maSanPhamDaChon + "?");

        if (xacNhan) {
            try {
                SanPham.xoaKhoiSQL(maSanPhamDaChon);

                danhSachSanPham = SanPham.docTuSQL();
                capNhatBang();
                xoaRongForm();

                thongBaoThanhCong("Xóa sản phẩm thành công!");

            } catch (Exception ex) {
                thongBaoLoi("Lỗi", "Lỗi khi xóa: " + ex.getMessage());
            }
        }
    }

    private void thongBaoThanhCong(String message) {
        AppDialog.showSuccess(this, "Thông báo", message);
    }

    private void thongBaoCanhBao(String title, String message) {
        AppDialog.showWarning(this, title, message);
    }

    private void thongBaoLoi(String title, String message) {
        AppDialog.showError(this, title, message);
    }

    private boolean xacNhanHanhDong(String title, String message) {
        return AppDialog.showConfirm(this, title, message);
    }

    private void xoaRongForm() {
        maSanPhamDaChon = null;
        tableSanPham.clearSelection();

        txtTenSanPham.setText("");
        txtDonVi.setText("cái");
        txtGiaNhap.setText("0");
        txtGiaBan.setText("0");
        txtSoLuongTon.setText("0");
        txtLoaiSanPham.setText("");
        txtThuongHieu.setText("");
        txtXuatXu.setText("");
        txtMaNCC.setText("");
        txtMoTa.setText("");

        txtTenSanPham.requestFocus();
    }

    private void capNhatBang() {
        tableModel.setRowCount(0);

        for (SanPham sp : danhSachSanPham) {
            Object[] row = {
                    sp.getMaSanPham(),
                    sp.getTenSanPham(),
                    sp.getDonVi(),
                    sp.getGiaNhap(),
                    sp.getGiaBan(),
                    sp.getSoLuongTon(),
                    sp.getLoaiSanPham(),
                    sp.getThuongHieu(),
                    sp.getXuatXu(),
                    sp.getMaNCC(),
                    sp.getMoTa()
            };

            tableModel.addRow(row);
        }
    }
}