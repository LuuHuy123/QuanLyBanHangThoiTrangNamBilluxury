package Human;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import database.DBConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class KhachHang extends Nguoi {

    private int diemTichLuy;

    @Override
    protected String layPrefix() {
        return "KH";
    }

    // ===== Constructor =====
    public KhachHang() {
        super();
        this.diemTichLuy = 0;
    }

    public KhachHang(String hoTen, String soDienThoai, String diaChi) {
        super(hoTen, soDienThoai, diaChi);
        this.diemTichLuy = 0;
    }

    public KhachHang(String hoTen, String soDienThoai, String diaChi, int diemTichLuy) {
        super(hoTen, soDienThoai, diaChi);
        setDiemTichLuy(diemTichLuy);
    }

    public KhachHang(String maDinhDanh, String hoTen, String soDienThoai, String diaChi, int diemTichLuy) {
        super(maDinhDanh, hoTen, soDienThoai, diaChi);
        setDiemTichLuy(diemTichLuy);
    }

    // ===== Getter / Setter =====
    public int getDiemTichLuy() {
        return diemTichLuy;
    }

    public void setDiemTichLuy(int diemTichLuy) {
        if (diemTichLuy < 0) {
            throw new IllegalArgumentException("Điểm tích lũy không được âm.");
        }
        this.diemTichLuy = diemTichLuy;
    }

    // ===== Nhập thông tin =====
    @Override
    public void nhapThongTin() {
        Scanner sc = new Scanner(System.in);

        System.out.println(">>> Mã khách hàng được tự động tạo: " + maDinhDanh);

        nhapChuoiCoRangBuoc(sc, "Họ tên: ", val -> setHoTen(val));
        nhapChuoiCoRangBuoc(sc, "Số điện thoại: ", val -> setSoDienThoai(val));
        nhapChuoiCoRangBuoc(sc, "Địa chỉ: ", val -> setDiaChi(val));

        this.diemTichLuy = 0;
        System.out.println("Điểm tích lũy ban đầu: 0");
    }

    // ===== Sửa thông tin =====
    @Override
    public void suaThongTin() {
        Scanner sc = new Scanner(System.in);

        nhapChuoiCoRangBuoc(sc, "Họ tên mới: ", val -> setHoTen(val));
        nhapChuoiCoRangBuoc(sc, "Số điện thoại mới: ", val -> setSoDienThoai(val));
        nhapChuoiCoRangBuoc(sc, "Địa chỉ mới: ", val -> setDiaChi(val));
    }

    // ===== Xuất thông tin =====
    @Override
    public void xuatThongTin() {
        super.xuatThongTin();
        System.out.println("Điểm tích lũy: " + diemTichLuy);
    }

    // ===== Xử lý điểm tích lũy =====
    public void congDiemTichLuy(int diemCong) {
        if (diemCong <= 0) {
            throw new IllegalArgumentException("Số điểm cộng phải lớn hơn 0.");
        }
        this.diemTichLuy += diemCong;
    }

    public void truDiemTichLuy(int diemTru) {
        if (diemTru <= 0) {
            throw new IllegalArgumentException("Số điểm trừ phải lớn hơn 0.");
        }

        if (diemTru > this.diemTichLuy) {
            throw new IllegalArgumentException("Số điểm trừ không được vượt quá điểm hiện có.");
        }

        this.diemTichLuy -= diemTru;
    }

    // Giữ lại tên cũ nếu GUI đang gọi hàm này
    public void capNhatDiemTichLuy(int diemCong) {
        congDiemTichLuy(diemCong);
    }

    // ===== SQL: INSERT =====
    @Override
    public void luuVaoSQL() {
        String sql = """
                INSERT INTO KhachHang
                (maDinhDanh, hoTen, soDienThoai, diaChi, diemTichLuy)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maDinhDanh);
            ps.setString(2, hoTen);
            ps.setString(3, soDienThoai);
            ps.setString(4, diaChi);
            ps.setInt(5, diemTichLuy);

            ps.executeUpdate();
            System.out.println("Lưu khách hàng thành công! Mã: " + maDinhDanh);

        } catch (Exception e) {
            System.out.println("Lưu khách hàng thất bại!");
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    // ===== SQL: SELECT =====
    public static List<KhachHang> docTuSQL() {
        List<KhachHang> danhSach = new ArrayList<>();

        String sql = """
                SELECT maDinhDanh, hoTen, soDienThoai, diaChi, diemTichLuy
                FROM KhachHang
                ORDER BY maDinhDanh
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                KhachHang kh = new KhachHang(
                        rs.getString("maDinhDanh"),
                        rs.getString("hoTen"),
                        rs.getString("soDienThoai"),
                        rs.getString("diaChi"),
                        rs.getInt("diemTichLuy")
                );

                danhSach.add(kh);
            }

        } catch (Exception e) {
            System.out.println("Đọc danh sách khách hàng thất bại!");
            System.out.println("Lỗi: " + e.getMessage());
        }

        return danhSach;
    }

    // ===== SQL: UPDATE =====
    public void capNhatSQL() {
        String sql = """
                UPDATE KhachHang
                SET hoTen = ?, soDienThoai = ?, diaChi = ?, diemTichLuy = ?
                WHERE maDinhDanh = ?
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, hoTen);
            ps.setString(2, soDienThoai);
            ps.setString(3, diaChi);
            ps.setInt(4, diemTichLuy);
            ps.setString(5, maDinhDanh);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Cập nhật khách hàng thành công! Mã: " + maDinhDanh);
            } else {
                System.out.println("Không tìm thấy khách hàng có mã: " + maDinhDanh);
            }

        } catch (Exception e) {
            System.out.println("Cập nhật khách hàng thất bại!");
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    // ===== SQL: DELETE =====
    public static void xoaKhoiSQL(String maDinhDanh) {
        String sql = "DELETE FROM KhachHang WHERE maDinhDanh = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maDinhDanh);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Xóa khách hàng thành công! Mã: " + maDinhDanh);
            } else {
                System.out.println("Không tìm thấy khách hàng có mã: " + maDinhDanh);
            }

        } catch (Exception e) {
            System.out.println("Xóa khách hàng thất bại!");
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    // ===== SQL: SEARCH =====
    public static List<KhachHang> timKiemSQL(String keyword) {
        List<KhachHang> ds = new ArrayList<>();

        String sql = """
                SELECT maDinhDanh, hoTen, soDienThoai, diaChi, diemTichLuy
                FROM KhachHang
                WHERE maDinhDanh LIKE ?
                   OR hoTen LIKE ?
                   OR soDienThoai LIKE ?
                   OR diaChi LIKE ?
                ORDER BY maDinhDanh
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String key = "%" + keyword + "%";

            ps.setString(1, key);
            ps.setString(2, key);
            ps.setString(3, key);
            ps.setString(4, key);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    KhachHang kh = new KhachHang(
                            rs.getString("maDinhDanh"),
                            rs.getString("hoTen"),
                            rs.getString("soDienThoai"),
                            rs.getString("diaChi"),
                            rs.getInt("diemTichLuy")
                    );

                    ds.add(kh);
                }
            }

        } catch (Exception e) {
            System.out.println("Tìm kiếm khách hàng thất bại!");
            System.out.println("Lỗi: " + e.getMessage());
        }

        return ds;
    }

    // ===== Hiển thị danh sách =====
    public static void hienThiDanhSach(List<KhachHang> ds) {
        if (ds == null || ds.isEmpty()) {
            System.out.println("Danh sách khách hàng rỗng.");
            return;
        }

        for (KhachHang kh : ds) {
            System.out.println("--------------------");
            kh.xuatThongTin();
        }
    }

    // ===== Tiện ích nhập =====
    private void nhapChuoiCoRangBuoc(
            Scanner sc,
            String label,
            java.util.function.Consumer<String> setter
    ) {
        while (true) {
            System.out.print(label);

            try {
                setter.accept(sc.nextLine());
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("[Lỗi] " + e.getMessage());
            }
        }
    }
}