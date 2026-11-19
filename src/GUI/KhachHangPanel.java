package GUI;

import Human.KhachHang;

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

public class KhachHangPanel extends JPanel {

    private JTextField txtHoTen;
    private JTextField txtSoDienThoai;
    private JTextField txtDiaChi;
    private JTextField txtDiemTichLuy;
    private JTextField txtTimKiem;

    private JTable tableKhachHang;
    private DefaultTableModel tableModel;

    private List<KhachHang> danhSachKhachHang;
    private String maKhachHangDaChon = null;

    private String currentVaiTro = "ADMIN";

    private boolean laAdmin() {
        return "ADMIN".equalsIgnoreCase(currentVaiTro);
    }

    private boolean khongDuQuyen(String hanhDong) {
        if (!laAdmin()) {
            AppDialog.showWarning(
                    this,
                    "Không đủ quyền",
                    "Tài khoản nhân viên được thêm và sửa khách hàng, nhưng không được " + hanhDong + "."
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

    public KhachHangPanel() {
        this("ADMIN");
    }

    public KhachHangPanel(String vaiTro) {
        this.currentVaiTro = vaiTro == null || vaiTro.isBlank() ? "STAFF" : vaiTro.trim().toUpperCase();

        danhSachKhachHang = KhachHang.docTuSQL();

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
        pnlTimKiem.setBorder(taoTitledBorder("Tìm kiếm khách hàng"));

        pnlTimKiem.add(taoLabel("Tìm kiếm Mã/Tên/SĐT/Địa chỉ:"));
        txtTimKiem = taoTextField(25);

        JButton btnTimKiem = taoNut("Tìm Kiếm", new Color(218, 174, 88));
        JButton btnHienTatCa = taoNut("Hiển Thị Tất Cả", new Color(120, 105, 85));

        pnlTimKiem.add(txtTimKiem);
        pnlTimKiem.add(btnTimKiem);
        pnlTimKiem.add(btnHienTatCa);

        JPanel pnlInput = new JPanel(new GridLayout(4, 2, 10, 10));
        pnlInput.setBackground(CARD_DARK);
        pnlInput.setBorder(taoTitledBorder("Thông tin khách hàng"));

        pnlInput.add(taoLabel("Họ tên:"));
        txtHoTen = taoTextField();
        pnlInput.add(txtHoTen);

        pnlInput.add(taoLabel("Số điện thoại:"));
        txtSoDienThoai = taoTextField();
        pnlInput.add(txtSoDienThoai);

        pnlInput.add(taoLabel("Địa chỉ:"));
        txtDiaChi = taoTextField();
        pnlInput.add(txtDiaChi);

        pnlInput.add(taoLabel("Điểm tích lũy:"));
        txtDiemTichLuy = taoTextField("0");
        pnlInput.add(txtDiemTichLuy);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pnlButtons.setBackground(CARD_DARK);

        JButton btnThem = taoNut("Thêm Khách Hàng", new Color(70, 145, 210));
        JButton btnSua = taoNut("Sửa Thông Tin", new Color(218, 174, 88));
        JButton btnXoa = taoNut("Xóa Khách Hàng", new Color(210, 65, 55));
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
                "Mã KH", "Họ Tên", "SĐT", "Địa Chỉ", "Điểm Tích Lũy"
        };

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableKhachHang = new JTable(tableModel);
        tableKhachHang.setRowHeight(28);
        tableKhachHang.setBackground(TABLE_DARK);
        tableKhachHang.setForeground(TEXT_LIGHT);
        tableKhachHang.setGridColor(BORDER);
        tableKhachHang.setSelectionBackground(GOLD_DARK);
        tableKhachHang.setSelectionForeground(Color.WHITE);
        tableKhachHang.setShowVerticalLines(true);
        tableKhachHang.setShowHorizontalLines(true);
        tableKhachHang.getTableHeader().setBackground(CARD_DARK);
        tableKhachHang.getTableHeader().setForeground(GOLD_LIGHT);
        tableKhachHang.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tableKhachHang.getTableHeader().setReorderingAllowed(false);
        canGiuaHeaderVaFixKhoangTrangBang(tableKhachHang);
        tableKhachHang.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
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
        tableKhachHang.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = taoScrollPane(tableKhachHang, "Danh sách khách hàng");
        add(scrollPane, BorderLayout.CENTER);

        btnTimKiem.addActionListener(e -> xuLyTimKiem());

        btnHienTatCa.addActionListener(e -> {
            txtTimKiem.setText("");
            danhSachKhachHang = KhachHang.docTuSQL();
            capNhatBang();
            xoaRongForm();
        });

        btnThem.addActionListener(this::xuLyThemKhachHang);
        btnSua.addActionListener(this::xuLySuaKhachHang);
        btnXoa.addActionListener(this::xuLyXoaKhachHang);
        btnClear.addActionListener(e -> xoaRongForm());
    }

    private void ganSuKien() {
        tableKhachHang.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tableKhachHang.getSelectedRow();

                if (selectedRow >= 0) {
                    maKhachHangDaChon = tableModel
                            .getValueAt(selectedRow, 0)
                            .toString();

                    for (KhachHang kh : danhSachKhachHang) {
                        if (kh.getMaDinhDanh().equals(maKhachHangDaChon)) {
                            txtHoTen.setText(kh.getHoTen() != null ? kh.getHoTen() : "");
                            txtSoDienThoai.setText(kh.getSoDienThoai() != null ? kh.getSoDienThoai() : "");
                            txtDiaChi.setText(kh.getDiaChi() != null ? kh.getDiaChi() : "");
                            txtDiemTichLuy.setText(String.valueOf(kh.getDiemTichLuy()));
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
            danhSachKhachHang = KhachHang.docTuSQL();
        } else {
            danhSachKhachHang = KhachHang.timKiemSQL(keyword);
        }

        capNhatBang();
    }

    private void xuLyThemKhachHang(ActionEvent e) {
        try {
            KhachHang kh = new KhachHang();

            kh.setHoTen(txtHoTen.getText());
            kh.setSoDienThoai(txtSoDienThoai.getText());
            kh.setDiaChi(txtDiaChi.getText());
            kh.setDiemTichLuy(docDiemTichLuy());

            kh.luuVaoSQL();

            danhSachKhachHang = KhachHang.docTuSQL();
            capNhatBang();
            xoaRongForm();

            AppDialog.showSuccess(this, "Thông báo", "Thêm khách hàng thành công!");

        } catch (NumberFormatException ex) {
            AppDialog.showError(this, "Lỗi nhập liệu", "Điểm tích lũy phải là số nguyên!");

        } catch (IllegalArgumentException ex) {
            AppDialog.showError(this, "Lỗi nhập liệu", ex.getMessage());

        } catch (Exception ex) {
            AppDialog.showError(this, "Lỗi", "Lỗi: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void xuLySuaKhachHang(ActionEvent e) {
        if (maKhachHangDaChon == null) {
            AppDialog.showWarning(this, "Thông báo", "Vui lòng chọn khách hàng cần sửa!");
            return;
        }

        try {
            KhachHang kh = null;

            for (KhachHang item : danhSachKhachHang) {
                if (item.getMaDinhDanh().equals(maKhachHangDaChon)) {
                    kh = item;
                    break;
                }
            }

            if (kh == null) {
                AppDialog.showError(this, "Lỗi", "Không tìm thấy khách hàng đã chọn!");
                return;
            }

            kh.setHoTen(txtHoTen.getText());
            kh.setSoDienThoai(txtSoDienThoai.getText());
            kh.setDiaChi(txtDiaChi.getText());
            kh.setDiemTichLuy(docDiemTichLuy());

            kh.capNhatSQL();

            danhSachKhachHang = KhachHang.docTuSQL();
            capNhatBang();
            xoaRongForm();

            AppDialog.showSuccess(this, "Thông báo", "Sửa thông tin khách hàng thành công!");

        } catch (NumberFormatException ex) {
            AppDialog.showError(this, "Lỗi nhập liệu", "Điểm tích lũy phải là số nguyên!");

        } catch (IllegalArgumentException ex) {
            AppDialog.showError(this, "Lỗi nhập liệu", ex.getMessage());

        } catch (Exception ex) {
            AppDialog.showError(this, "Lỗi", "Lỗi: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void xuLyXoaKhachHang(ActionEvent e) {
        if (khongDuQuyen("xóa khách hàng")) return;

        if (maKhachHangDaChon == null) {
            AppDialog.showWarning(this, "Thông báo", "Vui lòng chọn khách hàng cần xóa!");
            return;
        }

        boolean xacNhan = AppDialog.showConfirm(
                this,
                "Xác nhận xóa",
                "Bạn có chắc chắn muốn xóa khách hàng " + maKhachHangDaChon + "?"
        );

        if (xacNhan) {
            try {
                KhachHang.xoaKhoiSQL(maKhachHangDaChon);

                danhSachKhachHang = KhachHang.docTuSQL();
                capNhatBang();
                xoaRongForm();

                AppDialog.showSuccess(this, "Thông báo", "Xóa khách hàng thành công!");

            } catch (Exception ex) {
                AppDialog.showError(this, "Lỗi", "Không thể xóa khách hàng. Khách hàng có thể đang được sử dụng trong hóa đơn.");
            }
        }
    }

    private int docDiemTichLuy() {
        String diemStr = txtDiemTichLuy.getText();

        if (diemStr == null || diemStr.trim().isEmpty()) {
            return 0;
        }

        return Integer.parseInt(diemStr.trim());
    }

    private void xoaRongForm() {
        maKhachHangDaChon = null;
        tableKhachHang.clearSelection();

        txtHoTen.setText("");
        txtSoDienThoai.setText("");
        txtDiaChi.setText("");
        txtDiemTichLuy.setText("0");

        txtHoTen.requestFocus();
    }

    private void capNhatBang() {
        tableModel.setRowCount(0);

        for (KhachHang kh : danhSachKhachHang) {
            Object[] row = {
                    kh.getMaDinhDanh(),
                    kh.getHoTen(),
                    kh.getSoDienThoai(),
                    kh.getDiaChi(),
                    kh.getDiemTichLuy()
            };

            tableModel.addRow(row);
        }
    }
}