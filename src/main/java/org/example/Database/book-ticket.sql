drop database bookticket;
create database bookticket;

use bookticket;

CREATE TABLE KhachHang (
    MaKH INT AUTO_INCREMENT  PRIMARY KEY,
    HoTen VARCHAR(100) NOT NULL,
    SDT VARCHAR(15) UNIQUE,
    NgaySinh DATE,
    DiemTichLuy INT DEFAULT 0,
    HangThanhVien VARCHAR(50) DEFAULT N'Thành viên mới'
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE NhanVien (
	MaNV INT AUTO_INCREMENT PRIMARY KEY,
	HoTen VARCHAR(100) NOT NULL,
	NgaySinh DATE,
	NgayVaoLam DATE,
	LuongCoBAn DECIMAL(15,3) NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE Ghe (
    MaGhe INT AUTO_INCREMENT PRIMARY KEY,
    MaPhong INT,
    MaLoaiGhe INT,
    HangGhe CHAR(1),
    SoGhe INT,
    FOREIGN KEY (MaPhong) REFERENCES PhongChieu(MaPhong),
    FOREIGN KEY (MaLoaiGhe) REFERENCES LoaiGhe(MaLoaiGhe)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE PhongChieu (
    MaPhong INT AUTO_INCREMENT PRIMARY KEY,
    TenPhong VARCHAR(20),
    LoaiPhong VARCHAR(10),
    SoHang INT,
    SoGheMoiHang INT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

 CREATE TABLE SuatChieu (
 MaSuatChieu INT AUTO_INCREMENT PRIMARY KEY,
 TenSuat VARCHAR(20), 
GioBatDau DATETIME,
GioKetThuc DATETIME
 
 )ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
 
 CREATE TABLE TheLoaiPhim (
 MaLoaiPhim INT AUTO_INCREMENT PRIMARY KEY,
 TenLoaiPhim VARCHAR(20) 

 )ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
 
 CREATE TABLE Phim (
 MaPhim INT AUTO_INCREMENT PRIMARY KEY,
 MaLoaiPhim INT,
 TenPhim VARCHAR(20),
 ThoiLuong TIME,
 DaoDien VARCHAR(60),
 NamSanXuat DATETIME,
 AnhMauPhim VARCHAR(999),
 
 FOREIGN KEY (MaLoaiPhim) REFERENCES TheLoaiPhim(MaLoaiPhim)
 )ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
 
 CREATE TABLE SanPham (
 MaSanPham INT AUTO_INCREMENT PRIMARY KEY,
 TenSanPham VARCHAR(100),
  HinhAnh VARCHAR(255),
 GiaBan INT,
 KichThuoc VARCHAR(4),
 SoLuong INT,
 TrangThai VARCHAR(10)
 )ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
 
 CREATE TABLE Ghe (
 MaGhe INT AUTO_INCREMENT PRIMARY KEY,
 MaPhong INT, 
 MaLoaiGhe INT,
 LoaiGhe VARCHAR(5),
 soThuTu INT,
 TrangThai VARCHAR(10),

 FOREIGN KEY (MaLoaiGhe) REFERENCES LoaiGhe(MaLoaiGhe),
 FOREIGN KEY (MaPhong) REFERENCES PhongChieu(MaPhong)
 )ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
 
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
 MaVe INT AUTO_INCREMENT  PRIMARY KEY,
 MaGhe INT,
 MaSuatChieu INT,
 GiaVe INT,
 TrangThai varchar(20),
 
 FOREIGN KEY (MaGhe) REFERENCES Ghe(MaGhe),
 FOREIGN KEY (MaSuatChieu) REFERENCES SuatChieu(MaSuatChieu)
 )ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

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
 
 CREATE TABLE ChiTietSanPham (
 MaHoaDon INT,
 MaSanPham INT NULL,
 Soluong INT,
 ThanhTien INT,
  
 FOREIGN KEY (MaHoaDon) REFERENCES HoaDon(MaHoaDon),
 FOREIGN KEY (MaSanPham) REFERENCES SanPham(MaSanPham)
 )ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

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

