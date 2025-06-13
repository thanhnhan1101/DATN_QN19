package poly.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "NguoiDung")
public class NguoiDung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaNguoiDung")
    private Long maNguoiDung;

    @Column(name = "Email", length = 100, nullable = false, unique = true)
    private String email;

    @Column(name = "MatKhau", length = 255, nullable = false)
    private String matKhau;

    @Column(name = "HoTen", length = 100, columnDefinition = "NVARCHAR(100)")
    private String hoTen;

    @Column(name = "NgaySinh")
    private LocalDate ngaySinh;

    @Column(name = "GioiTinh")
    private Boolean gioiTinh;  // true: Nam, false: Nữ

    @Column(name = "SoDienThoai", length = 20)
    private String soDienThoai;

    @Column(name = "DiaChi", columnDefinition = "NVARCHAR(255)")
    private String diaChi;

    @Column(name = "AnhDaiDien", columnDefinition = "VARCHAR(MAX)")
    private String anhDaiDien;

    @Column(name = "VaiTro", columnDefinition = "NVARCHAR(20) DEFAULT N'user'")
    private String vaiTro = "user"; // user, reporter, admin

    @Column(name = "MaXacThuc", length = 6)
    private String maXacThuc;

    @Column(name = "NgayTao", columnDefinition = "DATETIME DEFAULT GETDATE()")
    private LocalDateTime ngayTao;

    @Column(name = "LanDangNhapCuoi")
    private LocalDateTime lanDangNhapCuoi;

    @Column(name = "TrangThai", columnDefinition = "BIT DEFAULT 1")
    private Boolean trangThai = true;

    @PrePersist
    protected void onCreate() {
        if (ngayTao == null) {
            ngayTao = LocalDateTime.now();
        }
    }
}