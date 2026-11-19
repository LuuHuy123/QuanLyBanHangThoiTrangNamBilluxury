package Human;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import database.DBConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.function.Consumer;

public class SanPham {

    private String maSanPham;
    private String tenSanPham;
    private String donVi;
    private double giaNhap;
    private double giaBan;
    private int soLuongTon;
    private String loaiSanPham;
    private String thuongHieu;
    private String xuatXu;
    private String maNCC;
    private String moTa;

    private static String taoMaTuDong() {
        String uuid = UUID.randomUUID().toString()
                .replace("-", "")
                .substring(0, 6)
                .toUpperCase();
        return "SP-" + uuid;
    }

    public SanPham() {
        this.maSanPham = taoMaTuDong();
        this.donVi = "cái";
        this.soLuongTon = 0;
        this.moTa = "";
    }

    public SanPham(String tenSanPham, String donVi, double giaNhap, double giaBan,
                   int soLuongTon, String loaiSanPham, String thuongHieu,
                   String xuatXu, String maNCC, String moTa) {
        this.maSanPham = taoMaTuDong();
        setTenSanPham(tenSanPham);
        setDonVi(donVi);
        setGiaNhap(giaNhap);
        setGiaBan(giaBan);
        setSoLuongTon(soLuongTon);
        setLoaiSanPham(loaiSanPham);
        setThuongHieu(thuongHieu);
        setXuatXu(xuatXu);
        setMaNCC(maNCC);
        setMoTa(moTa);
    }

    public SanPham(String maSanPham, String tenSanPham, String donVi,
                   double giaNhap, double giaBan, int soLuongTon,
                   String loaiSanPham, String thuongHieu, String xuatXu,
                   String maNCC, String moTa) {
        setMaSanPham(maSanPham);
        setTenSanPham(tenSanPham);
        setDonVi(donVi);
        setGiaNhap(giaNhap);
        setGiaBan(giaBan);
        setSoLuongTon(soLuongTon);
        setLoaiSanPham(loaiSanPham);
        setThuongHieu(thuongHieu);
        setXuatXu(xuatXu);
        setMaNCC(maNCC);
        setMoTa(moTa);
    }

