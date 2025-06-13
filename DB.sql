CREATE TABLE NguoiDung (
    MaNguoiDung BIGINT PRIMARY KEY IDENTITY(1,1),
    Email VARCHAR(100) NOT NULL UNIQUE,
    MatKhau VARCHAR(255) NOT NULL,
    HoTen NVARCHAR(100),
    NgaySinh DATE,
    GioiTinh BIT, -- 1: Nam, 0: Nữ
    SoDienThoai VARCHAR(20),
    DiaChi NVARCHAR(255),
    AnhDaiDien VARCHAR(MAX), -- đường dẫn ảnh đại diện
    VaiTro NVARCHAR(20) DEFAULT N'user',
    MaXacThuc VARCHAR(6),
    NgayTao DATETIME DEFAULT GETDATE(),
    LanDangNhapCuoi DATETIME,
    TrangThai BIT DEFAULT 1 -- 1: hoạt động, 0: đã xóa tài khoản
);

CREATE TABLE DanhMuc (
    MaDanhMuc INT PRIMARY KEY IDENTITY(1,1),
    TenDanhMuc NVARCHAR(100) NOT NULL UNIQUE,
    DuongDan VARCHAR(100) NOT NULL UNIQUE,
    MoTa NVARCHAR(500),
    MaDanhMucCha INT,
    NgayTao DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (MaDanhMucCha) REFERENCES DanhMuc(MaDanhMuc)
);

CREATE TABLE BaiViet (
    MaBaiViet INT PRIMARY KEY IDENTITY(1,1),
    TieuDe NVARCHAR(255) NOT NULL,
    DuongDan VARCHAR(255) NOT NULL UNIQUE,
    TomTat NVARCHAR(500),
    NoiDung NVARCHAR(MAX) NOT NULL,
    HinhAnh VARCHAR(MAX),
    LuotXem INT DEFAULT 0,
    TrangThai NVARCHAR(20) DEFAULT N'Nháp', -- Nháp, Đã đăng, Đã ẩn
    NgayDang DATETIME,
    NgayTao DATETIME DEFAULT GETDATE(),
    NgaySua DATETIME,
    MaDanhMuc INT,
    MaTacGia BIGINT,
    FOREIGN KEY (MaDanhMuc) REFERENCES DanhMuc(MaDanhMuc),
    FOREIGN KEY (MaTacGia) REFERENCES NguoiDung(MaNguoiDung)
);

CREATE TABLE LichSuXem (
    MaLichSu BIGINT PRIMARY KEY IDENTITY(1,1),
    MaNguoiDung BIGINT,
    MaBaiViet INT,
    ThoiGianXem DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (MaNguoiDung) REFERENCES NguoiDung(MaNguoiDung),
    FOREIGN KEY (MaBaiViet) REFERENCES BaiViet(MaBaiViet)
);

CREATE TABLE BaiVietDaLuu (
    MaLuu BIGINT PRIMARY KEY IDENTITY(1,1),
    MaNguoiDung BIGINT,
    MaBaiViet INT,
    NgayLuu DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (MaNguoiDung) REFERENCES NguoiDung(MaNguoiDung),
    FOREIGN KEY (MaBaiViet) REFERENCES BaiViet(MaBaiViet)
);

CREATE TABLE TuongTac (
    MaTuongTac BIGINT PRIMARY KEY IDENTITY(1,1),
    MaNguoiDung BIGINT,
    MaBaiViet INT,
    LoaiTuongTac NVARCHAR(20), -- 'like', 'dislike', 'share_email', 'share_fb'
    ThoiGian DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (MaNguoiDung) REFERENCES NguoiDung(MaNguoiDung),
    FOREIGN KEY (MaBaiViet) REFERENCES BaiViet(MaBaiViet)
);

CREATE TABLE BinhLuan (
    MaBinhLuan BIGINT PRIMARY KEY IDENTITY(1,1),
    MaNguoiDung BIGINT,
    MaBaiViet INT,
    NoiDung NVARCHAR(MAX),
    MaBinhLuanCha BIGINT NULL, -- Cho phép trả lời bình luận
    NgayBinhLuan DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (MaNguoiDung) REFERENCES NguoiDung(MaNguoiDung),
    FOREIGN KEY (MaBaiViet) REFERENCES BaiViet(MaBaiViet),
    FOREIGN KEY (MaBinhLuanCha) REFERENCES BinhLuan(MaBinhLuan)
);

CREATE TABLE ThichBinhLuan (
    MaThich BIGINT PRIMARY KEY IDENTITY(1,1),
    MaNguoiDung BIGINT,
    MaBinhLuan BIGINT,
    NgayThich DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (MaNguoiDung) REFERENCES NguoiDung(MaNguoiDung),
    FOREIGN KEY (MaBinhLuan) REFERENCES BinhLuan(MaBinhLuan)
);

CREATE TABLE QuangCao (
    MaQuangCao INT PRIMARY KEY IDENTITY(1,1),
    TenQuangCao NVARCHAR(255) NOT NULL,
    HinhAnh VARCHAR(255),
    DuongDan VARCHAR(500),
    ViTri NVARCHAR(50),
    KichThuoc NVARCHAR(20),
    NgayBatDau DATETIME,
    NgayKetThuc DATETIME,
    LuotClick INT DEFAULT 0,
    DuongDanURL VARCHAR(500),
    TrangThai BIT DEFAULT 1,
    GiaTheoNgay DECIMAL(10,2),
    NhaTaiTro NVARCHAR(255),
    NgayTao DATETIME DEFAULT GETDATE()
);
