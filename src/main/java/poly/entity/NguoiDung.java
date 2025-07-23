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

    @Column(name = "Email", length = 50, nullable = false, unique = true)
    private String email;

    @Column(name = "MatKhau", length = 60, nullable = false)
    private String matKhau;

    @Column(name = "HoTen", length = 50)
    private String hoTen;

    @Column(name = "GioiTinh")
    private Boolean gioiTinh; // true: Nam, false: Nữ

    @Column(name = "NgaySinh")
    private LocalDate ngaySinh;

    @Column(name = "SoDienThoai", length = 10)
    private String soDienThoai;

    @Column(name = "VaiTro", length = 10)
    private String vaiTro = "user"; // user, reporter, admin

    @Column(name = "TrangThai", length = 15)
    private String trangThai = "Hoạt động"; // Hoạt động, Khóa

    @Column(name = "NgayTao")
    private LocalDateTime ngayTao;

    @PrePersist
    protected void onCreate() {
        if (ngayTao == null) {
            ngayTao = LocalDateTime.now();
        }
    }
}