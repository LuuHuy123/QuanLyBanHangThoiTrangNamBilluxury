# 👔 Billuxury - Quản Lý Bán Hàng Thời Trang Nam

Ứng dụng desktop quản lý cửa hàng thời trang nam **Billuxury**, xây dựng bằng **Java Swing** và cơ sở dữ liệu **Microsoft SQL Server**.

## ✨ Chức năng chính

- 🔐 **Đăng nhập** hệ thống (mật khẩu được mã hóa)
- 👕 **Quản lý sản phẩm**: thêm, sửa, xóa, tìm kiếm, theo dõi tồn kho
- 👥 **Quản lý khách hàng**
- 🧑‍💼 **Quản lý nhân viên**
- 🚚 **Quản lý nhà cung cấp**
- 🧾 **Quản lý hóa đơn** và chi tiết hóa đơn
- ⚠️ Xử lý lỗi riêng: dữ liệu trùng, dữ liệu không hợp lệ, hết hàng trong kho

## 🛠️ Công nghệ sử dụng

| Thành phần | Công nghệ |
|---|---|
| Ngôn ngữ | Java (JDK 11+) |
| Giao diện | Java Swing |
| Cơ sở dữ liệu | Microsoft SQL Server |
| Kết nối CSDL | JDBC (`mssql-jdbc-13.4.0.jre11`) |

## 📁 Cấu trúc thư mục

```
├── src/
│   ├── Main.java          # Điểm khởi chạy ứng dụng
│   ├── GUI/               # Các màn hình giao diện
│   ├── Human/             # Các lớp nghiệp vụ (Khách hàng, Nhân viên, ...)
│   ├── model/             # Các lớp mô hình dữ liệu
│   ├── database/          # Kết nối CSDL (DBConnection)
│   ├── exception/         # Các ngoại lệ tự định nghĩa
│   ├── util/              # Tiện ích (mã hóa mật khẩu)
│   └── image/             # Hình ảnh giao diện
├── lib/                   # Thư viện JDBC SQL Server
├── database/
│   └── database_setup.sql # Script tạo CSDL và dữ liệu mẫu
└── bin/                   # File đã biên dịch
```

## 🗄️ Cơ sở dữ liệu

Gồm 6 bảng: `NhaCungCap`, `NhanVien`, `KhachHang`, `SanPham`, `HoaDon`, `ChiTietHoaDon`.

## 🚀 Hướng dẫn cài đặt và chạy

### 1. Yêu cầu
- JDK 11 trở lên
- Microsoft SQL Server (bật TCP/IP, port `1433`, bật chế độ đăng nhập SQL Server)

### 2. Tạo cơ sở dữ liệu
Mở file `database/database_setup.sql` bằng SQL Server Management Studio và chạy để tạo database `BiluxuryDB`.

### 3. Cấu hình kết nối
Sửa thông tin trong `src/database/DBConnection.java` cho phù hợp với máy của bạn:

```java
private static final String USER = "sa";
private static final String PASSWORD = "your_password";
```

### 4. Biên dịch và chạy

**Windows:**
```bat
mkdir out
javac -encoding UTF-8 -cp "lib/*" -d out src\*.java src\GUI\*.java src\model\*.java src\util\*.java src\database\*.java src\exception\*.java src\Human\*.java
xcopy src\image out\image /E /I
java -cp "out;lib/*" Main
```

**macOS / Linux:**
```bash
mkdir -p out
javac -encoding UTF-8 -cp "lib/*" -d out $(find src -name "*.java")
cp -r src/image out/image
java -cp "out:lib/*" Main
```

## ☁️ Chạy trực tiếp trên GitHub (Codespaces)

Không cần cài JDK hay SQL Server trên máy. Chỉ cần trình duyệt và tài khoản GitHub.

[![Open in GitHub Codespaces](https://github.com/codespaces/badge.svg)](https://codespaces.new/LuuHuy123/QuanLyBanHangThoiTrangNamBilluxury)

### Yêu cầu trong repo
Repo đã có sẵn cấu hình môi trường trong thư mục `.devcontainer/`:
- `devcontainer.json`: cấu hình Java 17 và desktop ảo
- `docker-compose.yml`: dựng SQL Server và tự chạy `database/database_setup.sql`

Mật khẩu SQL Server trong môi trường này là `Billuxury@123`, đã khai báo trong `src/database/DBConnection.java`.

### Các bước chạy

**Bước 1. Mở Codespace**
Bấm nút **Open in GitHub Codespaces** ở trên, hoặc vào repo → **Code** → tab **Codespaces** → **Create codespace on main**. Đợi 3 đến 5 phút để môi trường dựng xong.

**Bước 2. Biên dịch** (mở Terminal bằng `Ctrl + \``)
```bash
mkdir -p out
javac -encoding UTF-8 -cp "lib/*" -d out $(find src -name "*.java")
cp -r src/image out/image
```

**Bước 3. Bật desktop ảo**
```bash
sudo /usr/local/share/desktop-init.sh
```
Sau đó mở tab **PORTS**, chuột phải dòng **6080** → **Port Visibility** → **Public**, bấm biểu tượng quả địa cầu để mở, thêm `/vnc.html` vào cuối địa chỉ, bấm **Connect** (mật khẩu nếu được hỏi: `vscode`).

**Bước 4. Chạy ứng dụng**
Giữ nguyên tab desktop ảo, quay lại Terminal của Codespace và chạy:
```bash
DISPLAY=:1 java -cp "out:lib/*" Main
```
Chuyển sang tab desktop ảo, cửa sổ đăng nhập của ứng dụng sẽ hiện ra.

### Lỗi thường gặp
| Lỗi | Cách xử lý |
|---|---|
| Trang desktop báo `HTTP 502` | Chạy lại `sudo /usr/local/share/desktop-init.sh`, đợi 10 giây rồi tải lại trang |
| Báo lỗi kết nối SQL Server | Đợi khoảng 1 phút cho database khởi tạo xong, rồi chạy lại |
| `Can't connect to X11 window server` | Kiểm tra `echo $DISPLAY` phải là `:1`, nếu khác thì đổi lại trong lệnh `java` |
| Sửa code nhưng không đổi | Chạy lại lệnh `javac` ở Bước 2 |

> 💡 Dùng xong nên bấm **Stop codespace** để tiết kiệm giờ miễn phí hàng tháng.

Hoặc mở bằng IntelliJ / Eclipse / VS Code, thêm file `.jar` trong `lib/` vào classpath và chạy `Main.java`.

## 👤 Tác giả

**LuuHuy123**
