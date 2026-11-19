package model;

import exception.ValidationException;

public class KhachHang extends Nguoi {

    private int diemTichLuy;

    public KhachHang() {
        super();
        this.diemTichLuy = 0;
    }

    public KhachHang(String maDinhDanh, String hoTen,
                      String soDienThoai, String diaChi,
                      int diemTichLuy) throws ValidationException {

        super(maDinhDanh, hoTen, soDienThoai, diaChi);

        setDiemTichLuy(diemTichLuy);
    }

    public int getDiemTichLuy() {
        return diemTichLuy;
    }

    public void setDiemTichLuy(int diemTichLuy) throws ValidationException {

        if (diemTichLuy < 0) {
            throw new ValidationException(
                    "Điểm tích lũy không được âm.");
        }

        this.diemTichLuy = diemTichLuy;
    }

    public void congDiem(int diemCong) throws ValidationException {

        if (diemCong <= 0) {
            throw new ValidationException(
                    "Điểm cộng phải lớn hơn 0.");
        }

        this.diemTichLuy += diemCong;
    }

    public void truDiem(int diemTru) throws ValidationException {

        if (diemTru <= 0) {
            throw new ValidationException(
                    "Điểm trừ phải lớn hơn 0.");
        }

        if (diemTru > diemTichLuy) {
            throw new ValidationException(
                    "Điểm trừ vượt quá điểm hiện có.");
        }

        this.diemTichLuy -= diemTru;
    }

    @Override
    public void nhapThongTin() {

    }

    @Override
    public void suaThongTin() {

    }

    @Override
    public void xuatThongTin() {

        System.out.println("===== THÔNG TIN KHÁCH HÀNG =====");

        System.out.println("Mã khách hàng : " + getMaDinhDanh());
        System.out.println("Họ tên        : " + getHoTen());
        System.out.println("SĐT           : " + getSoDienThoai());
        System.out.println("Địa chỉ       : " + getDiaChi());
        System.out.println("Điểm tích lũy : " + diemTichLuy);
    }

    @Override
    public void luuVaoSQL() throws Exception {

    }
}