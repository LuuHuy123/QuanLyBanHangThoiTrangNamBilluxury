package model;

import exception.ValidationException;

public class ChiTietHoaDon {

    private String maHoaDon;
    private String maSanPham;
    private int soLuong;
    private double donGia;

    public ChiTietHoaDon() {
    }

    public ChiTietHoaDon(String maHoaDon, String maSanPham, int soLuong, double donGia)
            throws ValidationException {
        setMaHoaDon(maHoaDon);
        setMaSanPham(maSanPham);
        setSoLuong(soLuong);
        setDonGia(donGia);
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

    public String getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(String maSanPham) throws ValidationException {
        if (maSanPham == null || maSanPham.trim().isEmpty()) {
            throw new ValidationException("Mã sản phẩm không được để trống.");
        }

        this.maSanPham = maSanPham.trim().toUpperCase();
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) throws ValidationException {
        if (soLuong <= 0) {
            throw new ValidationException("Số lượng phải lớn hơn 0.");
        }

        this.soLuong = soLuong;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) throws ValidationException {
        if (donGia <= 0) {
            throw new ValidationException("Đơn giá phải lớn hơn 0.");
        }

        this.donGia = donGia;
    }

    public double getThanhTien() {
        return soLuong * donGia;
    }
}