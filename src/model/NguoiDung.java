package model;

import exception.ValidationException;
import java.time.LocalDateTime;

public class NguoiDung {

    public static final String ADMIN = "ADMIN";
    public static final String NHANVIEN = "NHANVIEN";

    private String taiKhoan;
    private String matKhau;
    private String hoTen;
    private String vaiTro;
    private String maNhanVien;
    private boolean trangThai;
    private LocalDateTime ngayTao;

    public NguoiDung() {
        this.trangThai = true;
        this.ngayTao = LocalDateTime.now();
    }

    public NguoiDung(String taiKhoan, String matKhau,
                      String hoTen, String vaiTro,
                      String maNhanVien,
                      boolean trangThai,
                      LocalDateTime ngayTao)
            throws ValidationException {

        setTaiKhoan(taiKhoan);
        setMatKhau(matKhau);
        setHoTen(hoTen);
        setVaiTro(vaiTro);
        setMaNhanVien(maNhanVien);

        this.trangThai = trangThai;
        this.ngayTao = ngayTao;
    }

    public String getTaiKhoan() {
        return taiKhoan;
    }

    public void setTaiKhoan(String taiKhoan)
            throws ValidationException {

        if (taiKhoan == null || taiKhoan.trim().isEmpty()) {
            throw new ValidationException(
                    "Tài khoản không được để trống.");
        }

        taiKhoan = taiKhoan.trim();

        if (taiKhoan.length() < 4 || taiKhoan.length() > 50) {
            throw new ValidationException(
                    "Tài khoản phải từ 4 đến 50 ký tự.");
        }

        if (!taiKhoan.matches("[a-zA-Z0-9_]+")) {
            throw new ValidationException(
                    "Tài khoản chỉ được chứa chữ, số và dấu gạch dưới.");
        }

        this.taiKhoan = taiKhoan;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau)
            throws ValidationException {

        if (matKhau == null || matKhau.trim().isEmpty()) {
            throw new ValidationException(
                    "Mật khẩu không được để trống.");
        }

        if (matKhau.length() < 6) {
            throw new ValidationException(
                    "Mật khẩu phải có ít nhất 6 ký tự.");
        }

        this.matKhau = matKhau;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen)
            throws ValidationException {

        if (hoTen == null || hoTen.trim().isEmpty()) {
            throw new ValidationException(
                    "Họ tên không được để trống.");
        }

        hoTen = hoTen.trim();

        if (hoTen.length() < 2 || hoTen.length() > 100) {
            throw new ValidationException(
                    "Họ tên phải từ 2 đến 100 ký tự.");
        }

        this.hoTen = hoTen;
    }

    public String getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(String vaiTro)
            throws ValidationException {

        if (vaiTro == null || vaiTro.trim().isEmpty()) {
            throw new ValidationException(
                    "Vai trò không được để trống.");
        }

        vaiTro = vaiTro.trim().toUpperCase();

        if (!ADMIN.equals(vaiTro)
                && !NHANVIEN.equals(vaiTro)) {

            throw new ValidationException(
                    "Vai trò chỉ được là ADMIN hoặc NHANVIEN.");
        }

        this.vaiTro = vaiTro;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien)
            throws ValidationException {

        if (NHANVIEN.equals(vaiTro)) {

            if (maNhanVien == null
                    || maNhanVien.trim().isEmpty()) {

                throw new ValidationException(
                        "Nhân viên phải có mã nhân viên.");
            }
        }

        if (maNhanVien != null) {
            maNhanVien = maNhanVien.trim().toUpperCase();
        }

        this.maNhanVien = maNhanVien;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    public LocalDateTime getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(LocalDateTime ngayTao)
            throws ValidationException {

        if (ngayTao == null) {
            throw new ValidationException(
                    "Ngày tạo không được để trống.");
        }

        this.ngayTao = ngayTao;
    }
}