package Human;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import database.DBConnection;

public class NhanVien extends Nguoi {

    private LocalDate ngaySinh;
    private String email;
    private String cccd;
    private String chucVu;
    private double luong;
    private LocalTime gioVaoLam;
    private LocalTime gioKetThuc;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    protected String layPrefix() {
        return "NV";
    }

    public NhanVien() {
        super();
    }

    public NhanVien(String hoTen, String soDienThoai, String diaChi,
                    String ngaySinh, String email, String cccd,
                    String chucVu, double luong,
                    String gioVaoLam, String gioKetThuc) {
        super(hoTen, soDienThoai, diaChi);
        setNgaySinh(ngaySinh);
        setEmail(email);
        setCccd(cccd);
        setChucVu(chucVu);
        setLuong(luong);
        setGioVaoLam(gioVaoLam);
        setGioKetThuc(gioKetThuc);
    }

    public NhanVien(String maDinhDanh, String hoTen, String soDienThoai, String diaChi,
                    String ngaySinh, String email, String cccd,
                    String chucVu, double luong,
                    String gioVaoLam, String gioKetThuc) {
        super(maDinhDanh, hoTen, soDienThoai, diaChi);
        setNgaySinh(ngaySinh);
        setEmail(email);
        setCccd(cccd);
        setChucVu(chucVu);
        setLuong(luong);
        setGioVaoLam(gioVaoLam);
        setGioKetThuc(gioKetThuc);
    }

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public String getNgaySinhText() {
        return ngaySinh != null ? ngaySinh.format(DATE_FMT) : "";
    }

    public String getEmail() {
        return email;
    }

    public String getCccd() {
        return cccd;
    }

    public String getChucVu() {
        return chucVu;
    }

    public double getLuong() {
        return luong;
    }

    public LocalTime getGioVaoLam() {
        return gioVaoLam;
    }

    public LocalTime getGioKetThuc() {
        return gioKetThuc;
    }

    public String getGioVaoLamText() {
        return gioVaoLam != null ? gioVaoLam.format(TIME_FMT) : "";
    }

    public String getGioKetThucText() {
        return gioKetThuc != null ? gioKetThuc.format(TIME_FMT) : "";
    }

    public void setNgaySinh(String str) {
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException("Ngày sinh không được để trống.");
        }

