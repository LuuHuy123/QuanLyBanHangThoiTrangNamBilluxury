package model;

import exception.ValidationException;

public abstract class Nguoi {

    protected String maDinhDanh;
    protected String hoTen;
    protected String soDienThoai;
    protected String diaChi;

    public Nguoi() {
    }

    public Nguoi(String maDinhDanh, String hoTen,
                 String soDienThoai, String diaChi) throws ValidationException {
        setMaDinhDanh(maDinhDanh);
        setHoTen(hoTen);
        setSoDienThoai(soDienThoai);
        setDiaChi(diaChi);
    }

    public String getMaDinhDanh() {
        return maDinhDanh;
    }

    public void setMaDinhDanh(String maDinhDanh) throws ValidationException {
        if (maDinhDanh == null || maDinhDanh.trim().isEmpty()) {
            throw new ValidationException("Mã định danh không được để trống.");
        }

        this.maDinhDanh = maDinhDanh.trim().toUpperCase();
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) throws ValidationException {
        if (hoTen == null || hoTen.trim().isEmpty()) {
            throw new ValidationException("Họ tên không được để trống.");
        }

        hoTen = hoTen.trim();

        if (hoTen.length() < 2 || hoTen.length() > 100) {
            throw new ValidationException("Họ tên phải từ 2 đến 100 ký tự.");
        }

        if (!hoTen.matches("[\\p{L}\\s.'-]+")) {
            throw new ValidationException("Họ tên chỉ được chứa chữ cái, khoảng trắng, dấu chấm, dấu nháy hoặc gạch nối.");
        }

        this.hoTen = hoTen;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) throws ValidationException {
        if (soDienThoai == null || soDienThoai.trim().isEmpty()) {
            throw new ValidationException("Số điện thoại không được để trống.");
        }

        soDienThoai = soDienThoai.trim();

        if (!soDienThoai.matches("0[0-9]{9}")) {
            throw new ValidationException("Số điện thoại phải có 10 chữ số và bắt đầu bằng 0.");
        }

        this.soDienThoai = soDienThoai;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) throws ValidationException {
        if (diaChi == null || diaChi.trim().isEmpty()) {
            throw new ValidationException("Địa chỉ không được để trống.");
        }

        diaChi = diaChi.trim();

        if (diaChi.length() > 200) {
            throw new ValidationException("Địa chỉ không được vượt quá 200 ký tự.");
        }

        this.diaChi = diaChi;
    }

    public abstract void nhapThongTin();

    public abstract void suaThongTin();

    public abstract void xuatThongTin();

    public abstract void luuVaoSQL() throws Exception;
}