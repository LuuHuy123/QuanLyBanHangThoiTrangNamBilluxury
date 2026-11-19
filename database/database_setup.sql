
-- =========================
-- NHÀ CUNG CẤP
-- =========================
CREATE TABLE NhaCungCap (
    maNCC NVARCHAR(20) PRIMARY KEY,
    tenCongTy NVARCHAR(200) NOT NULL,
    soDienThoai NVARCHAR(15) NOT NULL UNIQUE,
    email NVARCHAR(150) NOT NULL UNIQUE,
    diaChi NVARCHAR(200) NOT NULL,
    nguoiLienHe NVARCHAR(100) NOT NULL,
    moTa NVARCHAR(500)
);
GO

-- =========================
-- NHÂN VIÊN
-- =========================
CREATE TABLE NhanVien (
    maDinhDanh NVARCHAR(20) PRIMARY KEY,
    hoTen NVARCHAR(100) NOT NULL,
    soDienThoai NVARCHAR(15) NOT NULL UNIQUE,
    diaChi NVARCHAR(200) NOT NULL,
    ngaySinh NVARCHAR(12) NOT NULL,
    email NVARCHAR(150) NOT NULL UNIQUE,
    cccd NVARCHAR(12) NOT NULL UNIQUE,
    chucVu NVARCHAR(100) NOT NULL,
    luong FLOAT NOT NULL CHECK (luong >= 2000000),
    gioVaoLam TIME NOT NULL,
    gioKetThuc TIME NOT NULL,
    CONSTRAINT chk_gio_nv CHECK (gioKetThuc > gioVaoLam)
);
GO

-- =========================
-- KHÁCH HÀNG
-- =========================
CREATE TABLE KhachHang (
    maDinhDanh NVARCHAR(20) PRIMARY KEY,
    hoTen NVARCHAR(100) NOT NULL,
    soDienThoai NVARCHAR(15) NOT NULL UNIQUE,
    diaChi NVARCHAR(200) NOT NULL,
    diemTichLuy INT NOT NULL DEFAULT 0 CHECK (diemTichLuy >= 0)
);
GO

-- =========================
-- SẢN PHẨM
-- =========================
CREATE TABLE SanPham (
    maSanPham NVARCHAR(20) PRIMARY KEY,
    tenSanPham NVARCHAR(200) NOT NULL,
    donVi NVARCHAR(50) NOT NULL DEFAULT N'cái',
    giaNhap FLOAT NOT NULL CHECK (giaNhap >= 0),
    giaBan FLOAT NOT NULL CHECK (giaBan >= 0),
    soLuongTon INT NOT NULL DEFAULT 0 CHECK (soLuongTon >= 0),
    loaiSanPham NVARCHAR(100) NOT NULL,
    thuongHieu NVARCHAR(100) NOT NULL,
    xuatXu NVARCHAR(100) NOT NULL,
    maNCC NVARCHAR(20) NOT NULL,
    moTa NVARCHAR(500),
    CONSTRAINT fk_sp_ncc FOREIGN KEY (maNCC) REFERENCES NhaCungCap(maNCC),
    CONSTRAINT chk_gia_sp CHECK (giaBan >= giaNhap)
);
GO

-- =========================
-- HÓA ĐƠN
-- =========================
CREATE TABLE HoaDon (
    maHoaDon NVARCHAR(20) PRIMARY KEY,
    maKhachHang NVARCHAR(20) NOT NULL,
    maNhanVien NVARCHAR(20) NOT NULL,
    ngayLap DATETIME2 NOT NULL,
    tongTien FLOAT NOT NULL DEFAULT 0 CHECK (tongTien >= 0),
    giamGia FLOAT NOT NULL DEFAULT 0 CHECK (giamGia >= 0),
    thanhToan FLOAT NOT NULL DEFAULT 0 CHECK (thanhToan >= 0),
    trangThai NVARCHAR(30) NOT NULL DEFAULT N'Chờ thanh toán',
    ghiChu NVARCHAR(500),

    CONSTRAINT fk_hd_kh FOREIGN KEY (maKhachHang)
        REFERENCES KhachHang(maDinhDanh),

    CONSTRAINT fk_hd_nv FOREIGN KEY (maNhanVien)
        REFERENCES NhanVien(maDinhDanh),

    CONSTRAINT chk_hd_trangthai CHECK (
        trangThai IN (N'Chờ thanh toán', N'Đã thanh toán', N'Đã hủy')
    )
);
GO

-- =========================
-- CHI TIẾT HÓA ĐƠN
-- =========================
CREATE TABLE ChiTietHoaDon (
    maHoaDon NVARCHAR(20) NOT NULL,
    maSanPham NVARCHAR(20) NOT NULL,
    soLuong INT NOT NULL CHECK (soLuong > 0),
    donGia FLOAT NOT NULL CHECK (donGia > 0),

    PRIMARY KEY (maHoaDon, maSanPham),

    CONSTRAINT fk_cthd_hd FOREIGN KEY (maHoaDon)
        REFERENCES HoaDon(maHoaDon)
        ON DELETE CASCADE,

    CONSTRAINT fk_cthd_sp FOREIGN KEY (maSanPham)
        REFERENCES SanPham(maSanPham)
);
GO