    public String getMaSanPham() {
        return maSanPham;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public String getDonVi() {
        return donVi;
    }

    public double getGiaNhap() {
        return giaNhap;
    }

    public double getGiaBan() {
        return giaBan;
    }

    public int getSoLuongTon() {
        return soLuongTon;
    }

    public String getLoaiSanPham() {
        return loaiSanPham;
    }

    public String getThuongHieu() {
        return thuongHieu;
    }

    public String getXuatXu() {
        return xuatXu;
    }

    public String getMaNCC() {
        return maNCC;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMaSanPham(String maSanPham) {
        if (maSanPham == null || maSanPham.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã sản phẩm không được để trống.");
        }

        maSanPham = maSanPham.trim().toUpperCase();

        if (!maSanPham.matches("SP-[A-Z0-9]{6}") && !maSanPham.matches("SP-[0-9]{3}")) {
            throw new IllegalArgumentException("Mã sản phẩm không hợp lệ.");
        }

        this.maSanPham = maSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        if (tenSanPham == null || tenSanPham.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên sản phẩm không được để trống.");
        }

        tenSanPham = tenSanPham.trim();

        if (tenSanPham.length() < 2 || tenSanPham.length() > 200) {
            throw new IllegalArgumentException("Tên sản phẩm phải từ 2 đến 200 ký tự.");
        }

        this.tenSanPham = tenSanPham;
    }

    public void setDonVi(String donVi) {
        if (donVi == null || donVi.trim().isEmpty()) {
            donVi = "cái";
        }

        donVi = donVi.trim();

        if (donVi.length() > 50) {
            throw new IllegalArgumentException("Đơn vị không được vượt quá 50 ký tự.");
        }

        this.donVi = donVi;
    }

    public void setGiaNhap(double giaNhap) {
        if (giaNhap < 0) {
            throw new IllegalArgumentException("Giá nhập không được âm.");
        }

        this.giaNhap = giaNhap;

        if (this.giaBan > 0 && this.giaBan < this.giaNhap) {
            throw new IllegalArgumentException("Giá bán không được thấp hơn giá nhập.");
        }
    }

    public void setGiaBan(double giaBan) {
        if (giaBan < 0) {
            throw new IllegalArgumentException("Giá bán không được âm.");
        }

        if (giaBan < this.giaNhap) {
            throw new IllegalArgumentException("Giá bán không được thấp hơn giá nhập.");
        }

        this.giaBan = giaBan;
    }

    public void setSoLuongTon(int soLuongTon) {
        if (soLuongTon < 0) {
            throw new IllegalArgumentException("Số lượng tồn không được âm.");
        }

        this.soLuongTon = soLuongTon;
    }

    public void setLoaiSanPham(String loaiSanPham) {
        if (loaiSanPham == null || loaiSanPham.trim().isEmpty()) {
            throw new IllegalArgumentException("Loại sản phẩm không được để trống.");
        }

        loaiSanPham = loaiSanPham.trim();

        if (loaiSanPham.length() > 100) {
            throw new IllegalArgumentException("Loại sản phẩm không được vượt quá 100 ký tự.");
        }

        this.loaiSanPham = loaiSanPham;
    }

    public void setThuongHieu(String thuongHieu) {
        if (thuongHieu == null || thuongHieu.trim().isEmpty()) {
            throw new IllegalArgumentException("Thương hiệu không được để trống.");
        }

        thuongHieu = thuongHieu.trim();

        if (thuongHieu.length() > 100) {
            throw new IllegalArgumentException("Thương hiệu không được vượt quá 100 ký tự.");
        }

        this.thuongHieu = thuongHieu;
    }

    public void setXuatXu(String xuatXu) {
        if (xuatXu == null || xuatXu.trim().isEmpty()) {
            throw new IllegalArgumentException("Xuất xứ không được để trống.");
        }

        xuatXu = xuatXu.trim();

        if (xuatXu.length() > 100) {
            throw new IllegalArgumentException("Xuất xứ không được vượt quá 100 ký tự.");
        }

        this.xuatXu = xuatXu;
    }

    public void setMaNCC(String maNCC) {
        if (maNCC == null || maNCC.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã nhà cung cấp không được để trống.");
        }

        this.maNCC = maNCC.trim().toUpperCase();
    }

    public void setMoTa(String moTa) {
        if (moTa == null) {
            this.moTa = "";
            return;
        }

        moTa = moTa.trim();

        if (moTa.length() > 500) {
            throw new IllegalArgumentException("Mô tả không được vượt quá 500 ký tự.");
        }

        this.moTa = moTa;
    }

    public void nhapThem(int soLuong) {
        if (soLuong <= 0) {
            throw new IllegalArgumentException("Số lượng nhập thêm phải lớn hơn 0.");
        }

        this.soLuongTon += soLuong;
    }

    public void xuatHang(int soLuong) {
        if (soLuong <= 0) {
            throw new IllegalArgumentException("Số lượng xuất phải lớn hơn 0.");
        }

        if (soLuong > this.soLuongTon) {
            throw new IllegalArgumentException("Số lượng xuất vượt quá tồn kho.");
        }

        this.soLuongTon -= soLuong;
    }

    public double tinhLoiNhuan() {
        return giaBan - giaNhap;
    }

    public boolean conHang() {
        return soLuongTon > 0;
    }

    public void nhapThongTin() {
        Scanner sc = new Scanner(System.in);

        System.out.println(">>> Mã sản phẩm được tự động tạo: " + maSanPham);

        nhapCoRangBuoc(sc, "Tên sản phẩm: ", val -> setTenSanPham(val));
        nhapCoRangBuoc(sc, "Đơn vị tính: ", val -> setDonVi(val));

        while (true) {
            System.out.print("Giá nhập: ");
            try {
                setGiaNhap(Double.parseDouble(sc.nextLine().trim()));
                break;
            } catch (Exception e) {
                System.out.println("[Lỗi] Giá nhập phải là số >= 0.");
            }
        }

        while (true) {
            System.out.print("Giá bán: ");
            try {
                setGiaBan(Double.parseDouble(sc.nextLine().trim()));
                break;
            } catch (Exception e) {
                System.out.println("[Lỗi] Giá bán phải là số và >= giá nhập.");
            }
        }

        while (true) {
            System.out.print("Số lượng tồn: ");
            try {
                setSoLuongTon(Integer.parseInt(sc.nextLine().trim()));
                break;
            } catch (Exception e) {
                System.out.println("[Lỗi] Số lượng tồn phải là số nguyên >= 0.");
            }
        }

        nhapCoRangBuoc(sc, "Loại sản phẩm: ", val -> setLoaiSanPham(val));
        nhapCoRangBuoc(sc, "Thương hiệu: ", val -> setThuongHieu(val));
        nhapCoRangBuoc(sc, "Xuất xứ: ", val -> setXuatXu(val));
        nhapCoRangBuoc(sc, "Mã nhà cung cấp: ", val -> setMaNCC(val));
        nhapCoRangBuoc(sc, "Mô tả: ", val -> setMoTa(val));
    }

    public void suaThongTin() {
        Scanner sc = new Scanner(System.in);

        System.out.println(">>> Sửa thông tin sản phẩm [" + maSanPham + "]");

        nhapCoRangBuoc(sc, "Tên sản phẩm mới: ", val -> setTenSanPham(val));
        nhapCoRangBuoc(sc, "Đơn vị mới: ", val -> setDonVi(val));

        while (true) {
            System.out.print("Giá nhập mới: ");
            try {
                setGiaNhap(Double.parseDouble(sc.nextLine().trim()));
                break;
            } catch (Exception e) {
                System.out.println("[Lỗi] Giá nhập phải là số >= 0.");
            }
        }

        while (true) {
            System.out.print("Giá bán mới: ");
            try {
                setGiaBan(Double.parseDouble(sc.nextLine().trim()));
                break;
            } catch (Exception e) {
                System.out.println("[Lỗi] Giá bán phải là số và >= giá nhập.");
            }
        }

        while (true) {
            System.out.print("Số lượng tồn mới: ");
            try {
                setSoLuongTon(Integer.parseInt(sc.nextLine().trim()));
                break;
            } catch (Exception e) {
                System.out.println("[Lỗi] Số lượng tồn phải là số nguyên >= 0.");
            }
        }

        nhapCoRangBuoc(sc, "Loại sản phẩm mới: ", val -> setLoaiSanPham(val));
        nhapCoRangBuoc(sc, "Thương hiệu mới: ", val -> setThuongHieu(val));
        nhapCoRangBuoc(sc, "Xuất xứ mới: ", val -> setXuatXu(val));
        nhapCoRangBuoc(sc, "Mã nhà cung cấp mới: ", val -> setMaNCC(val));
        nhapCoRangBuoc(sc, "Mô tả mới: ", val -> setMoTa(val));
    }

    public void xuatThongTin() {
        System.out.println("Mã sản phẩm: " + maSanPham);
        System.out.println("Tên sản phẩm: " + tenSanPham);
        System.out.println("Đơn vị: " + donVi);
        System.out.printf("Giá nhập: %,.0f VNĐ%n", giaNhap);
        System.out.printf("Giá bán: %,.0f VNĐ%n", giaBan);
        System.out.printf("Lợi nhuận: %,.0f VNĐ/%s%n", tinhLoiNhuan(), donVi);
        System.out.println("Tồn kho: " + soLuongTon);
        System.out.println("Loại sản phẩm: " + loaiSanPham);
        System.out.println("Thương hiệu: " + thuongHieu);
        System.out.println("Xuất xứ: " + xuatXu);
        System.out.println("Mã NCC: " + maNCC);
        System.out.println("Mô tả: " + (moTa == null || moTa.isEmpty() ? "(không có)" : moTa));
    }

    public void luuVaoSQL() {
        String sql = """
                INSERT INTO SanPham
                (maSanPham, tenSanPham, donVi, giaNhap, giaBan, soLuongTon,
                 loaiSanPham, thuongHieu, xuatXu, maNCC, moTa)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maSanPham);
            ps.setString(2, tenSanPham);
            ps.setString(3, donVi);
            ps.setDouble(4, giaNhap);
            ps.setDouble(5, giaBan);
            ps.setInt(6, soLuongTon);
            ps.setString(7, loaiSanPham);
            ps.setString(8, thuongHieu);
            ps.setString(9, xuatXu);
            ps.setString(10, maNCC);
            ps.setString(11, moTa);

            ps.executeUpdate();
            System.out.println("Lưu sản phẩm thành công! Mã: " + maSanPham);

        } catch (Exception e) {
            System.out.println("Lưu sản phẩm thất bại!");
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    public static List<SanPham> docTuSQL() {
        List<SanPham> ds = new ArrayList<>();

        String sql = """
                SELECT maSanPham, tenSanPham, donVi, giaNhap, giaBan, soLuongTon,
                       loaiSanPham, thuongHieu, xuatXu, maNCC, moTa
                FROM SanPham
                ORDER BY maSanPham
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                SanPham sp = new SanPham(
                        rs.getString("maSanPham"),
                        rs.getString("tenSanPham"),
                        rs.getString("donVi"),
                        rs.getDouble("giaNhap"),
                        rs.getDouble("giaBan"),
                        rs.getInt("soLuongTon"),
                        rs.getString("loaiSanPham"),
                        rs.getString("thuongHieu"),
                        rs.getString("xuatXu"),
                        rs.getString("maNCC"),
                        rs.getString("moTa")
                );

                ds.add(sp);
            }

        } catch (Exception e) {
            System.out.println("Đọc danh sách sản phẩm thất bại!");
            System.out.println("Lỗi: " + e.getMessage());
        }

        return ds;
    }

