package model;

import exception.ValidationException;

public class NhaCungCap {

    private String maNCC;
    private String tenCongTy;
    private String soDienThoai;
    private String email;
    private String diaChi;
    private String nguoiLienHe;
    private String moTa;

    public NhaCungCap() {
        this.moTa = "";
    }

    public NhaCungCap(String maNCC,
                      String tenCongTy,
                      String soDienThoai,
                      String email,
                      String diaChi,
                      String nguoiLienHe,
                      String moTa)
            throws ValidationException {

        setMaNCC(maNCC);
        setTenCongTy(tenCongTy);
        setSoDienThoai(soDienThoai);
        setEmail(email);
        setDiaChi(diaChi);
        setNguoiLienHe(nguoiLienHe);
        setMoTa(moTa);
    }

    public String getMaNCC() {
        return maNCC;
    }

    public void setMaNCC(String maNCC)
            throws ValidationException {

        if (maNCC == null || maNCC.trim().isEmpty()) {
            throw new ValidationException(
                    "Mã nhà cung cấp không được để trống.");
        }

        this.maNCC = maNCC.trim().toUpperCase();
    }

    public String getTenCongTy() {
        return tenCongTy;
    }

    public void setTenCongTy(String tenCongTy)
            throws ValidationException {

        if (tenCongTy == null || tenCongTy.trim().isEmpty()) {
            throw new ValidationException(
                    "Tên công ty không được để trống.");
        }

        tenCongTy = tenCongTy.trim();

        if (tenCongTy.length() < 2 || tenCongTy.length() > 200) {
            throw new ValidationException(
                    "Tên công ty phải từ 2 đến 200 ký tự.");
        }

        this.tenCongTy = tenCongTy;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai)
            throws ValidationException {

        if (soDienThoai == null || soDienThoai.trim().isEmpty()) {
            throw new ValidationException(
                    "Số điện thoại không được để trống.");
        }

        soDienThoai = soDienThoai.trim();

        if (!soDienThoai.matches("0[0-9]{9}")) {
            throw new ValidationException(
                    "Số điện thoại không hợp lệ.");
        }

        this.soDienThoai = soDienThoai;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email)
            throws ValidationException {

        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException(
                    "Email không được để trống.");
        }

        email = email.trim();

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new ValidationException(
                    "Email không hợp lệ.");
        }

        this.email = email.toLowerCase();
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi)
            throws ValidationException {

        if (diaChi == null || diaChi.trim().isEmpty()) {
            throw new ValidationException(
                    "Địa chỉ không được để trống.");
        }

        diaChi = diaChi.trim();

        if (diaChi.length() > 200) {
            throw new ValidationException(
                    "Địa chỉ không được vượt quá 200 ký tự.");
        }

        this.diaChi = diaChi;
    }

    public String getNguoiLienHe() {
        return nguoiLienHe;
    }

    public void setNguoiLienHe(String nguoiLienHe)
            throws ValidationException {

        if (nguoiLienHe == null || nguoiLienHe.trim().isEmpty()) {
            throw new ValidationException(
                    "Người liên hệ không được để trống.");
        }

        nguoiLienHe = nguoiLienHe.trim();

        if (nguoiLienHe.length() < 2 || nguoiLienHe.length() > 100) {
            throw new ValidationException(
                    "Người liên hệ phải từ 2 đến 100 ký tự.");
        }

        this.nguoiLienHe = nguoiLienHe;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa)
            throws ValidationException {

        if (moTa == null) {
            this.moTa = "";
            return;
        }

        moTa = moTa.trim();

        if (moTa.length() > 500) {
            throw new ValidationException(
                    "Mô tả không được vượt quá 500 ký tự.");
        }

        this.moTa = moTa;
    }
}