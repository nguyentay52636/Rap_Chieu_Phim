CREATE DATABASE bookticket;
USE bookticket;
CREATE TABLE KhachHang (
    MaKH INT AUTO_INCREMENT PRIMARY KEY,
    HoTen VARCHAR(100) NOT NULL,
    SDT VARCHAR(15) UNIQUE,
    NgaySinh DATE,
    DiemTichLuy INT DEFAULT 0,
    HangThanhVien VARCHAR(50) DEFAULT 'Thanh vien moi'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE NhanVien (
    MaNV INT AUTO_INCREMENT PRIMARY KEY,
    HoTen VARCHAR(100) NOT NULL,
    NgaySinh DATE,
    NgayVaoLam DATE,
    LuongCoBan DECIMAL(15,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE LoaiGhe (
    MaLoaiGhe INT AUTO_INCREMENT PRIMARY KEY,
    TenLoaiGhe VARCHAR(20),
    GiaPhuThu INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE PhongChieu (
    MaPhong INT AUTO_INCREMENT PRIMARY KEY,
    TenPhong VARCHAR(20),
    LoaiPhong VARCHAR(10),
    SoHang INT,
    SoGheMoiHang INT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE Ghe (
    MaGhe INT AUTO_INCREMENT PRIMARY KEY,
    MaPhong INT,
    MaLoaiGhe INT,
    HangGhe CHAR(1),
    SoGhe INT,

    FOREIGN KEY (MaPhong) REFERENCES PhongChieu(MaPhong),
    FOREIGN KEY (MaLoaiGhe) REFERENCES LoaiGhe(MaLoaiGhe)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE TheLoaiPhim (
    MaLoaiPhim INT AUTO_INCREMENT PRIMARY KEY,
    TenLoaiPhim VARCHAR(50)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE Phim (
    MaPhim INT AUTO_INCREMENT PRIMARY KEY,
    MaLoaiPhim INT,
    TenPhim VARCHAR(100),
    ThoiLuong INT,
    DaoDien VARCHAR(100),
    NamSanXuat YEAR,
    AnhMauPhim VARCHAR(255),

    NgayKhoiChieu DATE,
    TrangThai VARCHAR(20) DEFAULT 'DangChieu',

    FOREIGN KEY (MaLoaiPhim) REFERENCES TheLoaiPhim(MaLoaiPhim)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE SuatChieu (
    MaSuatChieu INT AUTO_INCREMENT PRIMARY KEY,
    MaPhim INT,
    MaPhong INT,
    NgayChieu DATE,
    GioBatDau TIME,
    GioKetThuc TIME,
    GiaVeGoc INT,
    TrangThai VARCHAR(20) DEFAULT 'SapChieu',

    FOREIGN KEY (MaPhim) REFERENCES Phim(MaPhim),
    FOREIGN KEY (MaPhong) REFERENCES PhongChieu(MaPhong)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE Ve (
    MaVe INT AUTO_INCREMENT PRIMARY KEY,
    MaGhe INT,
    MaSuatChieu INT,
    GiaVe INT,
    TrangThai VARCHAR(20) DEFAULT 'Trong',
    UNIQUE (MaGhe, MaSuatChieu),
    FOREIGN KEY (MaGhe) REFERENCES Ghe(MaGhe),
    FOREIGN KEY (MaSuatChieu) REFERENCES SuatChieu(MaSuatChieu)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE SanPham (
    MaSanPham INT AUTO_INCREMENT PRIMARY KEY,
    TenSanPham VARCHAR(100),
    HinhAnh VARCHAR(255),
    GiaBan INT,
    KichThuoc VARCHAR(10),
    SoLuong INT,
    TrangThai VARCHAR(20)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE HoaDon (
    MaHoaDon INT AUTO_INCREMENT PRIMARY KEY,
    MaKH INT,
    MaNV INT,
    NgayLapHoaDon DATETIME,
    TongTienVe INT DEFAULT 0,
    TongTienSanPham INT DEFAULT 0,
    TongThanhToan INT DEFAULT 0,

    FOREIGN KEY (MaKH) REFERENCES KhachHang(MaKH),
    FOREIGN KEY (MaNV) REFERENCES NhanVien(MaNV)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE ChiTietHoaDonVe (
    MaHoaDon INT,
    MaVe INT,
    DonGia INT,

    PRIMARY KEY (MaHoaDon, MaVe),

    FOREIGN KEY (MaHoaDon) REFERENCES HoaDon(MaHoaDon),
    FOREIGN KEY (MaVe) REFERENCES Ve(MaVe)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE ChiTietHoaDonSanPham (
    MaHoaDon INT,
    MaSanPham INT,
    SoLuong INT,
    DonGia INT,
    ThanhTien INT,

    PRIMARY KEY (MaHoaDon, MaSanPham),

    FOREIGN KEY (MaHoaDon) REFERENCES HoaDon(MaHoaDon),
    FOREIGN KEY (MaSanPham) REFERENCES SanPham(MaSanPham)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


USE bookticket;


-- 1. Bảng KhachHang
INSERT INTO KhachHang (HoTen, SDT, NgaySinh, DiemTichLuy, HangThanhVien) VALUES
('Nguyễn Văn An', '0912345678', '1985-03-12', 150, 'Thành viên bạc'),
('Trần Thị Bình', '0987654321', '1992-07-20', 320, 'Thành viên vàng'),
('Lê Văn Cường', '0971122334', '2000-11-05', 0, 'Thành viên mới'),
('Phạm Thị Dung', '0934567890', '1997-02-28', 80, 'Thành viên bạc'),
('Hoàng Văn Em', '0909876543', '1988-09-15', 450, 'Thành viên kim cương');

-- 2. Bảng NhanVien
INSERT INTO NhanVien (HoTen, NgaySinh, NgayVaoLam, LuongCoBan) VALUES
('Nguyễn Thị Hương', '1990-01-15', '2018-06-01', 8500000.00),
('Trần Minh Quân', '1987-04-22', '2016-09-15', 12000000.00),
('Lê Thị Ngọc', '1995-08-10', '2021-03-01', 7500000.00),
('Phạm Văn Hải', '1992-12-05', '2019-11-20', 9500000.00),
('Hoàng Thị Lan', '1989-06-30', '2017-02-10', 11000000.00);

-- 3. Bảng LoaiGhe
INSERT INTO LoaiGhe (TenLoaiGhe, GiaPhuThu) VALUES
('Thường', 0),
('VIP', 50000),
('Sweetbox', 80000),
('Couple', 120000),
('Deluxe', 150000);

-- 4. Bảng PhongChieu
INSERT INTO PhongChieu (TenPhong, LoaiPhong, SoHang, SoGheMoiHang) VALUES
('Phòng 1', '2D', 10, 15),
('Phòng 2', '3D', 8, 12),
('Phòng 3', 'IMAX', 9, 14),
('Phòng 4', '4DX', 7, 10),
('Phòng 5', '2D', 11, 16);

-- 5. Bảng Ghe (dùng MaPhong 1-5 và MaLoaiGhe 1-5)
INSERT INTO Ghe (MaPhong, MaLoaiGhe, HangGhe, SoGhe) VALUES
(1, 1, 'A', 1),
(1, 1, 'A', 2),
(2, 2, 'B', 5),
(3, 3, 'C', 8),
(4, 4, 'D', 10);

-- 6. Bảng TheLoaiPhim
INSERT INTO TheLoaiPhim (TenLoaiPhim) VALUES
('Hành động'),
('Tình cảm'),
('Kinh dị'),
('Hài hước'),
('Khoa học viễn tưởng');

-- 7. Bảng Phim (dùng MaLoaiPhim 1-5)
INSERT INTO Phim (MaLoaiPhim, TenPhim, ThoiLuong, DaoDien, NamSanXuat, AnhMauPhim, NgayKhoiChieu, TrangThai) VALUES
(1, 'Avengers: Endgame', 181, 'Anh em nhà Russo', 2019, 'https://example.com/avengers.jpg', '2025-12-01', 'DangChieu'),
(2, 'Titanic', 195, 'James Cameron', 1997, 'https://example.com/titanic.jpg', '2026-01-15', 'DangChieu'),
(3, 'The Conjuring', 112, 'James Wan', 2013, 'https://example.com/conjuring.jpg', '2026-02-20', 'DangChieu'),
(4, 'Deadpool & Wolverine', 128, 'Shawn Levy', 2024, 'https://example.com/deadpool.jpg', '2025-07-10', 'DangChieu'),
(5, 'Dune: Part Two', 166, 'Denis Villeneuve', 2024, 'https://example.com/dune.jpg', '2026-03-01', 'DangChieu');

-- 8. Bảng SuatChieu (dùng MaPhim 1-5 và MaPhong 1-5)
INSERT INTO SuatChieu (MaPhim, MaPhong, NgayChieu, GioBatDau, GioKetThuc, GiaVeGoc, TrangThai) VALUES
(1, 1, '2026-03-20', '18:00:00', '21:00:00', 120000, 'SapChieu'),
(2, 2, '2026-03-20', '19:30:00', '22:45:00', 150000, 'SapChieu'),
(3, 3, '2026-03-21', '20:00:00', '22:00:00', 130000, 'SapChieu'),
(4, 4, '2026-03-21', '17:00:00', '19:30:00', 140000, 'SapChieu'),
(5, 5, '2026-03-22', '18:30:00', '21:30:00', 160000, 'SapChieu');

-- 9. Bảng Ve (dùng MaGhe 1-5 và MaSuatChieu 1-5)
INSERT INTO Ve (MaGhe, MaSuatChieu, GiaVe, TrangThai) VALUES
(1, 1, 120000, 'Trong'),
(2, 2, 200000, 'Trong'),
(3, 3, 210000, 'Trong'),
(4, 4, 260000, 'Trong'),
(5, 5, 310000, 'Trong');

-- 10. Bảng SanPham
INSERT INTO SanPham (TenSanPham, HinhAnh, GiaBan, KichThuoc, SoLuong, TrangThai) VALUES
('Bắp rang bơ', 'https://example.com/popcorn.jpg', 45000, 'Lớn', 100, 'ConHang'),
('Coca Cola', 'https://example.com/coca.jpg', 25000, 'Nhỏ', 200, 'ConHang'),
('Combo Bắp + 2 Nước', 'https://example.com/combo1.jpg', 85000, 'Lớn', 50, 'ConHang'),
('Khoai tây chiên', 'https://example.com/fries.jpg', 35000, 'Vừa', 80, 'ConHang'),
('Nước suối', 'https://example.com/water.jpg', 15000, 'Nhỏ', 150, 'ConHang');

-- 11. Bảng HoaDon (dùng MaKH 1-5 và MaNV 1-5)
INSERT INTO HoaDon (MaKH, MaNV, NgayLapHoaDon, TongTienVe, TongTienSanPham, TongThanhToan) VALUES
(1, 1, '2026-03-17 10:15:00', 120000, 0, 120000),
(2, 2, '2026-03-17 11:30:00', 200000, 85000, 285000),
(3, 3, '2026-03-17 14:45:00', 210000, 45000, 255000),
(4, 4, '2026-03-17 16:20:00', 260000, 35000, 295000),
(5, 5, '2026-03-17 18:00:00', 310000, 25000, 335000);

-- 12. Bảng ChiTietHoaDonVe (1 vé tương ứng 1 hóa đơn)
INSERT INTO ChiTietHoaDonVe (MaHoaDon, MaVe, DonGia) VALUES
(1, 1, 120000),
(2, 2, 200000),
(3, 3, 210000),
(4, 4, 260000),
(5, 5, 310000);

-- 13. Bảng ChiTietHoaDonSanPham (một số hóa đơn có sản phẩm)
INSERT INTO ChiTietHoaDonSanPham (MaHoaDon, MaSanPham, SoLuong, DonGia, ThanhTien) VALUES
(2, 3, 1, 85000, 85000),
(3, 1, 1, 45000, 45000),
(4, 4, 1, 35000, 35000),
(5, 2, 1, 25000, 25000),
(1, 5, 2, 15000, 30000);  
INSERT INTO Phim (MaLoaiPhim, TenPhim, ThoiLuong, DaoDien, NamSanXuat, AnhMauPhim, NgayKhoiChieu, TrangThai)
VALUES
(1, 'Avengers: Endgame', 181, 'Anh em nhà Russo', 2019, 'https://picsum.photos/id/1015/800/1200', '2025-12-01', 'DangChieu'),

(2, 'Titanic', 195, 'James Cameron', 1997, 'https://picsum.photos/id/201/800/1200', '2026-01-15', 'DangChieu'),

(3, 'The Conjuring', 112, 'James Wan', 2013, 'https://picsum.photos/id/301/800/1200', '2026-02-20', 'DangChieu'),

(4, 'Deadpool & Wolverine', 128, 'Shawn Levy', 2024, 'https://picsum.photos/id/401/800/1200', '2025-07-10', 'DangChieu'),

(5, 'Dune: Part Two', 166, 'Denis Villeneuve', 2024, 'https://picsum.photos/id/501/800/1200', '2026-03-01', 'DangChieu');