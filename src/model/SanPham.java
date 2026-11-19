package model;

import exception.ValidationException;
import exception.KhoHangException;

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

    public SanPham() {
        this.donVi = "cái";
        this.moTa = "";
    }

    public SanPham(String maSanPham, String tenSanPham, String donVi,
                   double giaNhap, double giaBan, int soLuongTon,
                   String loaiSanPham, String thuongHieu,
                   String xuatXu, String maNCC, String moTa)
            throws ValidationException {

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

    public String getMaSanPham() { return maSanPham; }
    public String getTenSanPham() { return tenSanPham; }
    public String getDonVi() { return donVi; }
    public double getGiaNhap() { return giaNhap; }
    public double getGiaBan() { return giaBan; }
    public int getSoLuongTon() { return soLuongTon; }
    public String getLoaiSanPham() { return loaiSanPham; }
    public String getThuongHieu() { return thuongHieu; }
    public String getXuatXu() { return xuatXu; }
    public String getMaNCC() { return maNCC; }
    public String getMoTa() { return moTa; }

    public void setMaSanPham(String maSanPham) throws ValidationException {
        if (maSanPham == null || maSanPham.trim().isEmpty()) {
            throw new ValidationException("Mã sản phẩm không được để trống.");
        }
        this.maSanPham = maSanPham.trim().toUpperCase();
    }

    public void setTenSanPham(String tenSanPham) throws ValidationException {
        if (tenSanPham == null || tenSanPham.trim().isEmpty()) {
            throw new ValidationException("Tên sản phẩm không được để trống.");
        }
        if (tenSanPham.trim().length() < 2 || tenSanPham.trim().length() > 200) {
            throw new ValidationException("Tên sản phẩm phải từ 2 đến 200 ký tự.");
        }
        this.tenSanPham = tenSanPham.trim();
    }

    public void setDonVi(String donVi) throws ValidationException {
        if (donVi == null || donVi.trim().isEmpty()) {
            donVi = "cái";
        }
        if (donVi.trim().length() > 50) {
            throw new ValidationException("Đơn vị không được vượt quá 50 ký tự.");
        }
        this.donVi = donVi.trim();
    }

    public void setGiaNhap(double giaNhap) throws ValidationException {
        if (giaNhap < 0) {
            throw new ValidationException("Giá nhập không được âm.");
        }
        this.giaNhap = giaNhap;
    }

    public void setGiaBan(double giaBan) throws ValidationException {
        if (giaBan < 0) {
            throw new ValidationException("Giá bán không được âm.");
        }
        if (giaBan < giaNhap) {
            throw new ValidationException("Giá bán phải lớn hơn hoặc bằng giá nhập.");
        }
        this.giaBan = giaBan;
    }

    public void setSoLuongTon(int soLuongTon) throws ValidationException {
        if (soLuongTon < 0) {
            throw new ValidationException("Số lượng tồn không được âm.");
        }
        this.soLuongTon = soLuongTon;
    }

    public void setLoaiSanPham(String loaiSanPham) throws ValidationException {
        if (loaiSanPham == null || loaiSanPham.trim().isEmpty()) {
            throw new ValidationException("Loại sản phẩm không được để trống.");
        }
        this.loaiSanPham = loaiSanPham.trim();
    }

    public void setThuongHieu(String thuongHieu) throws ValidationException {
        if (thuongHieu == null || thuongHieu.trim().isEmpty()) {
            throw new ValidationException("Thương hiệu không được để trống.");
        }
        this.thuongHieu = thuongHieu.trim();
    }

    public void setXuatXu(String xuatXu) throws ValidationException {
        if (xuatXu == null || xuatXu.trim().isEmpty()) {
            throw new ValidationException("Xuất xứ không được để trống.");
        }
        this.xuatXu = xuatXu.trim();
    }

    public void setMaNCC(String maNCC) throws ValidationException {
        if (maNCC == null || maNCC.trim().isEmpty()) {
            throw new ValidationException("Mã nhà cung cấp không được để trống.");
        }
        this.maNCC = maNCC.trim().toUpperCase();
    }

    public void setMoTa(String moTa) throws ValidationException {
        if (moTa == null) {
            this.moTa = "";
            return;
        }
        if (moTa.trim().length() > 500) {
            throw new ValidationException("Mô tả không được vượt quá 500 ký tự.");
        }
        this.moTa = moTa.trim();
    }

    public void capNhatTonKho(int soLuong) throws KhoHangException, ValidationException {
        int tonMoi = this.soLuongTon + soLuong;

        if (tonMoi < 0) {
            throw new KhoHangException(this.tenSanPham, this.soLuongTon, Math.abs(soLuong));
        }

        setSoLuongTon(tonMoi);
    }
}