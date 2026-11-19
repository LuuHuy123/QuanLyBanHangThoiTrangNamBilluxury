package Human;

import database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class HoaDon {

    public static final String TRANG_THAI_CHO = "Chờ thanh toán";
    public static final String TRANG_THAI_DA_THANH = "Đã thanh toán";
    public static final String TRANG_THAI_HUY = "Đã hủy";

    private static final DateTimeFormatter DT_FMT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private String maHoaDon;
    private String maKhachHang;
    private String maNhanVien;
    private LocalDateTime ngayLap;
    private double giamGia;
    private String trangThai;
    private String ghiChu;

    private final Map<String, double[]> chiTiet = new LinkedHashMap<>();

    private static String taoMaTuDong() {
        String uuid = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 6)
                .toUpperCase();

        return "HD-" + uuid;
    }

    public HoaDon() {
        this.maHoaDon = taoMaTuDong();
        this.ngayLap = LocalDateTime.now();
        this.giamGia = 0;
        this.trangThai = TRANG_THAI_CHO;
        this.ghiChu = "";
    }

    public HoaDon(String maKhachHang, String maNhanVien) {
        this();
        setMaKhachHang(maKhachHang);
        setMaNhanVien(maNhanVien);
    }

    public HoaDon(String maHoaDon,
                  String maKhachHang,
                  String maNhanVien,
                  LocalDateTime ngayLap,
                  double giamGia,
                  String trangThai,
                  String ghiChu) {

        setMaHoaDon(maHoaDon);
        setMaKhachHang(maKhachHang);
        setMaNhanVien(maNhanVien);

        this.ngayLap = ngayLap != null ? ngayLap : LocalDateTime.now();

        setGiamGiaKhongCanTongTien(giamGia);
        setTrangThai(trangThai);
        setGhiChu(ghiChu);
    }

    public String getMaHoaDon() {
        return maHoaDon;
    }

    public String getMaKhachHang() {
        return maKhachHang;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public LocalDateTime getNgayLap() {
        return ngayLap;
    }

    public String getNgayLapText() {
        return ngayLap != null ? ngayLap.format(DT_FMT) : "";
    }

    public double getGiamGia() {
        return giamGia;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public double getTongTien() {
        double tong = 0;

        for (double[] ct : chiTiet.values()) {
            tong += ct[0] * ct[1];
        }

        return tong;
    }

    public double getThanhToan() {
        return Math.max(0, getTongTien() - giamGia);
    }

    public Map<String, double[]> getChiTiet() {
        return Collections.unmodifiableMap(chiTiet);
    }

    public void setMaHoaDon(String maHoaDon) {
        if (maHoaDon == null || maHoaDon.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã hóa đơn không được để trống.");
        }

        maHoaDon = maHoaDon.trim().toUpperCase();

        if (!maHoaDon.matches("HD-[A-Z0-9]{6}")
                && !maHoaDon.matches("HD-[0-9]{6}")) {
            throw new IllegalArgumentException("Mã hóa đơn không hợp lệ.");
        }

        this.maHoaDon = maHoaDon;
    }

    public void setMaKhachHang(String maKhachHang) {
        if (maKhachHang == null || maKhachHang.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã khách hàng không được để trống.");
        }

        this.maKhachHang = maKhachHang.trim().toUpperCase();
    }

    public void setMaNhanVien(String maNhanVien) {
        if (maNhanVien == null || maNhanVien.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã nhân viên không được để trống.");
        }

        this.maNhanVien = maNhanVien.trim().toUpperCase();
    }

    public void setGiamGia(double giamGia) {
        if (giamGia < 0) {
            throw new IllegalArgumentException("Giảm giá không được âm.");
        }

        if (giamGia > getTongTien()) {
            throw new IllegalArgumentException("Giảm giá không được vượt quá tổng tiền.");
        }

        this.giamGia = giamGia;
    }

    private void setGiamGiaKhongCanTongTien(double giamGia) {
        if (giamGia < 0) {
            throw new IllegalArgumentException("Giảm giá không được âm.");
        }

        this.giamGia = giamGia;
    }

    public void setTrangThai(String trangThai) {
        if (trangThai == null || trangThai.trim().isEmpty()) {
            this.trangThai = TRANG_THAI_CHO;
            return;
        }

        trangThai = trangThai.trim();

        if (!trangThai.equals(TRANG_THAI_CHO)
                && !trangThai.equals(TRANG_THAI_DA_THANH)
                && !trangThai.equals(TRANG_THAI_HUY)) {
            throw new IllegalArgumentException("Trạng thái hóa đơn không hợp lệ.");
        }

        this.trangThai = trangThai;
    }

    public void setGhiChu(String ghiChu) {
        if (ghiChu == null) {
            this.ghiChu = "";
            return;
        }

        ghiChu = ghiChu.trim();

        if (ghiChu.length() > 500) {
            throw new IllegalArgumentException("Ghi chú không được vượt quá 500 ký tự.");
        }

        this.ghiChu = ghiChu;
    }

    public void themSanPham(String maSanPham, int soLuong, double donGia) {
        kiemTraTrangThaiCho();

        if (maSanPham == null || maSanPham.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã sản phẩm không được để trống.");
        }

        if (soLuong <= 0) {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0.");
        }

        if (donGia <= 0) {
            throw new IllegalArgumentException("Đơn giá phải lớn hơn 0.");
        }

        maSanPham = maSanPham.trim().toUpperCase();

        if (chiTiet.containsKey(maSanPham)) {
            chiTiet.get(maSanPham)[0] += soLuong;
        } else {
            chiTiet.put(maSanPham, new double[]{soLuong, donGia});
        }
    }

    public void themHang(String maSanPham, int soLuong, double donGia) {
        themSanPham(maSanPham, soLuong, donGia);
    }

    public void xoaSanPham(String maSanPham) {
        kiemTraTrangThaiCho();

        if (maSanPham == null || maSanPham.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã sản phẩm không được để trống.");
        }

        maSanPham = maSanPham.trim().toUpperCase();

        if (!chiTiet.containsKey(maSanPham)) {
            throw new IllegalArgumentException("Sản phẩm không có trong hóa đơn.");
        }

        chiTiet.remove(maSanPham);
    }

    public void capNhatSoLuong(String maSanPham, int soLuongMoi) {
        kiemTraTrangThaiCho();

        if (maSanPham == null || maSanPham.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã sản phẩm không được để trống.");
        }

        maSanPham = maSanPham.trim().toUpperCase();

        if (!chiTiet.containsKey(maSanPham)) {
            throw new IllegalArgumentException("Sản phẩm không có trong hóa đơn.");
        }

        if (soLuongMoi <= 0) {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0.");
        }

        chiTiet.get(maSanPham)[0] = soLuongMoi;
    }

    public void xacNhanThanhToan() {
        kiemTraTrangThaiCho();

        if (chiTiet.isEmpty()) {
            throw new IllegalStateException("Hóa đơn chưa có sản phẩm nào.");
        }

        this.trangThai = TRANG_THAI_DA_THANH;
    }

    public void huyHoaDon() {
        if (TRANG_THAI_HUY.equals(trangThai)) {
            throw new IllegalStateException("Hóa đơn đã bị hủy trước đó.");
        }

        if (TRANG_THAI_DA_THANH.equals(trangThai)) {
            throw new IllegalStateException("Không thể hủy hóa đơn đã thanh toán.");
        }

        this.trangThai = TRANG_THAI_HUY;
    }

    private void kiemTraTrangThaiCho() {
        if (!TRANG_THAI_CHO.equals(trangThai)) {
            throw new IllegalStateException(
                    "Chỉ có thể thao tác khi hóa đơn ở trạng thái chờ thanh toán.");
        }
    }

    public void xuatThongTin() {
        System.out.println("Mã hóa đơn: " + maHoaDon);
        System.out.println("Mã khách hàng: " + maKhachHang);
        System.out.println("Mã nhân viên: " + maNhanVien);
        System.out.println("Ngày lập: " + getNgayLapText());
        System.out.println("Trạng thái: " + trangThai);
        System.out.println("--- Chi tiết hóa đơn ---");

        for (Map.Entry<String, double[]> e : chiTiet.entrySet()) {
            double soLuong = e.getValue()[0];
            double donGia = e.getValue()[1];

            System.out.printf(
                    "%-15s SL: %.0f x %,.0f = %,.0f VNĐ%n",
                    e.getKey(),
                    soLuong,
                    donGia,
                    soLuong * donGia
            );
        }

        System.out.printf("Tổng tiền: %,.0f VNĐ%n", getTongTien());
        System.out.printf("Giảm giá: %,.0f VNĐ%n", giamGia);
        System.out.printf("Thanh toán: %,.0f VNĐ%n", getThanhToan());
        System.out.println("Ghi chú: "
                + (ghiChu == null || ghiChu.isEmpty() ? "(không có)" : ghiChu));
    }

    public void luuVaoSQL() {
        if (chiTiet.isEmpty()) {
            throw new IllegalStateException("Hóa đơn chưa có sản phẩm nào, không thể lưu.");
        }

        String sqlHD = """
                INSERT INTO HoaDon
                (maHoaDon, maKhachHang, maNhanVien, ngayLap,
                 tongTien, giamGia, thanhToan, trangThai, ghiChu)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        String sqlCTHD = """
                INSERT INTO ChiTietHoaDon
                (maHoaDon, maSanPham, soLuong, donGia)
                VALUES (?, ?, ?, ?)
                """;

        Connection conn = null;

        try {
            conn = DBConnection.getConnection();

            if (conn == null) {
                throw new IllegalStateException("Không kết nối được database.");
            }

            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sqlHD)) {
                ps.setString(1, maHoaDon);
                ps.setString(2, maKhachHang);
                ps.setString(3, maNhanVien);
                ps.setTimestamp(4, Timestamp.valueOf(ngayLap));
                ps.setDouble(5, getTongTien());
                ps.setDouble(6, giamGia);
                ps.setDouble(7, getThanhToan());
                ps.setString(8, trangThai);
                ps.setString(9, ghiChu);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlCTHD)) {
                for (Map.Entry<String, double[]> e : chiTiet.entrySet()) {
                    ps.setString(1, maHoaDon);
                    ps.setString(2, e.getKey());
                    ps.setInt(3, (int) e.getValue()[0]);
                    ps.setDouble(4, e.getValue()[1]);
                    ps.addBatch();
                }

                ps.executeBatch();
            }

            conn.commit();
            System.out.println("Lưu hóa đơn thành công! Mã: " + maHoaDon);

        } catch (Exception e) {
            rollback(conn);

            System.out.println("Lưu hóa đơn thất bại!");
            System.out.println("Lỗi: " + e.getMessage());

            throw new RuntimeException(e);

        } finally {
            dongKetNoi(conn);
        }
    }

    public static List<HoaDon> docTuSQL() {
        List<HoaDon> ds = new ArrayList<>();

        String sql = """
                SELECT maHoaDon, maKhachHang, maNhanVien, ngayLap,
                       tongTien, giamGia, thanhToan, trangThai, ghiChu
                FROM HoaDon
                ORDER BY maHoaDon
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                HoaDon hd = taoHoaDonTuResultSet(rs);
                hd.napChiTietTuSQL();
                ds.add(hd);
            }

        } catch (Exception e) {
            System.out.println("Đọc danh sách hóa đơn thất bại!");
            System.out.println("Lỗi: " + e.getMessage());
        }

        return ds;
    }

    private void napChiTietTuSQL() {
        String sql = """
                SELECT maSanPham, soLuong, donGia
                FROM ChiTietHoaDon
                WHERE maHoaDon = ?
                """;

        chiTiet.clear();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maHoaDon);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    chiTiet.put(
                            rs.getString("maSanPham"),
                            new double[]{
                                    rs.getInt("soLuong"),
                                    rs.getDouble("donGia")
                            }
                    );
                }
            }

        } catch (Exception e) {
            System.out.println("Nạp chi tiết hóa đơn thất bại!");
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    public void capNhatTrangThaiSQL() {
        String sql = "UPDATE HoaDon SET trangThai = ? WHERE maHoaDon = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, trangThai);
            ps.setString(2, maHoaDon);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Cập nhật trạng thái hóa đơn thành công!");
            } else {
                System.out.println("Không tìm thấy hóa đơn có mã: " + maHoaDon);
            }

        } catch (Exception e) {
            System.out.println("Cập nhật trạng thái hóa đơn thất bại!");
            System.out.println("Lỗi: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static void xoaKhoiSQL(String maHoaDon) {
        String sqlCT = "DELETE FROM ChiTietHoaDon WHERE maHoaDon = ?";
        String sqlHD = "DELETE FROM HoaDon WHERE maHoaDon = ?";

        Connection conn = null;

        try {
            conn = DBConnection.getConnection();

            if (conn == null) {
                throw new IllegalStateException("Không kết nối được database.");
            }

            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sqlCT)) {
                ps.setString(1, maHoaDon);
                ps.executeUpdate();
            }

            int rows;

            try (PreparedStatement ps = conn.prepareStatement(sqlHD)) {
                ps.setString(1, maHoaDon);
                rows = ps.executeUpdate();
            }

            conn.commit();

            if (rows > 0) {
                System.out.println("Xóa hóa đơn thành công! Mã: " + maHoaDon);
            } else {
                System.out.println("Không tìm thấy hóa đơn có mã: " + maHoaDon);
            }

        } catch (Exception e) {
            rollback(conn);

            System.out.println("Xóa hóa đơn thất bại!");
            System.out.println("Lỗi: " + e.getMessage());

            throw new RuntimeException(e);

        } finally {
            dongKetNoi(conn);
        }
    }

    public static List<HoaDon> timKiemSQL(String keyword) {
        List<HoaDon> ds = new ArrayList<>();

        String sql = """
                SELECT maHoaDon, maKhachHang, maNhanVien, ngayLap,
                       tongTien, giamGia, thanhToan, trangThai, ghiChu
                FROM HoaDon
                WHERE maHoaDon LIKE ?
                   OR maKhachHang LIKE ?
                   OR maNhanVien LIKE ?
                   OR trangThai LIKE ?
                ORDER BY maHoaDon
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
                    HoaDon hd = taoHoaDonTuResultSet(rs);
                    hd.napChiTietTuSQL();
                    ds.add(hd);
                }
            }

        } catch (Exception e) {
            System.out.println("Tìm kiếm hóa đơn thất bại!");
            System.out.println("Lỗi: " + e.getMessage());
        }

        return ds;
    }

    public static List<HoaDon> locTheoTrangThaiSQL(String trangThai) {
        List<HoaDon> ds = new ArrayList<>();

        String sql = """
                SELECT maHoaDon, maKhachHang, maNhanVien, ngayLap,
                       tongTien, giamGia, thanhToan, trangThai, ghiChu
                FROM HoaDon
                WHERE trangThai = ?
                ORDER BY maHoaDon
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, trangThai);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    HoaDon hd = taoHoaDonTuResultSet(rs);
                    hd.napChiTietTuSQL();
                    ds.add(hd);
                }
            }

        } catch (Exception e) {
            System.out.println("Lọc hóa đơn thất bại!");
            System.out.println("Lỗi: " + e.getMessage());
        }

        return ds;
    }

    private static HoaDon taoHoaDonTuResultSet(ResultSet rs) throws Exception {
        Timestamp ts = rs.getTimestamp("ngayLap");
        LocalDateTime ngayLap = ts != null ? ts.toLocalDateTime() : LocalDateTime.now();

        return new HoaDon(
                rs.getString("maHoaDon"),
                rs.getString("maKhachHang"),
                rs.getString("maNhanVien"),
                ngayLap,
                rs.getDouble("giamGia"),
                rs.getString("trangThai"),
                rs.getString("ghiChu")
        );
    }

    private static void rollback(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private static void dongKetNoi(Connection conn) {
        if (conn != null) {
            try {
                conn.setAutoCommit(true);
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void hienThiDanhSach(List<HoaDon> ds) {
        if (ds == null || ds.isEmpty()) {
            System.out.println("Danh sách hóa đơn rỗng.");
            return;
        }

        for (HoaDon hd : ds) {
            System.out.println("====================");
            hd.xuatThongTin();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof HoaDon)) {
            return false;
        }

        HoaDon other = (HoaDon) obj;

        return maHoaDon != null && maHoaDon.equals(other.maHoaDon);
    }

    @Override
    public int hashCode() {
        return maHoaDon != null ? maHoaDon.hashCode() : 0;
    }
}