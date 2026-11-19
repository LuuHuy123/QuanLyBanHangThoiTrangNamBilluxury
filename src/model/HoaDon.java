package model;

import exception.ValidationException;
import java.time.LocalDateTime;

public class HoaDon {

    public static final String CHO_THANH_TOAN = "Chờ thanh toán";
    public static final String DA_THANH_TOAN = "Đã thanh toán";
    public static final String DA_HUY = "Đã hủy";

    private String maHoaDon;
    private String maKhachHang;
    private String maNhanVien;
    private LocalDateTime ngayLap;
    private double tongTien;
    private double giamGia;
    private double thanhToan;
    private String trangThai;
    private String ghiChu;

    public HoaDon() {
        this.trangThai = CHO_THANH_TOAN;
        this.ghiChu = "";
    }

    public HoaDon(String maHoaDon, String maKhachHang, String maNhanVien,
                  LocalDateTime ngayLap, double tongTien, double giamGia,
                  String trangThai, String ghiChu) throws ValidationException {
        setMaHoaDon(maHoaDon);
        setMaKhachHang(maKhachHang);
        setMaNhanVien(maNhanVien);
        setNgayLap(ngayLap);
        setTongTien(tongTien);
        setGiamGia(giamGia);
        tinhThanhToan();
        setTrangThai(trangThai);
        setGhiChu(ghiChu);
    }

    public String getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(String maHoaDon) throws ValidationException {
        if (maHoaDon == null || maHoaDon.trim().isEmpty()) {
            throw new ValidationException("Mã hóa đơn không được để trống.");
        }
        this.maHoaDon = maHoaDon.trim().toUpperCase();
    }

    public String getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(String maKhachHang) throws ValidationException {
        if (maKhachHang == null || maKhachHang.trim().isEmpty()) {
            throw new ValidationException("Mã khách hàng không được để trống.");
        }
        this.maKhachHang = maKhachHang.trim().toUpperCase();
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) throws ValidationException {
        if (maNhanVien == null || maNhanVien.trim().isEmpty()) {
            throw new ValidationException("Mã nhân viên không được để trống.");
        }
        this.maNhanVien = maNhanVien.trim().toUpperCase();
    }

    public LocalDateTime getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(LocalDateTime ngayLap) throws ValidationException {
        if (ngayLap == null) {
            throw new ValidationException("Ngày lập không được để trống.");
        }
        this.ngayLap = ngayLap;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) throws ValidationException {
        if (tongTien < 0) {
            throw new ValidationException("Tổng tiền không được âm.");
        }
        this.tongTien = tongTien;
        tinhThanhToan();
    }

    public double getGiamGia() {
        return giamGia;
    }

    public void setGiamGia(double giamGia) throws ValidationException {
        if (giamGia < 0) {
            throw new ValidationException("Giảm giá không được âm.");
        }
        if (giamGia > tongTien) {
            throw new ValidationException("Giảm giá không được vượt quá tổng tiền.");
        }
        this.giamGia = giamGia;
        tinhThanhToan();
    }

    public double getThanhToan() {
        return thanhToan;
    }

    private void tinhThanhToan() {
        this.thanhToan = Math.max(0, tongTien - giamGia);
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) throws ValidationException {
        if (trangThai == null || trangThai.trim().isEmpty()) {
            trangThai = CHO_THANH_TOAN;
        }

        if (!trangThai.equals(CHO_THANH_TOAN)
                && !trangThai.equals(DA_THANH_TOAN)
                && !trangThai.equals(DA_HUY)) {
            throw new ValidationException("Trạng thái hóa đơn không hợp lệ.");
        }

        this.trangThai = trangThai;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) throws ValidationException {
        if (ghiChu == null) {
            this.ghiChu = "";
            return;
        }

        if (ghiChu.trim().length() > 500) {
            throw new ValidationException("Ghi chú không được vượt quá 500 ký tự.");
        }

        this.ghiChu = ghiChu.trim();
    }
}