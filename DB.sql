-- BẢNG NGƯỜI DÙNG
CREATE TABLE NguoiDung (
    MaNguoiDung BIGINT PRIMARY KEY IDENTITY(1,1),
    Email VARCHAR(50) NOT NULL UNIQUE,
    MatKhau VARCHAR(60) NOT NULL,
    HoTen NVARCHAR(50),
    GioiTinh BIT,
    NgaySinh DATE,
    SoDienThoai VARCHAR(10),
    VaiTro NVARCHAR(10) DEFAULT N'user',
    TrangThai NVARCHAR(15) DEFAULT N'Hoạt động'
        CHECK (TrangThai IN (N'Hoạt động', N'Khóa')),
    NgayTao DATETIME DEFAULT GETDATE()
);

-- BẢNG DANH MỤC
CREATE TABLE DanhMuc (
    MaDanhMuc INT PRIMARY KEY IDENTITY(1,1),
    TenDanhMuc NVARCHAR(25) NOT NULL,
    MoTa NVARCHAR(255),
    ThuTuHienThi INT DEFAULT 0,
    DuongDan VARCHAR(10) NOT NULL DEFAULT ''
);

-- BẢNG BÀI VIẾT
CREATE TABLE BaiViet (
    MaBaiViet BIGINT PRIMARY KEY IDENTITY(1,1),
    TieuDe NVARCHAR(100) NOT NULL,
    TomTat NVARCHAR(500),
    NoiDung NVARCHAR(MAX) NOT NULL,
    DuongDanAnh1 NVARCHAR(MAX),
    DuongDanAnh2 NVARCHAR(MAX),
    DuongDanAnh3 NVARCHAR(MAX),
    MaDanhMuc INT,
    MaTacGia BIGINT,
    TrangThai NVARCHAR(15) DEFAULT N'Nháp'
        CHECK (TrangThai IN (N'Nháp', N'Chờ duyệt', N'Đã xuất bản', N'Từ chối')),
    NgayXuatBan DATETIME NULL,
    LuotXem INT DEFAULT 0,
    NgayTao DATETIME DEFAULT GETDATE(),
    NgayCapNhat DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (MaDanhMuc) REFERENCES DanhMuc(MaDanhMuc),
    FOREIGN KEY (MaTacGia) REFERENCES NguoiDung(MaNguoiDung)
);

-- BẢNG LỊCH SỬ CHỈNH SỬA
CREATE TABLE LichSuChinhSua (
    MaLichSu BIGINT PRIMARY KEY IDENTITY(1,1),
    MaBaiViet BIGINT,
    MaNguoiSua BIGINT,
    NgayChinhSua DATETIME DEFAULT GETDATE(),
    NoiDungCu NVARCHAR(MAX),
    FOREIGN KEY (MaBaiViet) REFERENCES BaiViet(MaBaiViet),
    FOREIGN KEY (MaNguoiSua) REFERENCES NguoiDung(MaNguoiDung)
);

-- BẢNG BÌNH LUẬN
CREATE TABLE BinhLuan (
    MaBinhLuan BIGINT PRIMARY KEY IDENTITY(1,1),
    MaBaiViet BIGINT,
    MaNguoiDung BIGINT,
    NoiDung NVARCHAR(100) NOT NULL,
    TrangThai NVARCHAR(15) DEFAULT N'Chờ duyệt'
        CHECK (TrangThai IN (N'Chờ duyệt', N'Đã duyệt', N'Bị từ chối')),
    NgayTao DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (MaBaiViet) REFERENCES BaiViet(MaBaiViet),
    FOREIGN KEY (MaNguoiDung) REFERENCES NguoiDung(MaNguoiDung)
);

-- BẢNG YÊU THÍCH (CÓ TRẠNG THÁI)
CREATE TABLE YeuThich (
    MaYeuThich BIGINT PRIMARY KEY IDENTITY(1,1),
    MaNguoiDung BIGINT,
    MaBaiViet BIGINT,
    TrangThai NVARCHAR(20) DEFAULT N'Đã thích'
        CHECK (TrangThai IN (N'Đã thích', N'Đã bỏ thích')),
    NgayTao DATETIME DEFAULT GETDATE(),
    NgayCapNhat DATETIME DEFAULT GETDATE(),

    UNIQUE (MaNguoiDung, MaBaiViet),
    FOREIGN KEY (MaNguoiDung) REFERENCES NguoiDung(MaNguoiDung),
    FOREIGN KEY (MaBaiViet) REFERENCES BaiViet(MaBaiViet)
);

-- BẢNG QUẢNG CÁO
CREATE TABLE QuangCao (
    MaQuangCao BIGINT PRIMARY KEY IDENTITY(1,1),
    MaNguoiDung BIGINT,
    TieuDe NVARCHAR(255),
    DuongDanAnh NVARCHAR(MAX),
    DuongDanLienKet NVARCHAR(MAX),
    ViTri NVARCHAR(20)
        CHECK (ViTri IN (N'Trên', N'Bên', N'Dưới')),
    NgayBatDau DATE,
    NgayKetThuc DATE,
    TrangThai NVARCHAR(20) DEFAULT N'Chờ duyệt'
        CHECK (TrangThai IN (N'Chờ duyệt', N'Đã duyệt', N'Từ chối')),
    NgayTao DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (MaNguoiDung) REFERENCES NguoiDung(MaNguoiDung)
);

-- BẢNG THÔNG BÁO
CREATE TABLE ThongBao (
    MaThongBao BIGINT PRIMARY KEY IDENTITY(1,1),
    TieuDe NVARCHAR(255),
    NoiDung NVARCHAR(100),
    MaNguoiNhan BIGINT,
    TrangThai NVARCHAR(20) DEFAULT N'Chưa đọc'
        CHECK (TrangThai IN (N'Chưa đọc', N'Đã đọc')),
    NgayGui DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (MaNguoiNhan) REFERENCES NguoiDung(MaNguoiDung)
);

-- BẢNG PHẢN HỒI
CREATE TABLE PhanHoi (
    MaPhanHoi BIGINT PRIMARY KEY IDENTITY(1,1),
    MaNguoiDung BIGINT,
    NoiDung NVARCHAR(255),
    TrangThai NVARCHAR(20) DEFAULT N'Chưa xử lý'
        CHECK (TrangThai IN (N'Chưa xử lý', N'Đã xử lý')),
    NgayGui DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (MaNguoiDung) REFERENCES NguoiDung(MaNguoiDung)
);

-- BẢNG LỊCH SỬ ĐĂNG NHẬP
CREATE TABLE LichSuDangNhap (
    MaLichSu BIGINT PRIMARY KEY IDENTITY(1,1),
    MaNguoiDung BIGINT,
    DiaChiIP NVARCHAR(50),
    ThietBi NVARCHAR(100),
    ThoiGian DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (MaNguoiDung) REFERENCES NguoiDung(MaNguoiDung)
);

-- BẢNG LỊCH SỬ TƯƠNG TÁC
CREATE TABLE LichSuTuongTac (
    MaTuongTac BIGINT PRIMARY KEY IDENTITY(1,1),
    MaNguoiDung BIGINT,
    MaBaiViet BIGINT,
    LoaiTuongTac NVARCHAR(15)
        CHECK (LoaiTuongTac IN (N'Xem', N'Thích', N'Bình luận')),
    NgayTuongTac DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (MaNguoiDung) REFERENCES NguoiDung(MaNguoiDung),
    FOREIGN KEY (MaBaiViet) REFERENCES BaiViet(MaBaiViet)
);

