package model;

import exception.ValidationException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class NhanVien extends Nguoi {

    private String email;
    private String cccd;
    private LocalDate ngaySinh;
    private String chucVu;
    private double luong;
    private LocalTime gioVaoLam;
    private LocalTime gioKetThuc;

    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final DateTimeFormatter TIME_FMT =
            DateTimeFormatter.ofPattern("HH:mm");

    public NhanVien() {
        super();
    }

    public NhanVien(String maDinhDanh, String hoTen,
                    String soDienThoai, String diaChi,
                    String ngaySinh, String email, String cccd,
                    String chucVu, double luong,
                    String gioVaoLam, String gioKetThuc)
            throws ValidationException {

        super(maDinhDanh, hoTen, soDienThoai, diaChi);
        setNgaySinh(ngaySinh);
        setEmail(email);
        setCccd(cccd);
        setChucVu(chucVu);
        setLuong(luong);
        setGioVaoLam(gioVaoLam);
        setGioKetThuc(gioKetThuc);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws ValidationException {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("Email không được để trống.");
        }

        email = email.trim().toLowerCase();

        if (email.length() > 150) {
            throw new ValidationException("Email không được vượt quá 150 ký tự.");
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new ValidationException("Email không hợp lệ.");
        }

        this.email = email;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) throws ValidationException {
        if (cccd == null || cccd.trim().isEmpty()) {
            throw new ValidationException("CCCD không được để trống.");
        }

        cccd = cccd.trim();

        if (!cccd.matches("\\d{12}")) {
            throw new ValidationException("CCCD phải gồm đúng 12 chữ số.");
        }

        this.cccd = cccd;
    }

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public String getNgaySinhText() {
        return ngaySinh != null ? ngaySinh.format(DATE_FMT) : "";
    }

    public void setNgaySinh(String ngaySinh) throws ValidationException {
        if (ngaySinh == null || ngaySinh.trim().isEmpty()) {
            throw new ValidationException("Ngày sinh không được để trống.");
        }

        try {
            LocalDate date = LocalDate.parse(ngaySinh.trim(), DATE_FMT);
            setNgaySinh(date);
        } catch (DateTimeParseException e) {
            throw new ValidationException("Ngày sinh phải đúng định dạng dd/MM/yyyy.");
        }
    }

    public void setNgaySinh(LocalDate ngaySinh) throws ValidationException {
        if (ngaySinh == null) {
            throw new ValidationException("Ngày sinh không được để trống.");
        }

        if (ngaySinh.isAfter(LocalDate.now())) {
            throw new ValidationException("Ngày sinh không được ở tương lai.");
        }

        if (Period.between(ngaySinh, LocalDate.now()).getYears() < 18) {
            throw new ValidationException("Nhân viên phải đủ 18 tuổi.");
        }

        this.ngaySinh = ngaySinh;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) throws ValidationException {
        if (chucVu == null || chucVu.trim().isEmpty()) {
            throw new ValidationException("Chức vụ không được để trống.");
        }

        chucVu = chucVu.trim();

        if (chucVu.length() > 100) {
            throw new ValidationException("Chức vụ không được vượt quá 100 ký tự.");
        }

        this.chucVu = chucVu;
    }

    public double getLuong() {
        return luong;
    }

    public void setLuong(double luong) throws ValidationException {
        if (luong < 2_000_000) {
            throw new ValidationException("Lương tối thiểu là 2.000.000 VNĐ.");
        }

        this.luong = luong;
    }

    public LocalTime getGioVaoLam() {
        return gioVaoLam;
    }

    public String getGioVaoLamText() {
        return gioVaoLam != null ? gioVaoLam.format(TIME_FMT) : "";
    }

    public void setGioVaoLam(String gioVaoLam) throws ValidationException {
        if (gioVaoLam == null || gioVaoLam.trim().isEmpty()) {
            throw new ValidationException("Giờ vào làm không được để trống.");
        }

        try {
            this.gioVaoLam = LocalTime.parse(gioVaoLam.trim(), TIME_FMT);

            if (gioKetThuc != null && !gioKetThuc.isAfter(this.gioVaoLam)) {
                throw new ValidationException("Giờ kết thúc phải sau giờ vào làm.");
            }

        } catch (DateTimeParseException e) {
            throw new ValidationException("Giờ vào làm phải đúng định dạng HH:mm.");
        }
    }

    public LocalTime getGioKetThuc() {
        return gioKetThuc;
    }

    public String getGioKetThucText() {
        return gioKetThuc != null ? gioKetThuc.format(TIME_FMT) : "";
    }

    public void setGioKetThuc(String gioKetThuc) throws ValidationException {
        if (gioKetThuc == null || gioKetThuc.trim().isEmpty()) {
            throw new ValidationException("Giờ kết thúc không được để trống.");
        }

        try {
            LocalTime time = LocalTime.parse(gioKetThuc.trim(), TIME_FMT);

            if (gioVaoLam != null && !time.isAfter(gioVaoLam)) {
                throw new ValidationException("Giờ kết thúc phải sau giờ vào làm.");
            }

            this.gioKetThuc = time;

        } catch (DateTimeParseException e) {
            throw new ValidationException("Giờ kết thúc phải đúng định dạng HH:mm.");
        }
    }

    @Override
    public void nhapThongTin() {
    }

    @Override
    public void suaThongTin() {
    }

    @Override
    public void xuatThongTin() {
        System.out.println("===== THÔNG TIN NHÂN VIÊN =====");
        System.out.println("Mã NV        : " + getMaDinhDanh());
        System.out.println("Họ tên       : " + getHoTen());
        System.out.println("SĐT          : " + getSoDienThoai());
        System.out.println("Địa chỉ      : " + getDiaChi());
        System.out.println("Ngày sinh    : " + getNgaySinhText());
        System.out.println("Email        : " + email);
        System.out.println("CCCD         : " + cccd);
        System.out.println("Chức vụ      : " + chucVu);
        System.out.printf("Lương        : %,.0f VNĐ%n", luong);
        System.out.println("Giờ vào làm  : " + getGioVaoLamText());
        System.out.println("Giờ kết thúc : " + getGioKetThucText());
    }

    @Override
    public void luuVaoSQL() throws Exception {
    }
}