package GUI;

import Human.NhanVien;
import Human.TaiKhoan;
import database.DBConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class NhanVienPanel extends JPanel {

    private JTextField txtHoTen;
    private JTextField txtSoDienThoai;
    private JTextField txtDiaChi;
    private JTextField txtNgaySinh;
    private JTextField txtEmail;
    private JTextField txtCccd;
    private JTextField txtChucVu;
    private JTextField txtLuong;
    private JTextField txtGioVaoLam;
    private JTextField txtGioKetThuc;
    private JTextField txtTimKiem;

    private JTable tableNhanVien;
    private DefaultTableModel tableModel;

    private List<NhanVien> danhSachNhanVien;
    private String maNhanVienDaChon = null;

    private static final Color BG_PANEL = new Color(28, 25, 22);
    private static final Color CARD_DARK = new Color(39, 37, 34);
    private static final Color INPUT_DARK = new Color(48, 43, 38);
    private static final Color TABLE_DARK = new Color(35, 32, 29);
    private static final Color TABLE_DARK_2 = new Color(42, 39, 35);
    private static final Color GOLD_LIGHT = new Color(223, 196, 162);
    private static final Color GOLD_DARK = new Color(124, 88, 49);
    private static final Color TEXT_LIGHT = new Color(245, 239, 230);
    private static final Color BORDER = new Color(84, 72, 56);

    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public NhanVienPanel() {
        danhSachNhanVien = NhanVien.docTuSQL();

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
        pnlTimKiem.setBorder(taoTitledBorder("Tìm kiếm nhân viên"));

        pnlTimKiem.add(taoLabel("Tìm kiếm Mã/Tên/SĐT/Email/CCCD/Chức vụ:"));
        txtTimKiem = taoTextField(25);

        JButton btnTimKiem = taoNut("Tìm Kiếm", new Color(218, 174, 88));
        JButton btnHienTatCa = taoNut("Hiển Thị Tất Cả", new Color(120, 105, 85));

        pnlTimKiem.add(txtTimKiem);
        pnlTimKiem.add(btnTimKiem);
        pnlTimKiem.add(btnHienTatCa);

        JPanel pnlInput = new JPanel(new GridLayout(5, 4, 10, 10));
        pnlInput.setBackground(CARD_DARK);
        pnlInput.setBorder(taoTitledBorder("Thông tin nhân viên"));

        pnlInput.add(taoLabel("Họ tên:"));
        txtHoTen = taoTextField();
        pnlInput.add(txtHoTen);

        pnlInput.add(taoLabel("Số điện thoại:"));
        txtSoDienThoai = taoTextField();
        pnlInput.add(txtSoDienThoai);

        pnlInput.add(taoLabel("Địa chỉ:"));
        txtDiaChi = taoTextField();
        pnlInput.add(txtDiaChi);

        pnlInput.add(taoLabel("Ngày sinh (dd/MM/yyyy):"));
        txtNgaySinh = taoTextField();
        pnlInput.add(txtNgaySinh);

        pnlInput.add(taoLabel("Email:"));
        txtEmail = taoTextField();
        pnlInput.add(txtEmail);

        pnlInput.add(taoLabel("CCCD:"));
        txtCccd = taoTextField();
        pnlInput.add(txtCccd);

        pnlInput.add(taoLabel("Chức vụ:"));
        txtChucVu = taoTextField();
        pnlInput.add(txtChucVu);

        pnlInput.add(taoLabel("Lương:"));
        txtLuong = taoTextField();
        pnlInput.add(txtLuong);

        pnlInput.add(taoLabel("Giờ vào làm (HH:mm):"));
        txtGioVaoLam = taoTextField();
        pnlInput.add(txtGioVaoLam);

        pnlInput.add(taoLabel("Giờ kết thúc (HH:mm):"));
        txtGioKetThuc = taoTextField();
        pnlInput.add(txtGioKetThuc);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pnlButtons.setBackground(CARD_DARK);

        JButton btnThem = taoNut("Thêm Nhân Viên", new Color(70, 145, 210));
        JButton btnSua = taoNut("Sửa Thông Tin", new Color(218, 174, 88));
        JButton btnXoa = taoNut("Xóa Nhân Viên", new Color(210, 65, 55));
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
                "Mã NV", "Họ Tên", "SĐT", "Địa Chỉ", "Ngày Sinh",
                "Email", "CCCD", "Chức Vụ", "Lương", "Vào Làm", "Kết Thúc"
        };

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableNhanVien = new JTable(tableModel);
        tableNhanVien.setRowHeight(28);
        tableNhanVien.setBackground(TABLE_DARK);
        tableNhanVien.setForeground(TEXT_LIGHT);
        tableNhanVien.setGridColor(BORDER);
        tableNhanVien.setSelectionBackground(GOLD_DARK);
        tableNhanVien.setSelectionForeground(Color.WHITE);
        tableNhanVien.setShowVerticalLines(true);
        tableNhanVien.setShowHorizontalLines(true);
        tableNhanVien.getTableHeader().setBackground(CARD_DARK);
        tableNhanVien.getTableHeader().setForeground(GOLD_LIGHT);
        tableNhanVien.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tableNhanVien.getTableHeader().setReorderingAllowed(false);
        canGiuaHeaderVaFixKhoangTrangBang(tableNhanVien);
        tableNhanVien.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
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
        tableNhanVien.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = taoScrollPane(tableNhanVien, "Danh sách nhân viên");
        add(scrollPane, BorderLayout.CENTER);

        btnTimKiem.addActionListener(e -> xuLyTimKiem());

        btnHienTatCa.addActionListener(e -> {
            txtTimKiem.setText("");
            danhSachNhanVien = NhanVien.docTuSQL();
            capNhatBang();
            xoaRongForm();
        });

        btnThem.addActionListener(this::xuLyThemNhanVien);
        btnSua.addActionListener(this::xuLySuaNhanVien);
        btnXoa.addActionListener(this::xuLyXoaNhanVien);
        btnClear.addActionListener(e -> xoaRongForm());
    }

    private void ganSuKien() {
        tableNhanVien.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableNhanVien.getSelectedRow();

                if (row >= 0) {
                    maNhanVienDaChon = tableModel.getValueAt(row, 0).toString();

                    for (NhanVien nv : danhSachNhanVien) {
                        if (nv.getMaDinhDanh().equals(maNhanVienDaChon)) {
                            txtHoTen.setText(nv.getHoTen() != null ? nv.getHoTen() : "");
                            txtSoDienThoai.setText(nv.getSoDienThoai() != null ? nv.getSoDienThoai() : "");
                            txtDiaChi.setText(nv.getDiaChi() != null ? nv.getDiaChi() : "");
                            txtNgaySinh.setText(nv.getNgaySinh() != null ? DATE_FMT.format(nv.getNgaySinh()) : "");
                            txtEmail.setText(nv.getEmail() != null ? nv.getEmail() : "");
                            txtCccd.setText(nv.getCccd() != null ? nv.getCccd() : "");
                            txtChucVu.setText(nv.getChucVu() != null ? nv.getChucVu() : "");
                            txtLuong.setText(String.format("%.0f", nv.getLuong()));
                            txtGioVaoLam.setText(nv.getGioVaoLam() != null ? nv.getGioVaoLam().toString() : "");
                            txtGioKetThuc.setText(nv.getGioKetThuc() != null ? nv.getGioKetThuc().toString() : "");
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
            danhSachNhanVien = NhanVien.docTuSQL();
        } else {
            danhSachNhanVien = NhanVien.timKiemSQL(keyword);
        }

        capNhatBang();
    }

    private void xuLyThemNhanVien(ActionEvent e) {
        try {
            NhanVien nv = new NhanVien();

            nv.setHoTen(txtHoTen.getText());
            nv.setSoDienThoai(txtSoDienThoai.getText());
            nv.setDiaChi(txtDiaChi.getText());
            nv.setNgaySinh(txtNgaySinh.getText());
            nv.setEmail(txtEmail.getText());
            nv.setCccd(txtCccd.getText());
            nv.setChucVu(txtChucVu.getText());
            nv.setLuong(docLuong());
            nv.setGioVaoLam(txtGioVaoLam.getText());
            nv.setGioKetThuc(txtGioKetThuc.getText());

            nv.luuVaoSQL();
            taoTaiKhoanChoNhanVien(nv);

            danhSachNhanVien = NhanVien.docTuSQL();
            capNhatBang();
            xoaRongForm();

            AppDialog.showSuccess(this, "Thông báo", "Thêm nhân viên thành công!");

        } catch (NumberFormatException ex) {
            AppDialog.showError(this, "Lỗi nhập liệu", "Lương phải là số!");

        } catch (IllegalArgumentException ex) {
            AppDialog.showError(this, "Lỗi nhập liệu", ex.getMessage());

        } catch (Exception ex) {
            AppDialog.showError(this, "Lỗi", "Lỗi: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void xuLySuaNhanVien(ActionEvent e) {
        if (maNhanVienDaChon == null) {
            AppDialog.showWarning(this, "Thông báo", "Vui lòng chọn nhân viên cần sửa!");
            return;
        }

        try {
            NhanVien nv = null;

            for (NhanVien item : danhSachNhanVien) {
                if (item.getMaDinhDanh().equals(maNhanVienDaChon)) {
                    nv = item;
                    break;
                }
            }

            if (nv == null) {
                AppDialog.showError(this, "Lỗi", "Không tìm thấy nhân viên đã chọn!");
                return;
            }

            String soDienThoaiCu = nv.getSoDienThoai();

            nv.setHoTen(txtHoTen.getText());
            nv.setSoDienThoai(txtSoDienThoai.getText());
            nv.setDiaChi(txtDiaChi.getText());
            nv.setNgaySinh(txtNgaySinh.getText());
            nv.setEmail(txtEmail.getText());
            nv.setCccd(txtCccd.getText());
            nv.setChucVu(txtChucVu.getText());
            nv.setLuong(docLuong());
            nv.setGioVaoLam(txtGioVaoLam.getText());
            nv.setGioKetThuc(txtGioKetThuc.getText());

            nv.capNhatSQL();
            capNhatTaiKhoanNhanVien(soDienThoaiCu, nv);

            danhSachNhanVien = NhanVien.docTuSQL();
            capNhatBang();
            xoaRongForm();

            AppDialog.showSuccess(this, "Thông báo", "Sửa thông tin nhân viên thành công!");

        } catch (NumberFormatException ex) {
            AppDialog.showError(this, "Lỗi nhập liệu", "Lương phải là số!");

        } catch (IllegalArgumentException ex) {
            AppDialog.showError(this, "Lỗi nhập liệu", ex.getMessage());

        } catch (Exception ex) {
            AppDialog.showError(this, "Lỗi", "Lỗi: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void xuLyXoaNhanVien(ActionEvent e) {
        if (maNhanVienDaChon == null) {
            AppDialog.showWarning(this, "Thông báo", "Vui lòng chọn nhân viên cần xóa!");
            return;
        }

        boolean xacNhan = AppDialog.showConfirm(
                this,
                "Xác nhận xóa",
                "Bạn có chắc chắn muốn xóa nhân viên " + maNhanVienDaChon + "?"
        );

        if (xacNhan) {
            try {
                String soDienThoaiCanKhoa = laySoDienThoaiNhanVienDangChon();

                NhanVien.xoaKhoiSQL(maNhanVienDaChon);
                khoaTaiKhoanNhanVien(soDienThoaiCanKhoa);

                danhSachNhanVien = NhanVien.docTuSQL();
                capNhatBang();
                xoaRongForm();

                AppDialog.showSuccess(this, "Thông báo", "Xóa nhân viên thành công!");

            } catch (Exception ex) {
                AppDialog.showError(this, "Lỗi", "Không thể xóa nhân viên. Nhân viên có thể đang được sử dụng trong hóa đơn.");
            }
        }
    }


    private void taoTaiKhoanChoNhanVien(NhanVien nv) {
        try {
            TaiKhoan tk = new TaiKhoan(
                    nv.getSoDienThoai(),
                    nv.getCccd(),
                    TaiKhoan.VAI_TRO_STAFF,
                    nv.getHoTen(),
                    nv.getEmail()
            );
            tk.luuVaoSQL();
        } catch (Exception ex) {
            AppDialog.showWarning(
                    this,
                    "Cảnh báo tài khoản",
                    "Nhân viên đã được lưu nhưng chưa tạo được tài khoản đăng nhập. Lý do: " + ex.getMessage()
            );
        }
    }

    private void capNhatTaiKhoanNhanVien(String soDienThoaiCu, NhanVien nv) {
        if (soDienThoaiCu == null || soDienThoaiCu.trim().isEmpty()) {
            taoTaiKhoanChoNhanVien(nv);
            return;
        }

        String sql = "UPDATE TaiKhoan SET tenDangNhap=?, matKhau=?, hoTen=?, email=?, vaiTro=?, trangThai=1 WHERE tenDangNhap=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nv.getSoDienThoai().trim().toLowerCase());
            ps.setString(2, TaiKhoan.hashSHA256(nv.getCccd()));
            ps.setString(3, nv.getHoTen());
            ps.setString(4, nv.getEmail() == null ? "" : nv.getEmail());
            ps.setString(5, TaiKhoan.VAI_TRO_STAFF);
            ps.setString(6, soDienThoaiCu.trim().toLowerCase());

            int affected = ps.executeUpdate();
            if (affected == 0) {
                taoTaiKhoanChoNhanVien(nv);
            }

        } catch (Exception ex) {
            AppDialog.showWarning(
                    this,
                    "Cảnh báo tài khoản",
                    "Thông tin nhân viên đã được cập nhật nhưng tài khoản đăng nhập chưa đồng bộ được. Lý do: " + ex.getMessage()
            );
        }
    }

    private String laySoDienThoaiNhanVienDangChon() {
        if (maNhanVienDaChon == null) return "";

        for (NhanVien nv : danhSachNhanVien) {
            if (nv.getMaDinhDanh().equals(maNhanVienDaChon)) {
                return nv.getSoDienThoai();
            }
        }
        return "";
    }

    private void khoaTaiKhoanNhanVien(String soDienThoai) {
        if (soDienThoai == null || soDienThoai.trim().isEmpty()) return;

        String sql = "UPDATE TaiKhoan SET trangThai=0 WHERE tenDangNhap=? AND vaiTro=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, soDienThoai.trim().toLowerCase());
            ps.setString(2, TaiKhoan.VAI_TRO_STAFF);
            ps.executeUpdate();

        } catch (Exception ex) {
            AppDialog.showWarning(
                    this,
                    "Cảnh báo tài khoản",
                    "Nhân viên đã được xóa nhưng tài khoản đăng nhập chưa khóa được. Lý do: " + ex.getMessage()
            );
        }
    }

    private double docLuong() {
        String luongStr = txtLuong.getText();

        if (luongStr == null || luongStr.trim().isEmpty()) {
            throw new NumberFormatException("Lương không được để trống.");
        }

        return Double.parseDouble(luongStr.trim());
    }

    private void xoaRongForm() {
        maNhanVienDaChon = null;
        tableNhanVien.clearSelection();

        txtHoTen.setText("");
        txtSoDienThoai.setText("");
        txtDiaChi.setText("");
        txtNgaySinh.setText("");
        txtEmail.setText("");
        txtCccd.setText("");
        txtChucVu.setText("");
        txtLuong.setText("");
        txtGioVaoLam.setText("");
        txtGioKetThuc.setText("");

        txtHoTen.requestFocus();
    }

    private void capNhatBang() {
        tableModel.setRowCount(0);

        for (NhanVien nv : danhSachNhanVien) {
            Object[] row = {
                    nv.getMaDinhDanh(),
                    nv.getHoTen(),
                    nv.getSoDienThoai(),
                    nv.getDiaChi(),
                    nv.getNgaySinh() != null ? DATE_FMT.format(nv.getNgaySinh()) : "",
                    nv.getEmail(),
                    nv.getCccd(),
                    nv.getChucVu(),
                    String.format("%,.0f", nv.getLuong()),
                    nv.getGioVaoLam() != null ? nv.getGioVaoLam().toString() : "",
                    nv.getGioKetThuc() != null ? nv.getGioKetThuc().toString() : ""
            };

            tableModel.addRow(row);
        }
    }
}