    public void capNhatSQL() {
        String sql = """
                UPDATE SanPham
                SET tenSanPham = ?, donVi = ?, giaNhap = ?, giaBan = ?, soLuongTon = ?,
                    loaiSanPham = ?, thuongHieu = ?, xuatXu = ?, maNCC = ?, moTa = ?
                WHERE maSanPham = ?
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tenSanPham);
            ps.setString(2, donVi);
            ps.setDouble(3, giaNhap);
            ps.setDouble(4, giaBan);
            ps.setInt(5, soLuongTon);
            ps.setString(6, loaiSanPham);
            ps.setString(7, thuongHieu);
            ps.setString(8, xuatXu);
            ps.setString(9, maNCC);
            ps.setString(10, moTa);
            ps.setString(11, maSanPham);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Cập nhật sản phẩm thành công! Mã: " + maSanPham);
            } else {
                System.out.println("Không tìm thấy sản phẩm có mã: " + maSanPham);
            }

        } catch (Exception e) {
            System.out.println("Cập nhật sản phẩm thất bại!");
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    public void capNhatSoLuongSQL() {
        String sql = "UPDATE SanPham SET soLuongTon = ? WHERE maSanPham = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, soLuongTon);
            ps.setString(2, maSanPham);
            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println("Cập nhật số lượng tồn thất bại!");
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    public static void xoaKhoiSQL(String maSanPham) {
        String sql = "DELETE FROM SanPham WHERE maSanPham = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maSanPham);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Xóa sản phẩm thành công! Mã: " + maSanPham);
            } else {
                System.out.println("Không tìm thấy sản phẩm có mã: " + maSanPham);
            }

        } catch (Exception e) {
            System.out.println("Xóa sản phẩm thất bại!");
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    public static List<SanPham> timKiemSQL(String keyword) {
        List<SanPham> ds = new ArrayList<>();

        String sql = """
                SELECT maSanPham, tenSanPham, donVi, giaNhap, giaBan, soLuongTon,
                       loaiSanPham, thuongHieu, xuatXu, maNCC, moTa
                FROM SanPham
                WHERE maSanPham LIKE ?
                   OR tenSanPham LIKE ?
                   OR loaiSanPham LIKE ?
                   OR thuongHieu LIKE ?
                   OR xuatXu LIKE ?
                ORDER BY maSanPham
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String key = "%" + keyword + "%";

            for (int i = 1; i <= 5; i++) {
                ps.setString(i, key);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SanPham sp = new SanPham(
                            rs.getString("maSanPham"),
                            rs.getString("tenSanPham"),
                            rs.getString("donVi"),
                            rs.getDouble("giaNhap"),
                            rs.getDouble("giaBan"),
                            rs.getInt("soLuongTon"),
                            rs.getString("loaiSanPham"),
                            rs.getString("thuongHieu"),
                            rs.getString("xuatXu"),
                            rs.getString("maNCC"),
                            rs.getString("moTa")
                    );

                    ds.add(sp);
                }
            }

        } catch (Exception e) {
            System.out.println("Tìm kiếm sản phẩm thất bại!");
            System.out.println("Lỗi: " + e.getMessage());
        }

        return ds;
    }

    public static List<SanPham> timHangSapHetSQL(int nguongCanhBao) {
        List<SanPham> ds = new ArrayList<>();

        String sql = """
                SELECT maSanPham, tenSanPham, donVi, giaNhap, giaBan, soLuongTon,
                       loaiSanPham, thuongHieu, xuatXu, maNCC, moTa
                FROM SanPham
                WHERE soLuongTon <= ?
                ORDER BY soLuongTon ASC
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, nguongCanhBao);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SanPham sp = new SanPham(
                            rs.getString("maSanPham"),
                            rs.getString("tenSanPham"),
                            rs.getString("donVi"),
                            rs.getDouble("giaNhap"),
                            rs.getDouble("giaBan"),
                            rs.getInt("soLuongTon"),
                            rs.getString("loaiSanPham"),
                            rs.getString("thuongHieu"),
                            rs.getString("xuatXu"),
                            rs.getString("maNCC"),
                            rs.getString("moTa")
                    );

                    ds.add(sp);
                }
            }

        } catch (Exception e) {
            System.out.println("Tìm sản phẩm sắp hết thất bại!");
            System.out.println("Lỗi: " + e.getMessage());
        }

        return ds;
    }

    public static void hienThiDanhSach(List<SanPham> ds) {
        if (ds == null || ds.isEmpty()) {
            System.out.println("Danh sách sản phẩm rỗng.");
            return;
        }

        for (SanPham sp : ds) {
            System.out.println("--------------------");
            sp.xuatThongTin();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof SanPham)) return false;

        SanPham other = (SanPham) obj;
        return maSanPham != null && maSanPham.equals(other.maSanPham);
    }

    @Override
    public int hashCode() {
        return maSanPham != null ? maSanPham.hashCode() : 0;
    }

    private void nhapCoRangBuoc(Scanner sc, String label, Consumer<String> setter) {
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