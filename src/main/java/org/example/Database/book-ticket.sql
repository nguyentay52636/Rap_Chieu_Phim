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
    TrangThai VARCHAR(20) DEFAULT 'Trong',

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

    FOREIGN KEY (MaLoaiPhim) REFERENCES TheLoaiPhim(MaLoaiPhim)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE SuatChieu (
    MaSuatChieu INT AUTO_INCREMENT PRIMARY KEY,
    MaPhim INT,
    MaPhong INT,
    GioBatDau DATETIME,
    GioKetThuc DATETIME,
    GiaVeGoc INT,

    FOREIGN KEY (MaPhim) REFERENCES Phim(MaPhim),
    FOREIGN KEY (MaPhong) REFERENCES PhongChieu(MaPhong)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE Ve (
    MaVe INT AUTO_INCREMENT PRIMARY KEY,
    MaGhe INT,
    MaSuatChieu INT,
    GiaVe INT,
    TrangThai VARCHAR(20),

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

-- =============================================
-- 1. KhachHang - 5 dòng dữ liệu mẫu
-- =============================================
INSERT INTO KhachHang (HoTen, SDT, NgaySinh, DiemTichLuy, HangThanhVien) VALUES
('Nguyễn Văn An', '0901234567', '1990-05-15', 250, 'Thành viên vàng'),
('Trần Thị Bình', '0912345678', '1995-08-20', 120, 'Thành viên bạc'),
('Lê Văn Cường', '0987654321', '1988-12-10', 450, 'VIP'),
('Phạm Thị Dung', '0934567890', '2000-03-25', 50, 'Thành viên mới'),
('Hoàng Văn Em', '0978901234', '1992-07-30', 180, 'Thành viên vàng');

-- =============================================
-- 2. NhanVien - 5 dòng dữ liệu mẫu
-- =============================================
INSERT INTO NhanVien (HoTen, NgaySinh, NgayVaoLam, LuongCoBan) VALUES
('Nguyễn Thị Quản Lý', '1995-01-01', '2020-06-01', 15000000.00),
('Trần Văn Bảo', '1992-03-15', '2021-09-10', 12000000.00),
('Lê Thị Hương', '1998-07-20', '2022-01-05', 13500000.00),
('Phạm Minh Quân', '1990-11-30', '2019-04-15', 18000000.00),
('Hoàng Thị Lan', '1996-05-10', '2023-02-20', 11000000.00);

-- =============================================
-- 3. LoaiGhe - 5 dòng dữ liệu mẫu
-- =============================================
INSERT INTO LoaiGhe (TenLoaiGhe, GiaPhuThu) VALUES
('Ghế Thường', 0),
('Ghế VIP', 50000),
('Sweetbox', 100000),
('Ghế Couple', 80000),
('Recliner', 120000);

-- =============================================
-- 4. PhongChieu - 5 dòng dữ liệu mẫu
-- =============================================
INSERT INTO PhongChieu (TenPhong, LoaiPhong, SoHang, SoGheMoiHang) VALUES
('Phòng 1', '2D', 5, 10),
('Phòng 2', '3D', 6, 8),
('Phòng 3', 'IMAX', 4, 12),
('Phòng 4', '4DX', 5, 9),
('Phòng 5', '2D', 7, 10);

-- =============================================
-- 5. Ghe - 5 dòng dữ liệu mẫu (dùng phòng 1 + loại ghế 1-5)
-- =============================================
INSERT INTO Ghe (MaPhong, MaLoaiGhe, HangGhe, SoGhe, TrangThai) VALUES
(1, 1, 'A', 1, 'Trong'),
(1, 2, 'A', 2, 'Trong'),
(1, 3, 'B', 1, 'Da dat'),
(1, 4, 'B', 2, 'Trong'),
(1, 5, 'C', 1, 'Trong');

-- =============================================
-- 6. TheLoaiPhim - 5 dòng dữ liệu mẫu
-- =============================================
INSERT INTO TheLoaiPhim (TenLoaiPhim) VALUES
('Hành động'),
('Kinh dị'),
('Tình cảm'),
('Hoạt hình'),
('Khoa học viễn tưởng');

-- =============================================
-- 7. Phim - 5 dòng dữ liệu mẫu
-- =============================================
INSERT INTO Phim (MaLoaiPhim, TenPhim, ThoiLuong, DaoDien, NamSanXuat, AnhMauPhim) VALUES
(1, 'Avengers: Endgame', 181, 'Anthony Russo', 2019, 'https://example.com/avengers.jpg'),
(2, 'The Conjuring', 112, 'James Wan', 2013, 'https://example.com/conjuring.jpg'),
(3, 'Titanic', 194, 'James Cameron', 1997, 'https://example.com/titanic.jpg'),
(4, 'Frozen 2', 103, 'Chris Buck', 2019, 'https://example.com/frozen2.jpg'),
(5, 'Dune: Part Two', 166, 'Denis Villeneuve', 2024, 'https://example.com/dune2.jpg');

-- =============================================
-- 8. SuatChieu - 5 dòng dữ liệu mẫu
-- =============================================
INSERT INTO SuatChieu (MaPhim, MaPhong, GioBatDau, GioKetThuc, GiaVeGoc) VALUES
(1, 1, '2026-03-14 10:00:00', '2026-03-14 13:01:00', 120000),
(2, 2, '2026-03-14 14:30:00', '2026-03-14 16:22:00', 90000),
(3, 3, '2026-03-14 19:00:00', '2026-03-14 22:14:00', 150000),
(4, 4, '2026-03-15 09:15:00', '2026-03-15 11:00:00', 80000),
(5, 5, '2026-03-15 13:45:00', '2026-03-15 16:31:00', 140000);

-- =============================================
-- 9. Ve - 5 dòng dữ liệu mẫu
-- =============================================
INSERT INTO Ve (MaGhe, MaSuatChieu, GiaVe, TrangThai) VALUES
(1, 1, 120000, 'Da ban'),
(2, 1, 170000, 'Da ban'),
(3, 2, 90000, 'Trong'),
(4, 3, 150000, 'Da ban'),
(5, 4, 80000, 'Trong');

-- =============================================
-- 10. SanPham - 5 dòng dữ liệu mẫu
-- =============================================
INSERT INTO SanPham (TenSanPham, HinhAnh, GiaBan, KichThuoc, SoLuong, TrangThai) VALUES
('Bắp Ngọt Lớn', 'https://example.com/popcorn.jpg', 45000, 'L', 100, 'Con hang'),
('Coca Cola', 'https://example.com/coca.jpg', 30000, 'M', 150, 'Con hang'),
('Combo Bắp + Nước', 'https://example.com/combo1.jpg', 75000, 'Combo', 80, 'Con hang'),
('Nachos', 'https://example.com/nachos.jpg', 55000, 'L', 60, 'Con hang'),
('Khoai Tây Chiên', 'https://example.com/fries.jpg', 35000, 'M', 90, 'Con hang');

-- =============================================
-- 11. HoaDon - 5 dòng dữ liệu mẫu
-- =============================================
INSERT INTO HoaDon (MaKH, MaNV, NgayLapHoaDon, TongTienVe, TongTienSanPham, TongThanhToan) VALUES
(1, 1, '2026-03-14 10:30:00', 290000, 75000, 365000),
(2, 2, '2026-03-14 15:00:00', 90000, 0, 90000),
(3, 3, '2026-03-14 20:00:00', 150000, 45000, 195000),
(4, 4, '2026-03-15 10:00:00', 80000, 30000, 110000),
(5, 5, '2026-03-15 14:00:00', 140000, 75000, 215000);

-- =============================================
-- 12. ChiTietHoaDonVe - 5 dòng dữ liệu mẫu
-- =============================================
INSERT INTO ChiTietHoaDonVe (MaHoaDon, MaVe, DonGia) VALUES
(1, 1, 120000),
(1, 2, 170000),
(2, 3, 90000),
(3, 4, 150000),
(4, 5, 80000);

-- =============================================
-- 13. ChiTietHoaDonSanPham - 5 dòng dữ liệu mẫu
-- =============================================
INSERT INTO ChiTietHoaDonSanPham (MaHoaDon, MaSanPham, SoLuong, DonGia, ThanhTien) VALUES
(1, 1, 1, 45000, 45000),
(1, 2, 2, 30000, 60000),
(3, 3, 1, 75000, 75000),
(4, 4, 1, 55000, 55000),
(5, 5, 1, 35000, 35000);