        try {
            LocalDate d = LocalDate.parse(str.trim(), DATE_FMT);

            if (d.isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("Ngày sinh không được ở tương lai.");
            }

            if (Period.between(d, LocalDate.now()).getYears() < 18) {
                throw new IllegalArgumentException("Nhân viên phải đủ 18 tuổi.");
            }

            this.ngaySinh = d;

        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Ngày sinh phải đúng định dạng dd/MM/yyyy.");
        }
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email không được để trống.");
        }

        email = email.trim().toLowerCase();

        if (email.length() > 150) {
            throw new IllegalArgumentException("Email không được vượt quá 150 ký tự.");
        }

        if (!email.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("Email không hợp lệ.");
        }

        this.email = email;
    }

    public void setCccd(String cccd) {
        if (cccd == null || cccd.trim().isEmpty()) {
            throw new IllegalArgumentException("CCCD không được để trống.");
        }

        cccd = cccd.trim();

        if (!cccd.matches("\\d{12}")) {
            throw new IllegalArgumentException("CCCD phải gồm đúng 12 chữ số.");
        }

        this.cccd = cccd;
    }

    public void setChucVu(String chucVu) {
        if (chucVu == null || chucVu.trim().isEmpty()) {
            throw new IllegalArgumentException("Chức vụ không được để trống.");
        }

        chucVu = chucVu.trim();

        if (chucVu.length() > 100) {
            throw new IllegalArgumentException("Chức vụ không được vượt quá 100 ký tự.");
        }

        this.chucVu = chucVu;
    }

    public void setLuong(double luong) {
        if (luong < 2_000_000) {
            throw new IllegalArgumentException("Lương phải lớn hơn hoặc bằng 2,000,000 VNĐ.");
        }

        this.luong = luong;
    }

    public void setGioVaoLam(String str) {
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException("Giờ vào làm không được để trống.");
        }

        try {
            this.gioVaoLam = LocalTime.parse(str.trim(), TIME_FMT);

            if (gioKetThuc != null && !gioKetThuc.isAfter(this.gioVaoLam)) {
                throw new IllegalArgumentException("Giờ kết thúc phải sau giờ vào làm.");
            }

        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Giờ vào làm phải đúng định dạng HH:mm.");
        }
    }

    public void setGioKetThuc(String str) {
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException("Giờ kết thúc không được để trống.");
        }

        try {
            LocalTime kt = LocalTime.parse(str.trim(), TIME_FMT);

            if (gioVaoLam != null && !kt.isAfter(gioVaoLam)) {
                throw new IllegalArgumentException("Giờ kết thúc phải sau giờ vào làm.");
            }

            this.gioKetThuc = kt;

        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Giờ kết thúc phải đúng định dạng HH:mm.");
        }
    }

    @Override
    public void nhapThongTin() {
        Scanner sc = new Scanner(System.in);

        System.out.println(">>> Mã nhân viên được tự động tạo: " + maDinhDanh);

        nhapCoRangBuoc(sc, "Họ tên: ", val -> setHoTen(val));
        nhapCoRangBuoc(sc, "Số điện thoại: ", val -> setSoDienThoai(val));
        nhapCoRangBuoc(sc, "Địa chỉ: ", val -> setDiaChi(val));
        nhapCoRangBuoc(sc, "Ngày sinh (dd/MM/yyyy): ", val -> setNgaySinh(val));
        nhapCoRangBuoc(sc, "Email: ", val -> setEmail(val));
        nhapCoRangBuoc(sc, "CCCD: ", val -> setCccd(val));
        nhapCoRangBuoc(sc, "Chức vụ: ", val -> setChucVu(val));

        while (true) {
            System.out.print("Lương: ");
            try {
                setLuong(Double.parseDouble(sc.nextLine().trim()));
                break;
            } catch (Exception e) {
                System.out.println("[Lỗi] Lương phải là số và >= 2,000,000.");
            }
        }

        nhapCoRangBuoc(sc, "Giờ vào làm (HH:mm): ", val -> setGioVaoLam(val));
        nhapCoRangBuoc(sc, "Giờ kết thúc (HH:mm): ", val -> setGioKetThuc(val));
    }

    @Override
    public void suaThongTin() {
        Scanner sc = new Scanner(System.in);

        nhapCoRangBuoc(sc, "Họ tên mới: ", val -> setHoTen(val));
        nhapCoRangBuoc(sc, "Số điện thoại mới: ", val -> setSoDienThoai(val));
        nhapCoRangBuoc(sc, "Địa chỉ mới: ", val -> setDiaChi(val));
        nhapCoRangBuoc(sc, "Ngày sinh mới (dd/MM/yyyy): ", val -> setNgaySinh(val));
        nhapCoRangBuoc(sc, "Email mới: ", val -> setEmail(val));
        nhapCoRangBuoc(sc, "CCCD mới: ", val -> setCccd(val));
        nhapCoRangBuoc(sc, "Chức vụ mới: ", val -> setChucVu(val));

        while (true) {
            System.out.print("Lương mới: ");
            try {
                setLuong(Double.parseDouble(sc.nextLine().trim()));
                break;
            } catch (Exception e) {
                System.out.println("[Lỗi] Lương phải là số và >= 2,000,000.");
            }
        }

        nhapCoRangBuoc(sc, "Giờ vào làm mới (HH:mm): ", val -> setGioVaoLam(val));
        nhapCoRangBuoc(sc, "Giờ kết thúc mới (HH:mm): ", val -> setGioKetThuc(val));
    }

    @Override
    public void xuatThongTin() {
        super.xuatThongTin();
        System.out.println("Ngày sinh: " + getNgaySinhText());
        System.out.println("Email: " + email);
        System.out.println("CCCD: " + cccd);
        System.out.println("Chức vụ: " + chucVu);
        System.out.printf("Lương: %,.0f VNĐ%n", luong);
        System.out.println("Ca làm: " + getGioVaoLamText() + " - " + getGioKetThucText());
    }

    public boolean dangLamViec() {
        if (gioVaoLam == null || gioKetThuc == null) return false;

        LocalTime now = LocalTime.now();
        return !now.isBefore(gioVaoLam) && !now.isAfter(gioKetThuc);
    }

    @Override
    public void luuVaoSQL() {
        String sql = """
                INSERT INTO NhanVien
                (maDinhDanh, hoTen, soDienThoai, diaChi, ngaySinh, email, cccd, chucVu, luong, gioVaoLam, gioKetThuc)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maDinhDanh);
            ps.setString(2, hoTen);
            ps.setString(3, soDienThoai);
            ps.setString(4, diaChi);
            ps.setString(5, getNgaySinhText());
            ps.setString(6, email);
            ps.setString(7, cccd);
            ps.setString(8, chucVu);
            ps.setDouble(9, luong);
            ps.setTime(10, java.sql.Time.valueOf(gioVaoLam));
            ps.setTime(11, java.sql.Time.valueOf(gioKetThuc));

            ps.executeUpdate();
            System.out.println("Lưu nhân viên thành công! Mã: " + maDinhDanh);

        } catch (Exception e) {
            System.out.println("Lưu nhân viên thất bại!");
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    public static List<NhanVien> docTuSQL() {
        List<NhanVien> ds = new ArrayList<>();

        String sql = """
                SELECT maDinhDanh, hoTen, soDienThoai, diaChi, ngaySinh, email, cccd,
                       chucVu, luong, gioVaoLam, gioKetThuc
                FROM NhanVien
                ORDER BY maDinhDanh
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                NhanVien nv = new NhanVien(
                        rs.getString("maDinhDanh"),
                        rs.getString("hoTen"),
                        rs.getString("soDienThoai"),
                        rs.getString("diaChi"),
                        rs.getString("ngaySinh"),
                        rs.getString("email"),
                        rs.getString("cccd"),
                        rs.getString("chucVu"),
                        rs.getDouble("luong"),
                        rs.getTime("gioVaoLam").toLocalTime().format(TIME_FMT),
                        rs.getTime("gioKetThuc").toLocalTime().format(TIME_FMT)
                );

                ds.add(nv);
            }

        } catch (Exception e) {
            System.out.println("Đọc danh sách nhân viên thất bại!");
            System.out.println("Lỗi: " + e.getMessage());
        }

        return ds;
    }

    public void capNhatSQL() {
        String sql = """
                UPDATE NhanVien
                SET hoTen = ?, soDienThoai = ?, diaChi = ?, ngaySinh = ?, email = ?,
                    cccd = ?, chucVu = ?, luong = ?, gioVaoLam = ?, gioKetThuc = ?
                WHERE maDinhDanh = ?
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, hoTen);
            ps.setString(2, soDienThoai);
            ps.setString(3, diaChi);
            ps.setString(4, getNgaySinhText());
            ps.setString(5, email);
            ps.setString(6, cccd);
            ps.setString(7, chucVu);
            ps.setDouble(8, luong);
            ps.setTime(9, java.sql.Time.valueOf(gioVaoLam));
            ps.setTime(10, java.sql.Time.valueOf(gioKetThuc));
            ps.setString(11, maDinhDanh);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Cập nhật nhân viên thành công! Mã: " + maDinhDanh);
            } else {
                System.out.println("Không tìm thấy nhân viên có mã: " + maDinhDanh);
            }

        } catch (Exception e) {
            System.out.println("Cập nhật nhân viên thất bại!");
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    public static void xoaKhoiSQL(String maDinhDanh) {
        String sql = "DELETE FROM NhanVien WHERE maDinhDanh = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maDinhDanh);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Xóa nhân viên thành công! Mã: " + maDinhDanh);
            } else {
                System.out.println("Không tìm thấy nhân viên có mã: " + maDinhDanh);
            }

        } catch (Exception e) {
            System.out.println("Xóa nhân viên thất bại!");
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    public static List<NhanVien> timKiemSQL(String keyword) {
        List<NhanVien> ds = new ArrayList<>();

        String sql = """
                SELECT maDinhDanh, hoTen, soDienThoai, diaChi, ngaySinh, email, cccd,
                       chucVu, luong, gioVaoLam, gioKetThuc
                FROM NhanVien
                WHERE maDinhDanh LIKE ?
                   OR hoTen LIKE ?
                   OR soDienThoai LIKE ?
                   OR email LIKE ?
                   OR cccd LIKE ?
                   OR chucVu LIKE ?
                ORDER BY maDinhDanh
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String key = "%" + keyword + "%";

            for (int i = 1; i <= 6; i++) {
                ps.setString(i, key);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    NhanVien nv = new NhanVien(
                            rs.getString("maDinhDanh"),
                            rs.getString("hoTen"),
                            rs.getString("soDienThoai"),
                            rs.getString("diaChi"),
                            rs.getString("ngaySinh"),
                            rs.getString("email"),
                            rs.getString("cccd"),
                            rs.getString("chucVu"),
                            rs.getDouble("luong"),
                            rs.getTime("gioVaoLam").toLocalTime().format(TIME_FMT),
                            rs.getTime("gioKetThuc").toLocalTime().format(TIME_FMT)
                    );

                    ds.add(nv);
                }
            }

        } catch (Exception e) {
            System.out.println("Tìm kiếm nhân viên thất bại!");
            System.out.println("Lỗi: " + e.getMessage());
        }

        return ds;
    }

    public static void hienThiDanhSach(List<NhanVien> ds) {
        if (ds == null || ds.isEmpty()) {
            System.out.println("Danh sách nhân viên rỗng.");
            return;
        }

        for (NhanVien nv : ds) {
            System.out.println("--------------------");
            nv.xuatThongTin();
        }
    }

    private void nhapCoRangBuoc(Scanner sc, String label, java.util.function.Consumer<String> setter) {
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